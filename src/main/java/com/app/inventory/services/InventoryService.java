package com.app.inventory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.inventory.models.InventoryModel;
import com.app.inventory.models.ItemModel;
import com.app.inventory.repositories.InventoryRepository;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemService itemService;

    // public InventoryModel addInventory(InventoryModel inventory) {
    // ItemModel item = inventory.getItem();
    // if (inventory.getType().equals("T")) {
    // item.setStock(inventory.getQuantity() + item.getStock());
    // } else if (inventory.getType().equals("W")) {
    // item.setStock(item.getStock() - inventory.getQuantity());
    // }
    // itemService.updateItemStock(item.getId(), item.getStock());

    // return inventoryRepository.save(inventory);
    // }

}
