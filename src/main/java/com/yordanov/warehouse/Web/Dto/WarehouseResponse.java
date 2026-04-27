package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;
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
@Schema(description = "Response warehouse response")
public class WarehouseResponse {

    @Schema(description = "Unique identifier of the warehouse", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Name of the warehouse", example = "Main Warehouse")
    private String name;

    @Schema(description = "Code of the warehouse", example = "WH001")
    private String warehouseCode;

    @Schema(description = "Address of the warehouse", example = "123 Main St, Anytown, USA")
    private String address;

    @Schema(description = "City where the warehouse is located", example = "Anytown")
    private String city;

    @Schema(description = "Postal code of the warehouse location", example = "12345")
    private String postalCode;

    @Schema(description = "Country where the warehouse is located", example = "USA")
    private String country;

    @Schema(description = "Max pallet capacity of the warehouse", example = "1000")
    private int maxPalletCapacity;

    @Schema(description = "Current status of the warehouse", example = "ACTIVE")
    private WarehouseStatus warehouseStatus;

    @Schema(description = "Timestamp when the warehouse was created", example = "2024-06-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the warehouse was last updated", example = "2024-06-10T15:30:00")
    private LocalDateTime updatedAt;
}
