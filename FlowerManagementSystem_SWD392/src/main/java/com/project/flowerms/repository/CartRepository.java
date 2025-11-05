package com.project.flowerms.repository;

import com.project.flowerms.entity.Carts;
import com.project.flowerms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Carts, Integer> {
    Optional<Carts> findByUser(User user);
}

