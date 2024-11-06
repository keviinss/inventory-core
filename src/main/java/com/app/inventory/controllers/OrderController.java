package com.app.inventory.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.inventory.helpers.ResponseJson;
import com.app.inventory.models.OrderModel;
import com.app.inventory.models.ItemModel;
import com.app.inventory.request.OrderItemPayload;
import com.app.inventory.request.OrderPayload;
import com.app.inventory.services.OrderService;
import com.app.inventory.services.ItemService;

@RestController
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService inventoryService;

    @PostMapping("/order")
    public ResponseEntity<Object> create(@Valid @RequestBody OrderPayload payload, Errors errors) {
        ResponseJson<Object> response = new ResponseJson<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus_code(status.value());

        // Handle validation errors
        if (errors.hasErrors()) {
            List<String> messages = errors.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            response.setMessages(String.join(", ", messages));
            return ResponseEntity.status(status).body(response);
        }

        List<OrderModel> savedOrders = new ArrayList<>();

        try {
            // Process each item in the payload
            for (OrderItemPayload itemPayload : payload.getOrders()) {
                ItemModel item = itemService.findByItemId(itemPayload.getItemId());

                if (item == null) {
                    response.setMessages("Item not found for ID: " + itemPayload.getItemId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                // Create new inventory record
                OrderModel order = new OrderModel();
                order.setItemId(itemPayload.getItemId());
                order.setQuantity(itemPayload.getQuantity());

                // Withdrawal: decrease stock in Items
                if (item.getStock() >= itemPayload.getQuantity()) {
                    item.setStock(item.getStock() - itemPayload.getQuantity());
                } else {
                    response.setMessages("Insufficient stock for item ID: " + itemPayload.getItemId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                // Save updated item stock and new inventory record
                itemService.save(item);
                savedOrders.add(inventoryService.save(order));
            }
        } catch (DataIntegrityViolationException e) {
            // Handle constraint violation, such as quantity > 0 check
            response.setMessages("Failed to create order: Quantity must be greater than zero");
            return ResponseEntity.status(status).body(response);
        }

        // Set successful response data
        status = HttpStatus.OK;
        response.setStatus_code(status.value());
        response.setData(savedOrders);

        return ResponseEntity.status(status).body(response);
    }

}
