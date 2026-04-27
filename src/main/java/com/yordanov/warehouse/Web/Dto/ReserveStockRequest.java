package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
@Schema(description = "Request reserve stock request")
public class ReserveStockRequest {

    @NotNull
    @Schema(description = "Unique identifier of the product to be reserved", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID productId;

    @NotNull
    @Schema(description = "Unique identifier of the warehouse from which the stock will be reserved", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID warehouseId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    @Schema(description = "Quantity of stock to be reserved", example = "10")
    private int quantity;

}
