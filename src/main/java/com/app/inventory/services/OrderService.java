package com.app.inventory.services;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.inventory.models.OrderModel;
import com.app.inventory.repositories.OrderRepository;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderModel save(OrderModel order) {
        return orderRepository.save(order);
    }

    public OrderModel findByItemId(String orderId) {
        return orderRepository.findByItemId(orderId).orElse(null);
    }

    public OrderModel findByOrderId(String inventoryId) {
        return orderRepository.findByOrderId(inventoryId).orElse(null);
    }

    public OrderModel softDelete(OrderModel order) {
        order.setIsDeleted(true);
        return orderRepository.save(order);
    }

    public void deleteById(String orderId) {
        orderRepository.deleteById(orderId);
    }

    public Page<OrderModel> findAllItemByPage(Pageable pageable) {
        return orderRepository.findAllOrderPage(pageable);
    }

    public List<OrderModel> findAllActiveOrder() {
        List<OrderModel> activeOrder = orderRepository.findAllActiveOrder();
        return activeOrder.isEmpty() ? Collections.emptyList() : activeOrder;
    }

}
