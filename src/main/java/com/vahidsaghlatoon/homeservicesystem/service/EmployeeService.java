package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.exception.NotExistException;
import com.vahidsaghlatoon.homeservicesystem.model.Employee;
import com.vahidsaghlatoon.homeservicesystem.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    public static final int EMPLOYEE_PER_PAGE = 4;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Employee saveOrUpdate(Employee theEmployee) {
        boolean isUpdatingEmployee = (theEmployee.getId() != null);
        if (isUpdatingEmployee) {
            Employee existedEmployee = employeeRepository.findById(theEmployee.getId()).get();
            if (theEmployee.getPassword().isEmpty()) {
                theEmployee.setPassword(existedEmployee.getPassword());
            } else {
                encodePassword(theEmployee);
            }
        } else {
            encodePassword(theEmployee);
        }
        return employeeRepository.save(theEmployee);
    }

    public Employee updateAccount(Employee employeeInForm) {
        Employee existedEmployee = employeeRepository.findById(employeeInForm.getId()).get();
        if (!employeeInForm.getPassword().isEmpty()) {
            existedEmployee.setPassword(employeeInForm.getPassword());
            encodePassword(existedEmployee);
        }
        if (employeeInForm.getPhoto() != null) {
            existedEmployee.setPhoto(employeeInForm.getPhoto());
        }
        existedEmployee.setFirstName(employeeInForm.getFirstName());
        existedEmployee.setLastName(employeeInForm.getLastName());
        return employeeRepository.save(existedEmployee);
    }

    private void encodePassword(Employee employee) {
        String encodePassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodePassword);
    }

    @Transactional
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee findById(Long theId) {
        return employeeRepository.findById(theId)
                .orElseThrow(() -> new NotExistException("Did not find employee by id" + theId));
    }

    @Transactional
    public Employee deleteById(Long theId) {
        Employee theEmployee;
        if ((theEmployee = findById(theId)) == null)
            throw new NotExistException("Did not find employee by id" + theId);
        employeeRepository.deleteById(theId);
        return theEmployee;
    }

    @Transactional
    public boolean isEmailUnique(Long id, String theEmail) {
        Employee theEmployee = employeeRepository.findByEmail(theEmail.trim());
        if (theEmployee == null) {
            return true;
        }
        if (id != null) {
            return theEmail.equals(theEmployee.getEmail());
        }
        return false;
    }

    @Transactional
    public void updateEmployeeEnableStatus(Long id, boolean enabled) {
        employeeRepository.updateEnableStatus(id, enabled);
    }

    @Transactional
    public Page<Employee> findAllByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, EMPLOYEE_PER_PAGE, sort);
        if (keyword != null) {
            return employeeRepository.findAllByKeyword(keyword, pageable);
        }
        return employeeRepository.findAll(pageable);
    }

    @Transactional
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
}
