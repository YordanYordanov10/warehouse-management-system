package com.yordanov.warehouse.StockService;

import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.Order.Event.OrderCreatedEvent;
import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Order.Service.OrderService;
import com.yordanov.warehouse.OrderItem.Model.OrderItem;
import com.yordanov.warehouse.Web.Dto.ReleaseStockRequest;
import com.yordanov.warehouse.Web.Dto.ReserveStockRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class StockEventListener {

    private final StockService stockService;
    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(StockEventListener.class);

    public StockEventListener(StockService stockService, OrderService orderService) {
        this.stockService = stockService;
        this.orderService = orderService;
    }

    @EventListener
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        Order order = event.getOrder();
        logger.info("Processing order {} with {} items", order.getId(), order.getItems().size());

        boolean allItemsReserved = true;
        StringBuilder failureReasons = new StringBuilder();

        try {
            // Try to reserve all items
            for (OrderItem item : order.getItems()) {
                try {
                    ReserveStockRequest reserveRequest = new ReserveStockRequest();
                    reserveRequest.setWarehouseId(item.getWarehouseId());
                    reserveRequest.setProductId(item.getProductId());
                    reserveRequest.setQuantity(item.getQuantity());

                    stockService.reserveStock(reserveRequest);
                    logger.info("Reserved {} units of product {} in warehouse {}",
                            item.getQuantity(), item.getProductId(), item.getWarehouseId());

                } catch (ConflictException e) {
                    allItemsReserved = false;
                    failureReasons.append(String.format(
                            "Product %s: %s; ",
                            item.getProductId(),
                            e.getMessage()
                    ));
                    logger.warn("Failed to reserve item: {}", e.getMessage());
                }
            }

            // Update order status based on reservation result
            if (allItemsReserved) {
                orderService.markAsReserved(order.getId());
                logger.info("Order {} fully reserved", order.getId());
            } else {
                // Partial failure - release what was reserved and mark as failed
                releasePartialReservations(order);
                orderService.markAsReservationFailed(order.getId(), failureReasons.toString());
                logger.warn("Order {} reservation failed: {}", order.getId(), failureReasons);
            }

        } catch (Exception e) {
            logger.error("Unexpected error processing order {}", order.getId(), e);
            releasePartialReservations(order);
            orderService.markAsReservationFailed(order.getId(), "Unexpected error: " + e.getMessage());
        }
    }

    private void releasePartialReservations(Order order) {
        // If reservation failed, release what was already reserved
        for (OrderItem item : order.getItems()) {
            try {
                ReleaseStockRequest releaseRequest = new ReleaseStockRequest();
                releaseRequest.setWarehouseId(item.getWarehouseId());
                releaseRequest.setProductId(item.getProductId());
                releaseRequest.setQuantity(item.getQuantity());

                stockService.releaseStock(releaseRequest);
                logger.info("Released {} units of product {} (rollback)", item.getQuantity(), item.getProductId());

            } catch (Exception e) {
                logger.error("Failed to release stock during rollback", e);
            }
        }
    }
}