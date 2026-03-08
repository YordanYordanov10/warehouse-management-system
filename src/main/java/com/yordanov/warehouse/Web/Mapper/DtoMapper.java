package com.yordanov.warehouse.Web.Mapper;

import com.yordanov.warehouse.InventoryMovement.Model.InventoryMovement;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Web.Dto.ProductResponse;
import com.yordanov.warehouse.Web.Dto.ReceiveStockResponse;
import com.yordanov.warehouse.Web.Dto.WarehouseResponse;
import lombok.experimental.UtilityClass;
import com.yordanov.warehouse.Product.Model.Product;

import java.util.List;

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
                .updatedAt(warehouse.getUpdatedAt())
                .createdAt(warehouse.getCreatedAt())
                .build();
    }

    public static List<WarehouseResponse> toWarehouseResponseList(List<Warehouse> warehouses) {
        return warehouses.stream().map(DtoMapper::toWarehouseResponse).toList();
    }



}
