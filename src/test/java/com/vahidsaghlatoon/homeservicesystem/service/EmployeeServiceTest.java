package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.config.AppConfig;
import com.vahidsaghlatoon.homeservicesystem.exception.NotExistException;
import com.vahidsaghlatoon.homeservicesystem.model.Employee;
import com.vahidsaghlatoon.homeservicesystem.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@SpringJUnitConfig(AppConfig.class)
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RoleService roleService;

    @Test
    void test_save_isOk() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        Employee result = employeeService.saveOrUpdate(theEmployee);
        assertNotNull(result);
    }

    @Test
    void test_save_invalid_input_isOk() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password(" ")
                .build();
        assertThrows(Exception.class, () -> employeeService.saveOrUpdate(theEmployee));
    }

    @Test
    void test_find_all_isOk() {

        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        List<Employee> result = employeeService.findAll();
        assertNotNull(result);
    }

    @Test
    void test_find_all_by_page_isOk() {
        int pageNumber = 1;
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        employeeService.saveOrUpdate(theEmployee);
        Page<Employee> page = employeeService.findAllByPage(pageNumber,"firstName","asc",null);
        List<Employee> employeeList = page.getContent();
        assertNotNull(employeeList);
    }

    @Test
    void test_find_by_id_isOk() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        Employee newEmployee = employeeService.saveOrUpdate(theEmployee);
        Employee result = employeeService.findById(1l);
        assertNotNull(result);
    }

    @Test
    void test_find_by_invalid_id_isOk() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        Employee newEmployee = employeeService.saveOrUpdate(theEmployee);
        assertThrows(NotExistException.class, () -> employeeService.findById(2l));
    }

    @Test
    void delete_by_id_isOk() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        Employee newEmployee = employeeService.saveOrUpdate(theEmployee);
        Employee result = employeeService.deleteById(1l);
        assertNotNull(result);
    }

    @Test
    void delete_by_invalid_id_isOk() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        Employee newEmployee = employeeService.saveOrUpdate(theEmployee);
        assertThrows(NotExistException.class, () -> employeeService.deleteById(2l));
    }

    @Test
    void add_role_isOk() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        Employee newEmployee = employeeService.saveOrUpdate(theEmployee);

        Role theRole = Role.builder()
                .title("مدیر")
                .description("مدیریت تمام امور و دسترسی به تمامی قسمت های سیستم")
                .build();
        Role role = roleService.saveOrUpdate(theRole);

        theEmployee.addRole(role);

        assertTrue(theEmployee.getRoles().size() > 0);
    }

    @Test
    public void test_find_by_email() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        Employee newEmployee = employeeService.saveOrUpdate(theEmployee);
        boolean result = employeeService.isEmailUnique(1L, "vahid.d.w@gmail.com   ");
        assertTrue(result);

    }

    @Test
    public void test_find_by_Invalid_email() {
        Employee theEmployee = Employee.builder()
                .firstName("وحید")
                .lastName("سقلاطون")
                .email("vahid.d.w@gmail.com")
                .password("vs123456")
                .build();
        Employee newEmployee = employeeService.saveOrUpdate(theEmployee);
        boolean result = employeeService.isEmailUnique(1L, "nahid.d.w@gmail.com   ");
        assertFalse(result);

    }


}