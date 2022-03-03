package com.vahidsaghlatoon.homeservicesystem.repository;

import com.vahidsaghlatoon.homeservicesystem.model.SubService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubServiceRepository extends JpaRepository<SubService, Long> {
    SubService findByTitle(String title);
}
