package com.project.flowerms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Products")
@Getter
@Setter
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer productId;
    @Column(name = "product_name", nullable = false)
    private String productName;
    private String description;
    private Double price;
    private Double discount;
    @Column(name = "stock_quantity", nullable = false)
    private Integer quantity;
    @Column(name = "image_url")
    private String imageURL;
    @Column(name = "create_at")
    private String createdAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;
}


