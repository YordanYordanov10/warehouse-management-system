package com.yordanov.warehouse.Order.Service;

import com.yordanov.warehouse.Exception.ResourceNotFoundException;
import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Order.Repository.OrderRepository;
import com.yordanov.warehouse.OrderItem.Model.OrderItem;
import com.yordanov.warehouse.ReferenceSequence.Service.ReferenceSequenceService;
import com.yordanov.warehouse.StockService.StockService;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Warehouse.Repository.WarehouseRepository;
import com.yordanov.warehouse.Web.Dto.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ReferenceSequenceService referenceSequenceService;
    private final WarehouseRepository warehouseRepository;
    private final StockService stockService;


    public OrderService(OrderRepository orderRepository,
                        ReferenceSequenceService referenceSequenceService,
                        WarehouseRepository warehouseRepository, StockService stockService) {
        this.orderRepository = orderRepository;
        this.referenceSequenceService = referenceSequenceService;
        this.warehouseRepository = warehouseRepository;
        this.stockService = stockService;
        
    }

    @Value("${app.default-warehouse-id}")
    private UUID defaultWarehouseId;

 

    @Transactional
    public Order placeCustomerOrder(PlaceCustomerOrderRequest request, UUID customerId) {

        Warehouse warehouse = warehouseRepository.findById(defaultWarehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        List<OrderItem> items = createOrderItems(request.getItems());
        String reference = referenceSequenceService.generateReference(ReferenceType.ORDER, warehouse.getId());
        Order order = Order.createCustomerOrder(reference, customerId, warehouse, items);

        for (OrderItem item : order.getItems()) {
        stockService.reserveStock(warehouse.getId(), item.getProductId(), item.getQuantity(), order.getOrderReference());
}
        return  orderRepository.save(order);
    }

    @Transactional
    public Order cancelCustomerOrder(UUID orderId, String reason){

        Order order = orderRepository.findById(orderId).orElseThrow(() ->  new ResourceNotFoundException("Order %s not found".formatted(orderId))); 
        order.cancel(reason);  
        for (OrderItem item : order.getItems()){
            stockService.releaseStock(order.getWarehouse().getId(), item.getProductId(), item.getQuantity(), order.getOrderReference());
        }

    
        return orderRepository.save(order);
    }

    @Transactional
    public Order shipCustomerOrder(UUID orderId) {
        
        Order order = orderRepository.findById(orderId).orElseThrow(() ->  new ResourceNotFoundException("Order %s not found".formatted(orderId))); 
        order.markShipped();
        for (OrderItem item : order.getItems()) {
            stockService.shipStock(order.getWarehouse().getId(), item.getProductId(),
                    item.getQuantity(), order.getOrderReference());
        }
        return orderRepository.save(order);
    }

   

    private List<OrderItem> createOrderItems(List<OrderItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(item -> OrderItem.builder()
                        .productId(item.productId())
                        .quantity(item.quantity())
                        .build())
                .toList();
    }

   
}
