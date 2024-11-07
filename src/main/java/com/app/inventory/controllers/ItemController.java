package com.app.inventory.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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
import com.app.inventory.models.ItemModel;
import com.app.inventory.request.ItemPayload;
import com.app.inventory.services.ItemService;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity<ResponseJson<Object>> update(@Valid @RequestBody ItemPayload payload, Errors errors) {
        ResponseJson<Object> response = new ResponseJson<>();

        // Handle validation errors
        if (errors.hasErrors()) {
            String errorMessages = errors.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            response.setMessages(errorMessages);
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        // Check if stock is positive
        if (payload.getStock() <= 0) {
            response.setMessages("Stock must be greater than zero");
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        // Additional validation for price
        if (payload.getPrice() <= 0) {
            response.setMessages("Price must be greater than zero");
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        ItemModel newItem = new ItemModel();

        try {
            // Check if item exists by name
            ItemModel existingItem = itemService.findByName(payload.getName());
            if (existingItem != null) {
                response.setMessages("Name already exists");
                response.setStatus_code(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(response);
            }

            // Create and save a new item
            newItem.setName(payload.getName());
            newItem.setPrice(payload.getPrice());
            newItem.setStock(payload.getStock());

        } catch (DataIntegrityViolationException e) {
            response.setMessages("Failed to create item: Stock must be greater than zero");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Set success status
        response.setData(itemService.save(newItem));
        response.setStatus_code(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ResponseJson<Object>> updateItem(
            @PathVariable("id") String itemId,
            @Valid @RequestBody ItemPayload payload,
            Errors errors) {

        ResponseJson<Object> response = new ResponseJson<>();

        // Handle validation errors
        if (errors.hasErrors()) {
            String errorMessages = errors.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            response.setMessages(errorMessages);
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        // Check if stock is positive
        if (payload.getStock() <= 0) {
            response.setMessages("Stock must be greater than zero");
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        // Additional validation for price
        if (payload.getPrice() <= 0) {
            response.setMessages("Price must be greater than zero");
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(response);
        }

        ItemModel item = itemService.findByItemId(itemId);

        try {
            // Retrieve item by ID
            if (item == null) {
                response.setMessages("Data does not exist");
                response.setStatus_code(HttpStatus.NOT_FOUND.value());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Check if the name already exists for a different item
            ItemModel existingName = itemService.findByName(payload.getName());
            if (existingName != null && !existingName.getItemId().equals(itemId)) {
                response.setMessages("Name already exists");
                response.setStatus_code(HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.badRequest().body(response);
            }

            // Update item fields and save
            item.setName(payload.getName());
            item.setPrice(payload.getPrice());
            item.setStock(payload.getStock());

        } catch (DataIntegrityViolationException e) {
            response.setMessages("Failed to create item: Stock must be greater than zero");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.setData(itemService.save(item));
        response.setStatus_code(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/items")
    public ResponseEntity<ResponseJson<List<ItemModel>>> getAll() {

        // Get all item from database
        List<ItemModel> data = itemService.findAllActiveItems();

        ResponseJson<List<ItemModel>> response = new ResponseJson<>();

        // Set success status
        response.setData(data);
        response.setStatus_code(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable String itemId) {

        ResponseJson<Object> response = new ResponseJson<>();

        // Check if the Item ID cannot be empty
        if (itemId == null || itemId.isEmpty()) {
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            response.setMessages("Item ID is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if Item ID does not exist in the database
        ItemModel data = itemService.findByItemId(itemId);
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

    @GetMapping("/items/paging")
    public ResponseEntity<ResponseJson<Page<ItemModel>>> getAllItems(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // Create a Pageable object
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemModel> pagedItems = itemService.findAllItemByPage(pageable);

        // Prepare the response JSON
        ResponseJson<Page<ItemModel>> response = new ResponseJson<>();
        response.setData(pagedItems);
        response.setStatus_code(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("items/search")
    public ResponseEntity<ResponseJson<Page<ItemModel>>> searchItems(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemModel> searchResults = itemService.searchItemsByName(query, pageable);

        ResponseJson<Page<ItemModel>> response = new ResponseJson<>();
        response.setData(searchResults);
        response.setStatus_code(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/soft-delete/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable String itemId) {
        ResponseJson<Object> response = new ResponseJson<>();

        // Check if the Item ID cannot be empty
        if (itemId == null || itemId.isEmpty()) {
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            response.setMessages("Item ID is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if Item ID does not exist in the database
        ItemModel item = itemService.findByItemId(itemId);
        if (item != null) {
            item.setIsDeleted(true);
            itemService.save(item);

            // Update item fields and save
            response.setStatus_code(HttpStatus.OK.value());
            response.setData(item);
            response.setMessages("Data has been deleted");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.setStatus_code(HttpStatus.NOT_FOUND.value());
            response.setMessages("Data does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/items/hard-delete/{itemId}")
    public ResponseEntity<ResponseJson<Object>> hardDeleteItem(@PathVariable String itemId) {
        ResponseJson<Object> response = new ResponseJson<>();

        // Check if the Item ID cannot be empty
        if (itemId == null || itemId.isEmpty()) {
            response.setStatus_code(HttpStatus.BAD_REQUEST.value());
            response.setMessages("Item ID is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if Item ID exists in the database
        ItemModel item = itemService.findByItemId(itemId);
        if (item != null) {
            itemService.deleteById(itemId);

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
