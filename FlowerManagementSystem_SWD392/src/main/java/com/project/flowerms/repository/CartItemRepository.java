package com.project.flowerms.repository;

import com.project.flowerms.entity.CartItems;
import com.project.flowerms.entity.Carts;
import com.project.flowerms.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Integer> {
    List<CartItems> findByCart(Carts cart);
    Optional<CartItems> findByCartAndProduct(Carts cart, Products product);
}
