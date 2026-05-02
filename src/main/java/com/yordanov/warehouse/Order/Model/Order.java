package com.yordanov.warehouse.Order.Model;

import com.yordanov.warehouse.Order.Model.OrderStatus;
import com.yordanov.warehouse.Order.Model.OrderType;
import com.yordanov.warehouse.OrderItem.Model.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String orderReference;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // NEW: Track who created the order
    private UUID customerId;           // For ONLINE/CUSTOMER orders
    private UUID createdByEmployeeId;  // For MANUAL orders

    // NEW: Order type
    @Enumerated(EnumType.STRING)
    private OrderType orderType;       // ONLINE, MANUAL, IMPORT

    // NEW: Track reservation
    private boolean reserved;
    private String reservationFailureReason;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}