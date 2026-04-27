package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Schema(description = "Request update warehouse request")
@Data
public class UpdateWarehouseRequest {

    @Schema(description = "Status of the warehouse", example = "ACTIVE")
    @NotNull
    private WarehouseStatus status;

}
