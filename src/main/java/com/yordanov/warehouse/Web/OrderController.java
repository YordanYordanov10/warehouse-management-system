package com.yordanov.warehouse.Web;

import com.yordanov.warehouse.Order.Model.Order;
import com.yordanov.warehouse.Order.Service.OrderService;
import com.yordanov.warehouse.Web.Dto.CreateOrderRequest;
import com.yordanov.warehouse.Web.Dto.CreateOrderResponse;
import com.yordanov.warehouse.Web.Mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid CreateOrderRequest createOrderRequest) {

        Order order = orderService.createOrder(createOrderRequest);
        CreateOrderResponse createOrderResponse = DtoMapper.toCreateOrderResponse(order);

    }
}
