package com.vahidsaghlatoon.homeservicesystem.repository;

import com.vahidsaghlatoon.homeservicesystem.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);

    @Query("select e from Employee e where CONCAT(e.firstName, ' ', e.lastName , ' ', e.email) like %?1%")
    Page<Employee> findAllByKeyword(String keyword, Pageable pageable);

    @Query("update Employee e set e.enabled=?2 where e.id = ?1")
    @Modifying()
    void updateEnableStatus(Long id, boolean enabled);


}
