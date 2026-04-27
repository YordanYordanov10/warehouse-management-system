package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Schema(description = "Request warehouse request")
@Data
public class WarehouseRequest {

    @Schema(description = "Name of the warehouse", example = "Main Warehouse")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Code of the warehouse", example = "WH001")
    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @Schema(description = "Address of the warehouse", example = "123 Main St, Anytown, USA")
    @NotBlank
    @Size(max = 200)
    private String address;

    @Schema(description = "City where the warehouse is located", example = "Anytown")
    @NotBlank
    @Size(max = 100)
    private String city;

    @Schema(description = "Postal code of the warehouse location", example = "12345")
    @NotBlank
    private String postalCode;

    @Schema(description = "Country where the warehouse is located", example = "USA")
    @NotBlank
    private String country;

    @Schema(description = "Max pallet capacity of the warehouse", example = "1000")
    @Positive(message = "Max pallet capacity must be positive")
    private int maxPalletCapacity;


}
