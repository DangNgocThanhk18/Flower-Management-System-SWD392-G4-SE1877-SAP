package com.project.flowerms.service;

import com.project.flowerms.entity.*;
import com.project.flowerms.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Carts getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Carts newCart = new Carts();
                    newCart.setUser(user);
                    newCart.setCreatedAt(LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    return cartRepository.save(newCart);
                });
    }
}

