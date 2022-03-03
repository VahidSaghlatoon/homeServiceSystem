package com.vahidsaghlatoon.homeservicesystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
public class PasswordEncoderTest {

    @Test
    public void test_encode_password(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "vs123456";
        String encodePassword = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodePassword);
        assertTrue(matches);

    }
}
