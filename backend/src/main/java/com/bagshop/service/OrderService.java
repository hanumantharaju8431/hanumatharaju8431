package com.bagshop.service;

import com.bagshop.dto.CheckoutRequest;
import com.bagshop.dto.OrderDto;
import com.bagshop.entity.*;
import com.bagshop.repository.CartItemRepository;
import com.bagshop.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CurrentUserService currentUserService;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
                        CurrentUserService currentUserService) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.currentUserService = currentUserService;
    }

    public List<OrderDto> listOrders() {
        User user = currentUserService.requireUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream().map(OrderDto::from).toList();
    }

    @Transactional
    public OrderDto checkout(CheckoutRequest req) {
        User user = currentUserService.requireUser();
        List<CartItem> cart = cartItemRepository.findByUser(user);
        if (cart.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> lines = new ArrayList<>();
        for (CartItem ci : cart) {
            var p = ci.getProduct();
            if (p.getStock() < ci.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for " + p.getName());
            }
            p.setStock(p.getStock() - ci.getQuantity());
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            total = total.add(line);
            lines.add(OrderItem.builder()
                    .productId(p.getId())
                    .productName(p.getName())
                    .quantity(ci.getQuantity())
                    .unitPrice(p.getPrice())
                    .build());
        }
        Order order = Order.builder()
                .user(user)
                .totalAmount(total)
                .status("PLACED")
                .shippingAddress(req.getShippingAddress())
                .createdAt(Instant.now())
                .build();
        for (OrderItem line : lines) {
            line.setOrder(order);
            order.getItems().add(line);
        }
        order = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);
        return OrderDto.from(order);
    }
}
