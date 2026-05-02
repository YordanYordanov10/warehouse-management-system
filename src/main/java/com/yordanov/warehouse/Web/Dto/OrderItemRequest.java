package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


import java.util.UUID;
@Schema(description = "Single line item in an order request")
public record OrderItemRequest(
        @Schema(description = "Product identifier", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "Product ID is required")
        UUID productId,

        @Schema(description = "Ordered quantity", example = "5")
        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @Schema(description = "Warehouse identifier for this line item", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "Warehouse ID is required")
        UUID warehouseId
) {}
