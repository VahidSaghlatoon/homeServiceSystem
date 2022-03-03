package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.model.EmployeeDetails;
import com.vahidsaghlatoon.homeservicesystem.model.Employee;
import com.vahidsaghlatoon.homeservicesystem.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository ;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email);
        if(employee!=null){
            return new EmployeeDetails(employee);
        }
        throw new UsernameNotFoundException("Could not find employee with email: " + email);
    }
}
