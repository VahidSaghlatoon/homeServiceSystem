package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.config.AppConfig;
import com.vahidsaghlatoon.homeservicesystem.exception.NotExistException;
import com.vahidsaghlatoon.homeservicesystem.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@SpringJUnitConfig(AppConfig.class)
class RoleServiceTest {
    @Autowired
    private RoleService roleService;

    @Test
    void test_save_isOk() {
        Role theRole = Role.builder()
                .title("مدیر")
                .description("مدیریت تمام امور و دسترسی به تمامی قسمت های سیستم")
                .build();
        Role result = roleService.saveOrUpdate(theRole);
        assertNotNull(result);
    }

    @Test
    void test_save_invalid_input_isOk() {
        Role theRole = Role.builder()
                .title("")
                .description("مدیریت تمام امور و دسترسی به تمامی قسمت های سیستم")
                .build();
       assertThrows(Exception.class ,()-> roleService.saveOrUpdate(theRole));
    }

    @Test
    void test_find_all_isOk() {

        Role theRole = Role.builder()
                .title("مدیر")
                .description("مدیریت تمام امور و دسترسی به تمامی قسمت های سیستم")
                .build();
        Role newRole = roleService.saveOrUpdate(theRole);
        List<Role> result = roleService.findAll();
        assertNotNull(result);
    }

    @Test
    void test_find_by_id_isOk() {
        Role theRole = Role.builder()
                .title("مدیر")
                .description("مدیریت تمام امور و دسترسی به تمامی قسمت های سیستم")
                .build();
        Role newRole = roleService.saveOrUpdate(theRole);
        Role result = roleService.findById(1l);
        assertNotNull(result);
    }

    @Test
    void test_find_by_invalid_id_isOk() {
        Role theRole = Role.builder()
                .title("مدیر")
                .description("مدیریت تمام امور و دسترسی به تمامی قسمت های سیستم")
                .build();
        Role newRole = roleService.saveOrUpdate(theRole);
        assertThrows(NotExistException.class, () -> roleService.findById(2l));
    }

    @Test
      void delete_by_id_isOk() {
        Role theRole = Role.builder()
                .title("مدیر")
                .description("مدیریت تمام امور و دسترسی به تمامی قسمت های سیستم")
                .build();
        Role newRole = roleService.saveOrUpdate(theRole);
        Role result = roleService.deleteById(1l);
        assertNotNull(result);
    }

    @Test
    void delete_by_invalid_id_isOk() {
        Role theRole = Role.builder()
                .title("مدیر")
                .description("مدیریت تمام امور و دسترسی به تمامی قسمت های سیستم")
                .build();
        Role newRole = roleService.saveOrUpdate(theRole);
        assertThrows(NotExistException.class, () -> roleService.deleteById(2l));
    }
}