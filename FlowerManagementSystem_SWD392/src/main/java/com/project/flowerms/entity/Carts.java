package com.project.flowerms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Carts")
@Getter
@Setter
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Integer cartId;
    @Column(name = "create_at", nullable = false)
    private String createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
