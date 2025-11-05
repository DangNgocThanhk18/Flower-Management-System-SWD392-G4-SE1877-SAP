package com.project.flowerms.service;

import com.project.flowerms.entity.OrderDetail;
import com.project.flowerms.entity.Orders;
import com.project.flowerms.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getDetailsByOrder(Orders order) {
        return orderDetailRepository.findByOrder(order);
    }
}
