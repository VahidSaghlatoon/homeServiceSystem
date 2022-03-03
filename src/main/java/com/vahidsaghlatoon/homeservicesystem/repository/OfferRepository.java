package com.vahidsaghlatoon.homeservicesystem.repository;

import com.vahidsaghlatoon.homeservicesystem.model.Offer;
import com.vahidsaghlatoon.homeservicesystem.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByOrder(Order order);

    List<Offer> findAllByOrder(Order order, Pageable pageable);
}
