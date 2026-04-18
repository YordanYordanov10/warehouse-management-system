package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateWarehouseRequest {

    @NotNull
    private WarehouseStatus status;

}
