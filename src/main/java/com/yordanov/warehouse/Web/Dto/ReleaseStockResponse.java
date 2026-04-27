package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
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
@Schema(description = "Response release stock response")
public class ReleaseStockResponse {

    @Schema(description = "Unique identifier of the stock movement", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID movementId;

    @Schema(description = "Unique identifier of the warehouse from which the stock was released", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID warehouseId;

    @Schema(description = "Unique identifier of the product that was released", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID productId;

    @Schema(description = "Quantity of stock that was released", example = "10")
    private int releaseQuantity;

    @Schema(description = "Reference for the stock movement, such as an order number or shipment ID", example = "ORDER12345")
    private String reference;

    @Schema(description = "Type of reference for the stock movement, such as ORDER or SHIPMENT", example = "ORDER")
    private ReferenceType referenceType;

    @Schema(description = "Release date and time of the stock movement", example = "2024-06-15T14:30:00")
    private LocalDateTime releaseAt;
}
