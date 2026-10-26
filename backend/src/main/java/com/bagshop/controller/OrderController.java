package com.bagshop.controller;

import com.bagshop.dto.CheckoutRequest;
import com.bagshop.dto.OrderDto;
import com.bagshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDto> list() {
        return orderService.listOrders();
    }

    @PostMapping("/checkout")
    public OrderDto checkout(@Valid @RequestBody CheckoutRequest req) {
        return orderService.checkout(req);
    }
}
