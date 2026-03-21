package com.yordanov.warehouse.Web.Mapper;

import com.yordanov.warehouse.InventoryMovement.Model.InventoryMovement;
import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Web.Dto.*;
import lombok.experimental.UtilityClass;
import com.yordanov.warehouse.Product.Model.Product;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DtoMapper {

    public static ProductResponse toProductResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .sku(product.getSku())
                .description(product.getDescription())
                .build();
    }

    public static List<ProductResponse> toProductResponseList(List<Product> products) {
        return products.stream().map(DtoMapper::toProductResponse).toList();
    }

    public static WarehouseResponse toWarehouseResponse(Warehouse warehouse) {

        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .warehouseCode(warehouse.getWarehouseCode())
                .address(warehouse.getAddress())
                .city(warehouse.getCity())
                .country(warehouse.getCountry())
                .postalCode(warehouse.getPostalCode())
                .city(warehouse.getCity())
                .maxPalletCapacity(warehouse.getMaxPalletCapacity())
                .warehouseStatus(warehouse.getWarehouseStatus())
                .updatedAt(warehouse.getUpdatedAt())
                .createdAt(warehouse.getCreatedAt())
                .build();
    }

    public static List<WarehouseResponse> toWarehouseResponseList(List<Warehouse> warehouses) {
        return warehouses.stream().map(DtoMapper::toWarehouseResponse).toList();
    }

    public static CreateOrderResponse toCreateOrderResponse(Order order){

//        TODO
//        List<OrderItemResponse> itemResponses = orderItems.stream()
//                .map(item -> OrderItemResponse.builder()
//                        .productId(item.getProductId())
//                        .orderedQuantity(item.getOrderedQuantity())
//                        .build())
//                .collect(Collectors.toList());

        return CreateOrderResponse.builder()
                .orderId(order.getId())
                .orderReference(order.getOrderReference())
                .status(order.getOrderStatus())
//                .items(itemResponses)
                .build();
    }

}
