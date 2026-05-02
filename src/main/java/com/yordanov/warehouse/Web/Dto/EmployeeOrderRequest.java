package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Schema(description = "Request payload for manually created employee order")
public record EmployeeOrderRequest(
        @Schema(description = "Order line items")
        @NotEmpty(message = "Items cannot be empty")
        List<OrderItemRequest> items,

        @Schema(description = "Warehouse identifier used for all items when item-level warehouse is not provided", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "Warehouse ID is required")
        UUID warehouseId,

        @Schema(description = "Customer identifier related to this manual order", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID customerId,

        @Schema(description = "External or human-readable order reference", example = "MAN-2026-0001")
        @NotBlank(message = "Order reference is required for manual orders")
        String externalReference
) {}
