package com.project.flowerms.repository;

import com.project.flowerms.entity.OrderDetail;
import com.project.flowerms.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder(Orders order);
}
