package com.project.flowerms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Orders")
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer orderId;
    @Column(name = "order_date", nullable = false)
    private String orderDate;
    private String status;
    @Column(name = "total_amount", nullable = false)
    private Double total;
    @Column(name = "shipping_address", nullable = false)
    private String address;
    private String phone;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private User shipper;
}
