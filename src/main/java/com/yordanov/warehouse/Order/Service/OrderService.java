package com.yordanov.warehouse.Order.Service;

import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Order.Model.OrderStatus;
import com.yordanov.warehouse.Order.Repository.OrderRepository;
import com.yordanov.warehouse.OrderItem.Model.OrderItem;
import com.yordanov.warehouse.ReferenceSequence.Service.ReferenceSequenceService;
import com.yordanov.warehouse.Web.Dto.CreateOrderRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ReferenceSequenceService referenceSequenceService;

    public OrderService(OrderRepository orderRepository, ReferenceSequenceService referenceSequenceService) {
        this.orderRepository = orderRepository;
        this.referenceSequenceService = referenceSequenceService;
    }


    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) {

        Order order = new Order();

        List<OrderItem> items = createOrderRequest.getItems().stream()
                .map(i -> OrderItem.builder()
                        .order(order)
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .build())
                .toList();

        order.setOrderReference(referenceSequenceService.generateReference(ReferenceType.ORDER,null));
        order.setItems(items);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return order;
    }
}
