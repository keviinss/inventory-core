package com.app.inventory.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.app.inventory.models.InventoryModel;
import com.app.inventory.models.ItemModel;
import com.app.inventory.request.InventoryItemPayload;
import com.app.inventory.request.InventoryPayload;
import com.app.inventory.services.InventoryService;
import com.app.inventory.services.ItemService;

@RestController
public class InventoryController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/inventory")
    public ResponseEntity<Object> create(@Valid @RequestBody InventoryPayload payload, Errors errors) {
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

        List<InventoryModel> savedItems = new ArrayList<>();

        // Process each item in the payload
        for (InventoryItemPayload itemPayload : payload.getItems()) {
            ItemModel item = itemService.findByItemId(itemPayload.getItemId());

            if (item == null) {
                response.setMessages("Item not found for ID: " + itemPayload.getItemId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Create new inventory record
            InventoryModel inventory = new InventoryModel();
            inventory.setItemId(itemPayload.getItemId());
            inventory.setQuantity(itemPayload.getQuantity());
            inventory.setType(itemPayload.getType());

            // Update stock based on transaction type
            switch (itemPayload.getType()) {
                case "T" -> // Top-up: increase stock
                    item.setStock(item.getStock() + itemPayload.getQuantity());
                case "W" -> {
                    // Withdrawal: decrease stock
                    if (item.getStock() >= itemPayload.getQuantity()) {
                        item.setStock(item.getStock() - itemPayload.getQuantity());
                    } else {
                        response.setMessages("Insufficient stock for item ID: " + itemPayload.getItemId());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                }
                default -> {
                    response.setMessages("Invalid operation type for item ID: " + itemPayload.getItemId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            }

            // Save updated item stock and new inventory record
            itemService.save(item);
            savedItems.add(inventoryService.save(inventory));
        }

        // Set successful response data
        status = HttpStatus.OK;
        response.setStatus_code(status.value());
        response.setData(savedItems);

        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/inventory")
    public ResponseEntity<Object> updateInventory(@Valid @RequestBody InventoryPayload payload, Errors errors) {
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

        List<InventoryModel> updatedItems = new ArrayList<>();

        // Process each item in the payload
        for (InventoryItemPayload itemPayload : payload.getItems()) {
            // Check if the inventory record exists for the given inventoryId
            InventoryModel inventory = inventoryService.findByInventoryId(itemPayload.getInventoryId());

            if (inventory == null) {
                response.setMessages("Inventory record not found for ID: " + itemPayload.getInventoryId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Check if item exists in the item table
            ItemModel item = itemService.findByItemId(itemPayload.getItemId());

            if (item == null) {
                response.setMessages("Item not found for item ID: " + itemPayload.getItemId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Update stock based on transaction type
            switch (itemPayload.getType()) {
                case "T" -> // Top-up: increase stock
                    item.setStock(item.getStock() + itemPayload.getQuantity());
                case "W" -> {
                    // Withdrawal: decrease stock
                    if (item.getStock() >= itemPayload.getQuantity()) {
                        item.setStock(item.getStock() - itemPayload.getQuantity());
                    } else {
                        response.setMessages("Insufficient stock for item ID: " + itemPayload.getItemId());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                }
                default -> {
                    response.setMessages("Invalid operation type for item ID: " + itemPayload.getItemId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            }

            // Update the quantity in inventory
            inventory.setQuantity(itemPayload.getQuantity());

            // Save updated item stock and inventory record
            itemService.save(item);
            updatedItems.add(inventoryService.save(inventory));
        }

        // Set successful response data
        status = HttpStatus.OK;
        response.setStatus_code(status.value());
        response.setData(updatedItems);

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/inventory")
    public ResponseEntity<ResponseJson<List<InventoryModel>>> getAll() {

        // Get all item from database
        List<InventoryModel> data = inventoryService.findAllActiveItems();

        ResponseJson<List<InventoryModel>> response = new ResponseJson<>();

        // Set success status
        response.setData(data);
        response.setStatus_code(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<Object> getInventoryById(@PathVariable String inventoryId) {

        ResponseJson<Object> response = new ResponseJson<>();

        // Check if the Item ID cannot be empty
        if (inventoryId == null || inventoryId.isEmpty()) {
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            response.setMessages("Inventory ID is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if Item ID does not exist in the database
        InventoryModel data = inventoryService.findByInventoryId(inventoryId);
        if (data != null) {
            response.setStatus_code(HttpStatus.OK.value());
            response.setData(data);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.setStatus_code(HttpStatus.NOT_FOUND.value());
            response.setMessages("Data does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/inventory/paging")
    public ResponseEntity<ResponseJson<Page<InventoryModel>>> getAllInvetory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // Create a Pageable object
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryModel> pagedItems = inventoryService.findAllItemByPage(pageable);

        // Prepare the response JSON
        ResponseJson<Page<InventoryModel>> response = new ResponseJson<>();
        response.setData(pagedItems);
        response.setStatus_code(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/inventory/soft-delete/{inventoryId}")
    public ResponseEntity<Object> deleteItem(@PathVariable String inventoryId) {
        ResponseJson<Object> response = new ResponseJson<>();

        // Check if the Inventory ID cannot be empty
        if (inventoryId == null || inventoryId.isEmpty()) {
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            response.setMessages("Inventory ID is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if Inventory ID does not exist in the database
        InventoryModel inventory = inventoryService.findByInventoryId(inventoryId);
        if (inventory != null) {
            inventory.setIsDeleted(true);
            inventoryService.save(inventory);

            // Update Inventory fields and save
            response.setStatus_code(HttpStatus.OK.value());
            response.setData(inventory);
            response.setMessages("Data has been deleted");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.setStatus_code(HttpStatus.NOT_FOUND.value());
            response.setMessages("Data does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/inventory/hard-delete/{inventoryId}")
    public ResponseEntity<ResponseJson<Object>> hardDeleteInventory(@PathVariable String inventoryId) {
        ResponseJson<Object> response = new ResponseJson<>();

        // Check if the Inventory ID cannot be empty
        if (inventoryId == null || inventoryId.isEmpty()) {
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            response.setMessages("Inventory ID is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if Inventory ID exists in the database
        InventoryModel inventory = inventoryService.findByInventoryId(inventoryId);
        if (inventory != null) {
            inventoryService.deleteById(inventoryId);

            response.setStatus_code(HttpStatus.OK.value());
            response.setMessages("Data has been permanently deleted");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.setStatus_code(HttpStatus.NOT_FOUND.value());
            response.setMessages("Data does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
