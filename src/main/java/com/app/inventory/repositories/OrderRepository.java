package com.app.inventory.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.inventory.models.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, String> {

    @Query("SELECT a FROM OrderModel a WHERE a.isDeleted = false")
    List<OrderModel> findAllActiveOrder();

    @Query("SELECT a FROM OrderModel a WHERE a.isDeleted = false")
    Page<OrderModel> findAllOrderPage(Pageable pageable);

    @Query("SELECT u FROM OrderModel u WHERE u.isDeleted = false AND u.orderId = :orderId")
    Optional<OrderModel> findByOrderId(@Param("orderId") String orderId);

    @Query("SELECT u FROM InventoryModel u WHERE u.isDeleted = false AND u.itemId = :itemId")
    Optional<OrderModel> findByItemId(@Param("itemId") String itemId);

}
