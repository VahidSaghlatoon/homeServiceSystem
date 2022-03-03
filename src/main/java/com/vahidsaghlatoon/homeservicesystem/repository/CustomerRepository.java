package com.vahidsaghlatoon.homeservicesystem.repository;

import com.vahidsaghlatoon.homeservicesystem.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
    Customer findByNationalCode(String nationalCode);
    Customer findByVerificationCode(String code);
    @Query("select c from Customer c where CONCAT(c.firstName, ' ', c.lastName , ' ', c.email) like %?1%")
    Page<Customer> findAllByKeyword(String keyword, Pageable pageable);

    @Query("update Customer c set c.enabled=?2 where c.id = ?1")
    @Modifying()
    void updateEnableStatus(Long id, boolean enabled);

    @Query("update Customer c set c.enabled=true , c.verificationCode = null  where c.id = ?1")
    @Modifying()
    void enable(Long id);


}
