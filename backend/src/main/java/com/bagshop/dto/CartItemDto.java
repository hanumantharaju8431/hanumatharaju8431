package com.bagshop.dto;

import com.bagshop.entity.CartItem;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
    private String imageUrl;

    public static CartItemDto from(CartItem item) {
        var p = item.getProduct();
        var line = p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return CartItemDto.builder()
                .id(item.getId())
                .productId(p.getId())
                .productName(p.getName())
                .unitPrice(p.getPrice())
                .quantity(item.getQuantity())
                .lineTotal(line)
                .imageUrl(p.getImageUrl())
                .build();
    }
}
