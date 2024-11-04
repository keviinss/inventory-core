package com.app.inventory.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.inventory.models.InventoryModel;
import com.app.inventory.services.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    // @PostMapping
    // public InventoryModel saveInventory(@Valid @RequestBody InventoryModel
    // inventory) {
    // return inventoryService.addInventory(inventory);
    // }

}
