package com.app.inventory.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.inventory.models.InventoryModel;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryModel, String> {

    @Query("SELECT a FROM InventoryModel a WHERE a.isDeleted = false")
    List<InventoryModel> findAllActiveItems();

    @Query("SELECT a FROM InventoryModel a WHERE a.isDeleted = false")
    Page<InventoryModel> findAllItemsPage(Pageable pageable);

    @Query("SELECT u FROM InventoryModel u WHERE u.isDeleted = false AND u.itemId = :itemId")
    Optional<InventoryModel> findByItemId(@Param("itemId") String itemId);

}
