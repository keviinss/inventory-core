package com.app.inventory.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.inventory.models.OrderModel;
import com.app.inventory.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // @PostMapping
    // public OrderModel saveOrder(@Valid @RequestBody OrderModel order) {
    // return orderService.createOrder(order);
    // }

}
