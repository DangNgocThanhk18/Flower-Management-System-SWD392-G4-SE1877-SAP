package com.project.flowerms.repository;

import com.project.flowerms.entity.Products;
import com.project.flowerms.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
    List<Products> findByCategory(Categories category);
    List<Products> findByProductNameContainingIgnoreCase(String name);
}

