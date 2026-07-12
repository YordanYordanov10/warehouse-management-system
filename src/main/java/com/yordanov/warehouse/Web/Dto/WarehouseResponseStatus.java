package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "New status of the warehouse")
public record WarehouseResponseStatus(

        @Schema(description = "Warehouse identifier for this line item", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID warehouseId,

        @Schema(description = "Warehouse status", example = "ACTIVE")
        WarehouseStatus status
) {
}
