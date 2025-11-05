package com.project.flowerms.service;

import com.project.flowerms.entity.*;
import com.project.flowerms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public void createOrder(Orders order, List<CartItems> items) {
        orderRepository.save(order);

        for (CartItems item : items) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());

            double discount = item.getProduct().getDiscount() != null ? item.getProduct().getDiscount() : 0;
            double price = item.getProduct().getPrice() * (1 - discount);
            detail.setPrice(price);

            orderDetailRepository.save(detail);
        }
    }
    public Orders getOrderByIdAndUser(Integer id, User user) {
        return orderRepository.findByOrderIdAndUser(id, user);
    }
    public List<Orders> searchOrdersByUser(User user, String keyword, String status) {
        List<Orders> orders = orderRepository.findByUser(user);

        // Lọc theo trạng thái
        if (status != null && !status.isEmpty()) {
            orders = orders.stream()
                    .filter(o -> o.getStatus() != null && o.getStatus().equalsIgnoreCase(status))
                    .toList();
        }

        // Tìm kiếm theo ID hoặc ngày
        if (keyword != null && !keyword.isEmpty()) {
            String lower = keyword.toLowerCase();
            orders = orders.stream()
                    .filter(o ->
                            (o.getOrderId() != null && o.getOrderId().toString().contains(lower)) ||
                                    (o.getOrderDate() != null && o.getOrderDate().toLowerCase().contains(lower))
                    )
                    .toList();
        }

        return orders;
    }
    public List<Orders> getPendingOrders() {
            return orderRepository.findByStatus("Pending");
        }

        public List<Orders> getProcessingOrdersByShipper(User shipper) {
            return orderRepository.findByShipperAndStatusIn(shipper,List.of("Processing", "Shipping"));
        }

        public List<Orders> getCompletedOrCanceledOrdersByShipper(User shipper) {
            return orderRepository.findByShipperAndStatusIn(shipper, List.of("Completed", "Cancelled"));
        }

        public boolean isShipperProcessingOrder(Integer shipperId) {
            return orderRepository.existsByShipper_UserIdAndStatus(shipperId, "Processing");
        }

        public void assignOrderToShipper(Integer orderId, User shipper) {
            Orders order = orderRepository.findById(orderId).orElseThrow();
            order.setStatus("Processing");
            order.setShipper(shipper);
            orderRepository.save(order);
        }

        public void updateStatus(Integer orderId, String status) {
            Orders order = orderRepository.findById(orderId).orElseThrow();
            order.setStatus(status);
            orderRepository.save(order);
        }

        public void returnToPending(Integer orderId) {
            Orders order = orderRepository.findById(orderId).orElseThrow();
            order.setStatus("Pending");
            order.setShipper(null);
            orderRepository.save(order);
        }
    public Orders getOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void save(Orders order) {
        orderRepository.save(order);
    }
    public List<Orders> searchAndFilterOrders(String keyword, String status) {
        if ((keyword == null || keyword.isEmpty()) && (status == null || status.isEmpty())) {
            return orderRepository.findAll();
        }
        return orderRepository.searchAndFilter(keyword, status);
    }
    }


