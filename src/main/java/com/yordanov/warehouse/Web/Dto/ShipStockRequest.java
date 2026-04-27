package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
@Schema(description = "Request ship stock request")
public class ShipStockRequest {

    @Schema(description = "Unique identifier of the product to be shipped", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private UUID productId;

    @Schema(description = "Unique identifier of the warehouse from which the stock will be shipped", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private UUID warehouseId;

    @Schema(description = "Quantity of stock to be shipped", example = "10")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

    @Schema(description = "Reference for the stock movement, such as an order number or shipment ID", example = "ORDER12345")
    @NotBlank
    private String reference;

    @Schema(description = "Type of reference for the stock movement, such as ORDER or SHIPMENT", example = "ORDER")
    @NotNull
    private ReferenceType referenceType;
}
