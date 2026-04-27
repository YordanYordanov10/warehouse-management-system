package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
@Schema(description = "Request product request")
@Data
public class ProductRequest {

    @Schema(description = "Name of the product", example = "Laptop")
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be 1-255 characters")
    private String name;

    @Schema(description = "SKU of the product", example = "LAP12345")
    @NotBlank(message = "sku is required")
    private String sku;

    @Schema(description = "Description of the product", example = "A high-performance laptop for gaming and work")
    @Size(max = 190, message = "Description must be at most 190 characters")
    private String description;

    @Schema(description = "Price of the product", example = "999.99")
    @Positive(message = "Price must be positive number")
    private BigDecimal price;

}
