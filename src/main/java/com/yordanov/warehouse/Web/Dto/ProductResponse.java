package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response product response")
public class ProductResponse {

    @Schema(description = "Unique identifier of the product", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Name of the product", example = "Laptop")
    private String name;

    @Schema(description = "SKU of the product", example = "LAP12345")
    private String sku;

    @Schema(description = "Description of the product", example = "A high-performance laptop for gaming and work")
    private String description;

    @Schema(description = "Price of the product", example = "999.99")
    private BigDecimal price;


}
