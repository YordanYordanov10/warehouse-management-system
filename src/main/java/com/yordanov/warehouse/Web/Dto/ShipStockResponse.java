package com.yordanov.warehouse.Web.Dto;

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
public class ShipStockResponse {

    private UUID movementId;

    private UUID productId;

    private UUID warehouseId;

    private int shipQuantity;

    private String reference;

    private LocalDateTime shipDate;
}
