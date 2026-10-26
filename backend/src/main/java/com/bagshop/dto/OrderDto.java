package com.bagshop.dto;

import com.bagshop.entity.Order;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long id;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private Instant createdAt;
    private List<OrderLineDto> lines;

    public static OrderDto from(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt())
                .lines(order.getItems().stream().map(OrderLineDto::from).toList())
                .build();
    }
}
