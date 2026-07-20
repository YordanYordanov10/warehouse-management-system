package com.yordanov.warehouse.Web.Dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "Request payload for creating a basic order")
@Data
public class PlaceCustomerOrderRequest {

    
    @Schema(description = "List of products and quantities to order")
    @NotEmpty(message = "Items cannot be empty")
    @Valid
    private List<OrderItemRequest> items;


}
