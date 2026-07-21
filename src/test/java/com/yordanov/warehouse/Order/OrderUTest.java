package com.yordanov.warehouse.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Order.Model.OrderStatus;
import com.yordanov.warehouse.Order.Model.OrderType;
import com.yordanov.warehouse.OrderItem.Model.OrderItem;
import com.yordanov.warehouse.Product.Model.Product;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;

public class OrderUTest {

    private Warehouse warehouse;
    private Product product1;
    private Product product2;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private List<OrderItem> items = new ArrayList<>();
    private static final UUID TEMP_CUSTOMER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID TEMP_EMPLOYEE_ID = UUID.fromString("22111111-1111-1111-1111-111111111111");
    
    @BeforeEach
    void setUp() {
        product1 = Product.builder()
                .name("Test Product 1")
                .sku("SKU-001")
                .price(BigDecimal.TEN)
                .build();
        
        product2 = Product.builder()
                .name("Test Product 2")
                .sku("SKU-002")
                .price(BigDecimal.TWO)
                .build();       

        warehouse = Warehouse.builder()
                .name("Test Warehouse")
                .warehouseCode("WH-001")
                .address("Some street 1")
                .city("Sofia")
                .postalCode("1000")
                .country("Bulgaria")
                .maxPalletCapacity(100)
                .warehouseStatus(WarehouseStatus.ACTIVE)
                .build();
        orderItem1 = OrderItem.builder()
                .productId(product1.getId())
                .quantity(10)    
                .build();
        orderItem2 = OrderItem.builder()
                .productId(product2.getId())
                .quantity(20)    
                .build();        
        items.add(orderItem1);
        items.add(orderItem2);        
    }

    @Test
    void createCustomerOrder_shouldThrowException_whenItemsIsNull(){
        assertThrows(
            IllegalArgumentException.class,
                () -> Order.createCustomerOrder("ORD-0001", TEMP_CUSTOMER_ID, warehouse, null)  
        );

    }

    @Test
    void createCustomerOrder_shouldThrowException_whenItemsIsEmpty(){
        assertThrows(
            IllegalArgumentException.class,
                () -> Order.createCustomerOrder("ORD-0001", TEMP_CUSTOMER_ID, warehouse, items = new ArrayList<>())  
        );

    }

    @Test
     void createEmployeeOrder_shouldCreateOrderWithManualType(){

         Order order = Order.createEmployeeOrder("ORD-0002", TEMP_EMPLOYEE_ID, warehouse, items);

        assertEquals(OrderType.MANUAL, order.getOrderType());
        assertEquals(TEMP_EMPLOYEE_ID, order.getCreatedByEmployeeId());
        assertEquals(OrderStatus.RESERVED, order.getOrderStatus());
    }
 
    @Test
    void markShipped_changesStatusToShipped(){   

     Order order = Order.createCustomerOrder("ORD-0001", TEMP_CUSTOMER_ID, warehouse, items);   
     order.markShipped();

     assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());

    }

    @Test
    void markShipped_changesStatusCancelled_throwException(){   

     Order order = Order.createCustomerOrder("ORD-0001", TEMP_CUSTOMER_ID, warehouse, items);  
     order.setOrderStatus(OrderStatus.CANCELLED);
     assertThrows(
            ConflictException.class,
                () -> order.markShipped()
        );

    }

    @Test
    void markShipped_changesStatusShipped_throwException(){   

     Order order = Order.createCustomerOrder("ORD-0001", TEMP_CUSTOMER_ID, warehouse, items);  
     order.setOrderStatus(OrderStatus.SHIPPED);
     assertThrows(
            ConflictException.class,
                () -> order.markShipped()
        );

    }

    @Test
    void cancel_changesStatusToShipped(){   

     Order order = Order.createCustomerOrder("ORD-0001", TEMP_CUSTOMER_ID, warehouse, items);   
     order.cancel("reason");

     assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());

    }

    @Test
    void cancel_changesStatusShipped_throwException(){   

     Order order = Order.createCustomerOrder("ORD-0001", TEMP_CUSTOMER_ID, warehouse, items);  
     order.setOrderStatus(OrderStatus.SHIPPED);

     assertThrows(
            ConflictException.class,
                () -> order.cancel("reason")
        );

    }

    @Test
    void cancel_changesCancel_throwException(){   

     Order order = Order.createCustomerOrder("ORD-0001", TEMP_CUSTOMER_ID, warehouse, items);  
     order.setOrderStatus(OrderStatus.CANCELLED);

     assertThrows(
            ConflictException.class,
                () -> order.cancel("reason")
        );

    }
}
