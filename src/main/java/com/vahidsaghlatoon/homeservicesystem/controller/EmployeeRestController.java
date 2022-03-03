package com.vahidsaghlatoon.homeservicesystem.controller;

import com.vahidsaghlatoon.homeservicesystem.model.Employee;
import com.vahidsaghlatoon.homeservicesystem.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeRestController {
    private final EmployeeService employeeService;

    @PostMapping("/check_email")
    public String checkDuplicateEmail(@Param("id") Long id, @Param("email") String email) {
        return employeeService.isEmailUnique(id, email) ? "Ok" : "Duplicate";
    }
    @PreAuthorize("hasAnyRole('مدیر')")
    @GetMapping("/api")
    public ResponseEntity<List<Employee>> findAll(){
        List<Employee> employeeList = employeeService.findAll();
        return ResponseEntity.ok(employeeList);
    }
}
