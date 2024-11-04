package com.app.inventory.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.inventory.models.ItemModel;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, String> {

    @Query("SELECT a FROM ItemModel a WHERE a.isDeleted = false")
    List<ItemModel> findAllActiveItems();

    @Query("SELECT a FROM ItemModel a WHERE a.isDeleted = false")
    Page<ItemModel> findAllItemsPage(Pageable pageable);

    @Query("SELECT u FROM ItemModel u WHERE u.isDeleted = false AND u.itemId = :itemId")
    Optional<ItemModel> findByItemId(@Param("itemId") String itemId);

    @Query("SELECT u FROM ItemModel u WHERE u.isDeleted = false AND LOWER(u.name) = LOWER(:name)")
    Optional<ItemModel> findByName(@Param("name") String name);

    @Query("SELECT s FROM ItemModel s WHERE s.isDeleted = false AND LOWER(s.name) LIKE LOWER(CONCAT('%', :textSearch, '%'))")
    Page<ItemModel> searchByName(@Param("textSearch") String textSearch, Pageable pageable);
}
