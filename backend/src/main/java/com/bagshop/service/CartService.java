package com.bagshop.service;

import com.bagshop.dto.*;
import com.bagshop.entity.CartItem;
import com.bagshop.entity.User;
import com.bagshop.repository.CartItemRepository;
import com.bagshop.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository,
                       CurrentUserService currentUserService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    public List<CartItemDto> getCart() {
        User user = currentUserService.requireUser();
        return cartItemRepository.findByUser(user).stream().map(CartItemDto::from).toList();
    }

    @Transactional
    public CartItemDto add(CartAddRequest req) {
        User user = currentUserService.requireUser();
        var product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (product.getStock() < req.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock");
        }
        var existing = cartItemRepository.findByUserAndProductId(user, product.getId());
        CartItem item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setQuantity(item.getQuantity() + req.getQuantity());
        } else {
            item = CartItem.builder().user(user).product(product).quantity(req.getQuantity()).build();
        }
        return CartItemDto.from(cartItemRepository.save(item));
    }

    @Transactional
    public CartItemDto update(Long id, CartUpdateRequest req) {
        User user = currentUserService.requireUser();
        CartItem item = cartItemRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));
        if (item.getProduct().getStock() < req.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock");
        }
        item.setQuantity(req.getQuantity());
        return CartItemDto.from(cartItemRepository.save(item));
    }

    @Transactional
    public void remove(Long id) {
        User user = currentUserService.requireUser();
        CartItem item = cartItemRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));
        cartItemRepository.delete(item);
    }
}
