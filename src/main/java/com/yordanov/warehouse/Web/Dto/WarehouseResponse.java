package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;
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
public class WarehouseResponse {


    private UUID id;

    private String name;

    private String warehouseCode;

    private String address;

    private String city;

    private String postalCode;

    private String country;

    private int maxPalletCapacity;

    private WarehouseStatus warehouseStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
