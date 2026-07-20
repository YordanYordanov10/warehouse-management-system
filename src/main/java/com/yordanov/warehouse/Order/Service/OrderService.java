package com.yordanov.warehouse.Order.Service;

import com.yordanov.warehouse.Exception.ResourceNotFoundException;
import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Order.Model.OrderStatus;
import com.yordanov.warehouse.Order.Model.OrderType;
import com.yordanov.warehouse.Order.Repository.OrderRepository;
import com.yordanov.warehouse.OrderItem.Model.OrderItem;
import com.yordanov.warehouse.ReferenceSequence.Service.ReferenceSequenceService;
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


    public OrderService(OrderRepository orderRepository,
                        ReferenceSequenceService referenceSequenceService,
                        WarehouseRepository warehouseRepository) {
        this.orderRepository = orderRepository;
        this.referenceSequenceService = referenceSequenceService;
        this.warehouseRepository = warehouseRepository;
    }

    @Value("${app.default-warehouse-id}")
    private UUID defaultWarehouseId;

 

    @Transactional
    public Order placeCustomerOrder(PlaceCustomerOrderRequest request, UUID customerId) {

        Warehouse warehouse = warehouseRepository.findById(defaultWarehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        List<OrderItem> items = createOrderItems(request.getItems());
        String reference = referenceSequenceService.generateReference(ReferenceType.ORDER, warehouse.getId());
        Order order = Order.createCustomerOrder(reference, customerId, warehouse, items);

        return  orderRepository.save(order);
    }

    // @Transactional
    // public Order createCustomerOrder(CustomerOrderRequest request, UUID customerId) {
    //     List<OrderItem> items = createOrderItems(request.items(), null);

    //     Order order = Order.builder()
    //             .orderReference(referenceSequenceService.generateReference(ReferenceType.ORDER, null))
    //             .orderStatus(OrderStatus.CREATED)
    //             .customerId(customerId)
    //             .orderType(OrderType.ONLINE)
    //             .reserved(false)
    //             .createdAt(LocalDateTime.now())
    //             .updatedAt(LocalDateTime.now())
    //             .items(items)
    //             .build();

    //     items.forEach(item -> item.setOrder(order));

    //     Order savedOrder = orderRepository.save(order);

    //     // Publish event - StockEventListener will handle auto-reservation
    //     eventPublisher.publishEvent(new OrderCreatedEvent(this, savedOrder));

    //     return savedOrder;
    // }

    // @Transactional
    // public Order createEmployeeOrder(EmployeeOrderRequest request, UUID employeeId) {
    //     List<OrderItem> items = createOrderItems(request.items(), request.warehouseId());

    //     Order order = Order.builder()
    //             .orderReference(request.externalReference())
    //             .orderStatus(OrderStatus.CREATED)
    //             .customerId(request.customerId())
    //             .createdByEmployeeId(employeeId)
    //             .orderType(OrderType.MANUAL)
    //             .reserved(false)
    //             .createdAt(LocalDateTime.now())
    //             .updatedAt(LocalDateTime.now())
    //             .items(items)
    //             .build();

    //     items.forEach(item -> item.setOrder(order));

    //     Order savedOrder = orderRepository.save(order);

    //     // Publish event
    //     eventPublisher.publishEvent(new OrderCreatedEvent(this, savedOrder));

    //     return savedOrder;
    // }

    // @Transactional
    // public Order createImportOrder(ImportOrderRequest request) {
    //     List<OrderItem> items = createOrderItems(request.items(), request.warehouseId());

    //     Order order = Order.builder()
    //             .orderReference(request.externalOrderId())
    //             .orderStatus(OrderStatus.CREATED)
    //             .customerId(request.customerId())
    //             .orderType(OrderType.IMPORT)
    //             .reserved(false)
    //             .createdAt(LocalDateTime.now())
    //             .updatedAt(LocalDateTime.now())
    //             .items(items)
    //             .build();

    //     items.forEach(item -> item.setOrder(order));

    //     Order savedOrder = orderRepository.save(order);

    //     // Publish event
    //     eventPublisher.publishEvent(new OrderCreatedEvent(this, savedOrder));

    //     return savedOrder;
    // }

    private List<OrderItem> createOrderItems(List<OrderItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(item -> OrderItem.builder()
                        .productId(item.productId())
                        .quantity(item.quantity())
                        .build())
                .toList();
    }

    // public Order updateOrderStatus(UUID orderId, OrderStatus status) {
    //     Order order = orderRepository.findById(orderId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    //     order.setOrderStatus(status);
    //     order.setUpdatedAt(LocalDateTime.now());
    //     return orderRepository.save(order);
    // }

    // public Order markAsReserved(UUID orderId) {
    //     Order order = orderRepository.findById(orderId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    //     order.setReserved(true);
    //     order.setOrderStatus(OrderStatus.RESERVED);
    //     order.setUpdatedAt(LocalDateTime.now());
    //     return orderRepository.save(order);
    // }

    // public Order markAsReservationFailed(UUID orderId, String reason) {
    //     Order order = orderRepository.findById(orderId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    //     order.setReserved(false);
    //     order.setReservationFailureReason(reason);
    //     order.setOrderStatus(OrderStatus.CREATED);  // Back to CREATED state
    //     order.setUpdatedAt(LocalDateTime.now());
    //     return orderRepository.save(order);
    // }
}
