package com.app.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.inventory.models.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {
}
