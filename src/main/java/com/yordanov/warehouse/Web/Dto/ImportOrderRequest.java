package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Schema(description = "Request payload for imported order creation")
public record ImportOrderRequest(
        @Schema(description = "Order line items")
        @NotEmpty(message = "Items cannot be empty")
        List<OrderItemRequest> items,

        @Schema(description = "Warehouse identifier used for imported order fulfillment", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "Warehouse ID is required")
        UUID warehouseId,

        @Schema(description = "Customer identifier from source system", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @Schema(description = "External order id from integration source", example = "ERP-ORD-7842")
        @NotBlank(message = "External order ID is required")
        String externalOrderId
) {}
