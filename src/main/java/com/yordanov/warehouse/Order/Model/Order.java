package com.yordanov.warehouse.Order.Model;

import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.OrderItem.Model.OrderItem;
import com.yordanov.warehouse.User.Model.User;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String orderReference;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    private UUID customerId;

    private UUID createdByEmployeeId;

    @Column(nullable = true)
    private String cancellationReason;
    
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    
    @Enumerated(EnumType.STRING)
    private OrderType orderType;    
    
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Order createCustomerOrder(String reference, UUID customerId, Warehouse warehouse, List<OrderItem> items){
        if (items == null || items.isEmpty()) {
        throw new IllegalArgumentException("Order must contain at least one item");
        }
        Order order = new Order();
        order.orderReference = reference;
        order.customerId = customerId;
        order.warehouse = warehouse;
        order.orderType = OrderType.ONLINE;
        order.orderStatus = OrderStatus.RESERVED;
        items.forEach(order::addItem);
        return order;
    }

    public static Order createEmployeeOrder(String reference, UUID createdByEmployeeId, Warehouse warehouse, List<OrderItem> items){
        if (items == null || items.isEmpty()) {
        throw new IllegalArgumentException("Order must contain at least one item");
        }

        Order order = new Order();
        order.orderReference = reference;
        order.createdByEmployeeId = createdByEmployeeId;
        order.warehouse = warehouse;
        order.orderType = OrderType.MANUAL;
        order.orderStatus = OrderStatus.RESERVED;
        items.forEach(order::addItem);
        return order;
    }

    public void addItem(OrderItem item){
        items.add(item);
        item.setOrder(this);
    }
    

    public void markShipped(){
        if(this.orderStatus != OrderStatus.RESERVED){
            throw new ConflictException("Order %s cannot be shipped from status %s".formatted(orderReference,orderStatus));
        }

        this.orderStatus = OrderStatus.SHIPPED;
    
    }

    public void cancel(String reason){

        if(this.orderStatus != OrderStatus.RESERVED){
            throw new ConflictException("Order %s cannot be cancelled from status %s".formatted(orderReference,orderStatus));
        }

        this.orderStatus = OrderStatus.CANCELLED;
        this.cancellationReason = reason;
    }

}