package com.app.inventory.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.inventory.models.ItemModel;
import com.app.inventory.repositories.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public ItemModel save(ItemModel item) {
        return itemRepository.save(item);
    }

    public ItemModel findByItemId(String itemId) {
        return itemRepository.findByItemId(itemId).orElse(null);
    }

    public ItemModel findByName(String name) {
        return itemRepository.findByName(name).orElse(null);
    }

    public ItemModel softDelete(ItemModel item) {
        item.setIsDeleted(true);
        return itemRepository.save(item);
    }

    public void deleteById(String itemId) {
        itemRepository.deleteById(itemId);
    }

    public Page<ItemModel> findAllItemByPage(Pageable pageable) {
        return itemRepository.findAllItemsPage(pageable);
    }

    public List<ItemModel> findAllActiveItems() {
        List<ItemModel> activeItems = itemRepository.findAllActiveItems();
        return activeItems.isEmpty() ? Collections.emptyList() : activeItems;
    }

    public Page<ItemModel> searchItemsByName(String name, Pageable pageable) {
        return itemRepository.searchByName(name, pageable);
    }
}
