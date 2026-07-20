package com.yordanov.warehouse.Web;

import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Order.Service.OrderService;
import com.yordanov.warehouse.Web.Dto.CancelOrderRequest;
import com.yordanov.warehouse.Web.Dto.OrderResponse;
import com.yordanov.warehouse.Web.Dto.PlaceCustomerOrderRequest;
import com.yordanov.warehouse.Web.Dto.ErrorResponse;
import com.yordanov.warehouse.Web.Mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Orders", description = "Endpoints for creating and managing orders")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private static final UUID TEMP_CUSTOMER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create order",
            description = "Creates a basic order and starts async stock reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody PlaceCustomerOrderRequest placeCustomerOrderRequest) {


        Order order = orderService.placeCustomerOrder(placeCustomerOrderRequest, TEMP_CUSTOMER_ID);
        OrderResponse createOrderResponse = DtoMapper.toCreateOrderResponse(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponse);

    }

    @Operation(summary = "Cancel order",
        description = "Cancels a reserved order and releases its stock reservations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Order cannot be cancelled from its current status",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable("id") UUID orderId, @Valid @RequestBody CancelOrderRequest cancelOrderRequest){
        
        Order order = orderService.cancelCustomerOrder(orderId, cancelOrderRequest.getReason());
        OrderResponse cancelResponse = DtoMapper.toCreateOrderResponse(order);
        return ResponseEntity.status(HttpStatus.OK).body(cancelResponse);
        
    }

    @Operation(summary = "Ship order",
        description = "Ships a reserved order, consuming its stock reservations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order shipped successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Order cannot be shipped from its current status",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/ship")
    public ResponseEntity<OrderResponse> shipOrder(@PathVariable("id") UUID orderId){

        Order order = orderService.shipCustomerOrder(orderId);
        OrderResponse shipResponse = DtoMapper.toCreateOrderResponse(order);
        return ResponseEntity.status(HttpStatus.OK).body(shipResponse);
    }

}
