package com.app.inventory.models;

import java.io.Serializable;
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
@Table(name = "order", schema = "inventory")
public class OrderModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
    @GenericGenerator(name = "pg-uuid", strategy = "uuid2")
    @Column(name = "order_id", length = 50, updatable = false, nullable = false)
    private String orderId;

    @NotBlank(message = "Item ID is mandatory")
    @Column(name = "item_id", nullable = false, length = 50)
    private String itemId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @JsonIgnore
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+07")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+07")
    private Date updatedAt;

    public OrderModel(String orderId, @NotBlank(message = "Item ID is mandatory") String itemId, int quantity,
            Boolean isDeleted, Date createdAt, Date updatedAt) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
