package com.yordanov.warehouse.Web.Dto;

import com.yordanov.warehouse.Order.Model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Schema(description = "Response payload returned after order creation")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {

    @Schema(description = "Generated order identifier", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID orderId;

    @Schema(description = "Generated or external order reference", example = "ORD-2026-000123")
    private String orderReference;

    @Schema(description = "Current order status", example = "CREATED")
    private OrderStatus status;

    @Schema(description = "Created order line items")
    private List<OrderItemResponse> items;

}
