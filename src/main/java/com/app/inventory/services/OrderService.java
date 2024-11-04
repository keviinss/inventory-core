package com.app.inventory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.inventory.exceptions.InsufficientStockException;
import com.app.inventory.models.ItemModel;
import com.app.inventory.models.OrderModel;
import com.app.inventory.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemService itemService;

    // public OrderModel createOrder(OrderModel order) {
    // ItemModel item = order.getItem();
    // if (item.getStock() < order.getQuantity()) {
    // throw new InsufficientStockException("Insufficient stock for item: " +
    // item.getName());
    // }
    // item.setStock(item.getStock() - order.getQuantity());
    // itemService.updateItemStock(item.getId(), item.getStock());
    // order.setPrice(item.getPrice() * order.getQuantity());
    // return orderRepository.save(order);
    // }

}
