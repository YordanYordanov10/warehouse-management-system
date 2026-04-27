package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response ship stock response")
public class ShipStockResponse {

    @Schema(description = "Unique identifier of the stock movement", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID movementId;

    @Schema(description = "Unique identifier of the product that was shipped", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID productId;

    @Schema(description = "Unique identifier of the warehouse from which the stock was shipped", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID warehouseId;

    @Schema(description = "Quantity of stock that was shipped", example = "10")
    private int shipQuantity;

    @Schema(description = "Reference for the stock movement, such as an order number or shipment ID", example = "ORDER12345")
    private String reference;

    @Schema(description = "Shipping date and time of the stock movement", example = "2024-06-15T14:30:00")
    private LocalDateTime shipDate;
}
