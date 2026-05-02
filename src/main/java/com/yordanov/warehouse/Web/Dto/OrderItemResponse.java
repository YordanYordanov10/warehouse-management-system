package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "Order item details returned in order creation responses")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    @Schema(description = "Product identifier", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID productId;

    @Schema(description = "Quantity requested in the order", example = "5")
    private int orderedQuantity;
}
