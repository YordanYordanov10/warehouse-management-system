package com.yordanov.warehouse.Web.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be 1-255 characters")
    private String name;

    @NotBlank(message = "sku is required")
    private String sku;

    @Size(max = 190, message = "Description must be at most 190 characters")
    private String description;

    @Positive(message = "Price must be positive number")
    private BigDecimal price;

}
