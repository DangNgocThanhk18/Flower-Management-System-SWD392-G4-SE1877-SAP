package com.project.flowerms.service;

import com.project.flowerms.entity.Products;
import com.project.flowerms.entity.Categories;
import com.project.flowerms.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả sản phẩm
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    // Lấy sản phẩm theo danh mục
    public List<Products> getProductsByCategory(Categories category) {
        return productRepository.findByCategory(category);
    }

    // Tìm kiếm theo tên
    public List<Products> searchProducts(String keyword) {
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }

    // Lấy sản phẩm theo ID
    public Products getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    // Thêm hoặc cập nhật sản phẩm
    public Products saveProduct(Products product) {
        return productRepository.save(product);
    }

    // Xóa sản phẩm
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}

