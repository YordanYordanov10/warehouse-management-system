package com.yordanov.warehouse.Web;

import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Order.Service.OrderService;
import com.yordanov.warehouse.Web.Dto.CustomerOrderRequest;
import com.yordanov.warehouse.Web.Dto.CreateOrderRequest;
import com.yordanov.warehouse.Web.Dto.CreateOrderResponse;
import com.yordanov.warehouse.Web.Dto.EmployeeOrderRequest;
import com.yordanov.warehouse.Web.Dto.ImportOrderRequest;
import com.yordanov.warehouse.Web.Dto.ErrorResponse;
import com.yordanov.warehouse.Web.Mapper.DtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

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
    @PostMapping("/createOrder")
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {

        Order order = orderService.createOrder(createOrderRequest);
        CreateOrderResponse createOrderResponse = DtoMapper.toCreateOrderResponse(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponse);

    }

    @Operation(summary = "Create customer order",
            description = "Creates an online customer order and starts async stock reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/orders/customer/{customerId}")
    public ResponseEntity<CreateOrderResponse> createCustomerOrder(
            @Parameter(description = "Customer identifier", required = true)
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerOrderRequest request) {

        Order order = orderService.createCustomerOrder(request, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toCreateOrderResponse(order));
    }

    @Operation(summary = "Create employee order",
            description = "Creates a manual employee order and starts async stock reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/orders/employee/{employeeId}")
    public ResponseEntity<CreateOrderResponse> createEmployeeOrder(
            @Parameter(description = "Employee identifier", required = true)
            @PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeOrderRequest request) {

        Order order = orderService.createEmployeeOrder(request, employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toCreateOrderResponse(order));
    }

    @Operation(summary = "Create import order",
            description = "Creates an imported order and starts async stock reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Import order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/orders/import")
    public ResponseEntity<CreateOrderResponse> createImportOrder(@Valid @RequestBody ImportOrderRequest request) {

        Order order = orderService.createImportOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toCreateOrderResponse(order));
    }
}
