package com.project.flowerms.service;

import com.project.flowerms.entity.*;
import com.project.flowerms.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public void addToCart(Carts cart, Products product, int quantity) {
        CartItems existingItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItems newItem = new CartItems();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    public List<CartItems> getItemsByCart(Carts cart) {
        return cartItemRepository.findByCart(cart);
    }

    public void updateQuantity(Integer itemId, int quantity) {
        CartItems item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null && quantity > 0) {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    public void removeItem(Integer itemId) {
        cartItemRepository.deleteById(itemId);
    }
    public void clearCart(Carts cart) {
        List<CartItems> items = cartItemRepository.findByCart(cart);
        cartItemRepository.deleteAll(items);
    }

}
