package com.app.inventory.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@Table(name = "item", schema = "inventory")
public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
    @GenericGenerator(name = "pg-uuid", strategy = "uuid2")
    @Column(name = "item_id", length = 50)
    private String itemId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", length = 50)
    private String name;

    @Positive(message = "Price must be positive")
    @Column(name = "price")
    private int price;

    @NotNull(message = "Stock is mandatory")
    @Column(name = "stock")
    private int stock;

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

    public ItemModel(String itemId, @NotBlank(message = "Name is mandatory") String name,
            @Positive(message = "Price must be positive") int price, @NotNull(message = "Stock is mandatory") int stock,
            Boolean isDeleted, Date createdAt, Date updatedAt) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
