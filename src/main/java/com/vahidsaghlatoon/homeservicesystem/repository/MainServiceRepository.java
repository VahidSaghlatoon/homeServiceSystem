package com.vahidsaghlatoon.homeservicesystem.repository;

import com.vahidsaghlatoon.homeservicesystem.model.MainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MainServiceRepository extends JpaRepository<MainService, Long> {
    List<MainService> findAllByOrderByTitleAsc();

    MainService findByTitle(String title);

    @Query("select m from MainService m where m.mainService.id is null ")
    List<MainService> findRootMainServices();

    @Query("select m from MainService m where CONCAT(m.title, ' ', m.description ) like %?1%")
    Page<MainService> findAllByKeyword(String keyword, Pageable pageable);

    @Query("update MainService m set m.enabled=?2 where m.id = ?1")
    @Modifying()
    void updateEnableStatus(Long id, boolean enabled);
}
