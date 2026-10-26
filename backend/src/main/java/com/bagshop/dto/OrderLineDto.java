package com.bagshop.dto;

import com.bagshop.entity.OrderItem;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderLineDto {
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;

    public static OrderLineDto from(OrderItem item) {
        return OrderLineDto.builder()
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .build();
    }
}
