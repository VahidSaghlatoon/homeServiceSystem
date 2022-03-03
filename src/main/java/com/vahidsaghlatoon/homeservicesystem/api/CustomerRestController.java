package com.vahidsaghlatoon.homeservicesystem.api;

import com.vahidsaghlatoon.homeservicesystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

    @PostMapping("/check_email")
    public String checkDuplicateEmail(@Param("id") Long id, @Param("email") String email) {
        return customerService.isEmailUnique(id, email) ? "Ok" : "Duplicate";
    }
    @PostMapping("/check_nationalCode")
    public String checkDuplicateNationalCode(@Param("id") Long id, @Param("nationalCode") String nationalCode) {
        return customerService.isNationalCodeUnique(id, nationalCode) ? "Ok" : "Duplicate";
    }

}
