package com.yordanov.warehouse.Web.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ReceiveStockRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private UUID warehouseId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

}
