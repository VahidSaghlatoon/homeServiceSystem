package com.vahidsaghlatoon.homeservicesystem.repository;

import com.vahidsaghlatoon.homeservicesystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
