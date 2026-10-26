package com.bagshop.controller;

import com.bagshop.dto.*;
import com.bagshop.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartItemDto> getCart() {
        return cartService.getCart();
    }

    @PostMapping
    public CartItemDto add(@Valid @RequestBody CartAddRequest req) {
        return cartService.add(req);
    }

    @PutMapping("/{id}")
    public CartItemDto update(@PathVariable Long id, @Valid @RequestBody CartUpdateRequest req) {
        return cartService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ApiMessage remove(@PathVariable Long id) {
        cartService.remove(id);
        return new ApiMessage("Removed from cart");
    }
}
