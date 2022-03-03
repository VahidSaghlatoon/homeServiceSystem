package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.config.AppConfig;
import com.vahidsaghlatoon.homeservicesystem.exception.InvalidInputException;
import com.vahidsaghlatoon.homeservicesystem.model.MainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringJUnitConfig({AppConfig.class})
@Rollback(value = false)
class MainServiceTest {
    @Autowired
    private MainServiceService mainServiceService;

    @Test
    public void test_save_mainService() {
        MainService theMainService = MainService.builder()
                .title("حمل اثاثیه")
                .description(" تمامی وسایل")
                .build();
        MainService result = mainServiceService.saveOrUpdate(theMainService);
        assertNotNull(result);
    }

    @Test
    public void test_create_sub_service() {
        MainService theMainService = MainService.builder()
                .title("حمل اثاثیه")
                .description(" تمامی وسایل")
                .build();
        MainService mainService = mainServiceService.saveOrUpdate(theMainService);
        MainService subService = MainService.builder()
                .title("اثاث برقی")
                .description(" وزن کم")
                .mainService(mainService)
                .build();

        MainService theSubService = mainServiceService.saveOrUpdate(subService);
        assertNotNull(theSubService);
    }

    @Test
    public void test_save_mainService_if_input_be_invalid() {
        MainService theMainService = MainService.builder()
                .title("sdsd")
                .description("")
                .build();
        assertThrows(InvalidInputException.class, () -> mainServiceService.saveOrUpdate(theMainService));
    }

    @Test
    public void test_main_service_deleteById_isOk() {
        MainService theMainService = MainService.builder()
                .title("حمل اثاثیه")
                .description(" تمامی وسایل")
                .build();
        MainService result = mainServiceService.saveOrUpdate(theMainService);
        mainServiceService.deleteById(1l);
    }

    @Test
    public void test_find_main_service_with_sub_service() {
        MainService theMainService = mainServiceService.findById(2L);
        System.out.println(theMainService);
        //create sub service and store
        MainService subService = MainService.builder()
                .title("خشکشویی و اتوشویی")
                .description("سایر")
                .mainService(theMainService)
                .build();
        mainServiceService.saveOrUpdate(subService);
        //get set of sub service for main service
        Set<MainService> subServices = theMainService.getSubServices();
        subServices.forEach(System.out::println);
        assertTrue(subServices.size() > 0);
    }

    @Test
    public void test_print_hierarchical_main_service() {
        List<MainService> mainServiceList = mainServiceService.findAll();
        for (MainService mainService : mainServiceList) {
            if (mainService.getMainService() == null) {
                System.out.println(mainService.getTitle());
                Set<MainService> subServices = mainService.getSubServices();
                for (MainService subService : subServices) {
                    System.out.println("--" + subService.getTitle());
                }
            }
        }
    }

    @Test
    public void test_find_all_pageable() {
        Sort sort = Sort.by("title").ascending();
        Pageable pageable = PageRequest.of(1, 2, sort);
        Page<MainService> page = mainServiceService.findAll(pageable);
        List<MainService> mainServiceList = page.getContent();
        assertTrue(mainServiceList.size() > 0);
    }

    @Test
    public void test_find_root_main_service() {
        List<MainService> result = mainServiceService.findRootMainServices();
        assertNotNull(result);
    }

}