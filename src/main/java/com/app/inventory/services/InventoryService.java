package com.app.inventory.services;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.inventory.models.InventoryModel;
import com.app.inventory.repositories.InventoryRepository;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public InventoryModel save(InventoryModel item) {
        return inventoryRepository.save(item);
    }

    public InventoryModel findByItemId(String itemId) {
        return inventoryRepository.findByItemId(itemId).orElse(null);
    }

    public InventoryModel findByInventoryId(String inventoryId) {
        return inventoryRepository.findByInventoryId(inventoryId).orElse(null);
    }

    public InventoryModel softDelete(InventoryModel item) {
        item.setIsDeleted(true);
        return inventoryRepository.save(item);
    }

    public void deleteById(String itemId) {
        inventoryRepository.deleteById(itemId);
    }

    public Page<InventoryModel> findAllItemByPage(Pageable pageable) {
        return inventoryRepository.findAllItemsPage(pageable);
    }

    public List<InventoryModel> findAllActiveItems() {
        List<InventoryModel> activeItems = inventoryRepository.findAllActiveItems();
        return activeItems.isEmpty() ? Collections.emptyList() : activeItems;
    }

    // public Page<InventoryModel> searchItemsByName(String name, Pageable pageable)
    // {
    // return inventoryRepository.searchByName(name, pageable);
    // }
}
