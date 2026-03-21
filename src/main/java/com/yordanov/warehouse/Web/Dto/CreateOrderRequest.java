package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Order.Model.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderRequest {

    @NotNull
    private UUID productId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

}
