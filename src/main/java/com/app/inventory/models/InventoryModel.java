package com.app.inventory.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "inventory", schema = "inventory")
public class InventoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
    @GenericGenerator(name = "pg-uuid", strategy = "uuid2")
    @Column(name = "inventory_id", length = 50)
    private String inventoryId;

    @NotBlank(message = "Type is mandatory")
    @Column(name = "item_id", length = 50)
    private String itemId;

    @Column(name = "quantity", length = 50)
    private int quantity;

    @NotBlank(message = "Type is mandatory")
    @Column(name = "type", length = 1)
    private String type; // 'T' for top-up, 'W' for withdrawal

    @JsonIgnore
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+07")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+07")
    private Date updatedAt;

    public InventoryModel(String inventoryId, @NotBlank(message = "Type is mandatory") String itemId, int quantity,
            @NotBlank(message = "Type is mandatory") String type, Boolean isDeleted, Date createdAt, Date updatedAt) {
        this.inventoryId = inventoryId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.type = type;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
