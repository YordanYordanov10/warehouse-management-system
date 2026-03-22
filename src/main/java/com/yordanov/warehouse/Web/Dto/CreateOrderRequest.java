package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Order.Model.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderRequest {

    @NotEmpty
    private List<OrderItemRequest> items;
}
