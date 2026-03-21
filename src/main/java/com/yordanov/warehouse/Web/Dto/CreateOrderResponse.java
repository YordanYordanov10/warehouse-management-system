package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Order.Model.OrderStatus;
import com.yordanov.warehouse.OrderItem.Model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {

    private UUID orderId;

    private String orderReference;

    private OrderStatus status;

    private List<OrderItemResponse> items;

}
