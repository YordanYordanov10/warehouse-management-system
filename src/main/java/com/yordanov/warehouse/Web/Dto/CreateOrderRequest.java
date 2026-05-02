package com.yordanov.warehouse.Web.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Schema(description = "Request payload for creating a basic order")
@Data
public class CreateOrderRequest {

    @Schema(description = "List of products and quantities to order")
    @NotEmpty(message = "Items cannot be empty")
    private List<OrderItemRequest> items;
}
