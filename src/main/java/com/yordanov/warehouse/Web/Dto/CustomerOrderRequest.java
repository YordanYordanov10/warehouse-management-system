package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

@Schema(description = "Request payload for customer online order creation")
public record CustomerOrderRequest(

        @Schema(description = "Order line items")
        @NotEmpty(message = "Items cannot be empty")
        List<OrderItemRequest> items,

        @Schema(description = "Preferred warehouse identifier for fulfillment", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID preferredWarehouseId
) {
}
