package com.app.inventory.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

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
    private String inventory_id;

    @NotBlank(message = "Type is mandatory")
    @Column(name = "item_id", nullable = false)
    private String item_id;

    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", length = 50)
    private int quantity;

    @NotBlank(message = "Type is mandatory")
    @Column(name = "type", length = 1)
    private String type; // 'T' for top-up, 'W' for withdrawal

    @JsonIgnore
    @Column(name = "is_deleted")
    private Boolean is_deleted = false;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+07")
    private Date created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+07")
    private Date updated_at;

    public InventoryModel(String inventory_id, @NotBlank(message = "Type is mandatory") String item_id,
            @Positive(message = "Quantity must be positive") int quantity,
            @NotBlank(message = "Type is mandatory") String type, Boolean is_deleted, Date created_at,
            Date updated_at) {
        this.inventory_id = inventory_id;
        this.item_id = item_id;
        this.quantity = quantity;
        this.type = type;
        this.is_deleted = is_deleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

}
