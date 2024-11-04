package com.app.inventory.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryItemPayload {

    private String inventoryId;

    @NotBlank(message = "ItemId is mandatory")
    private String itemId;

    @Positive(message = "Quantity must be positive")
    private int quantity;

    @NotBlank(message = "Type is mandatory")
    @Pattern(regexp = "[TW]", message = "Type must be 'T' for top-up or 'W' for withdrawal")
    private String type;

}
