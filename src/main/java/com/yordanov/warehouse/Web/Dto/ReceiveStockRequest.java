package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Schema(description = "Request receive stock request")
@Data
public class ReceiveStockRequest {

    @Schema(description = "Unique identifier of the product to be received", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private UUID productId;

    @Schema(description = "Unique identifier of the warehouse where the stock will be received", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private UUID warehouseId;

    @Schema(description = "Quantity of stock to be received", example = "10")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

}
