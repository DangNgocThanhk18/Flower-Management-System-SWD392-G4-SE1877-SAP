package com.project.flowerms.controller;

import com.project.flowerms.entity.Orders;
import com.project.flowerms.service.OrderService;
import com.project.flowerms.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String viewOrders(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String status,
                             Model model) {

        List<Orders> orders = orderService.searchAndFilterOrders(keyword, status);
        model.addAttribute("orders", orders);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "manager-dashboard";
    }

    @PostMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Integer orderId) {
        Orders order = orderService.getOrderById(orderId);
        if (order != null) {
            order.setStatus("Cancelled");
            order.setShipper(null); // xóa shipper nếu có
            orderService.save(order);
        }
        return "redirect:/manager/orders";
    }

    @PostMapping("/order/return/{id}")
    public String returnOrder(@PathVariable("id") Integer orderId) {
        Orders order = orderService.getOrderById(orderId);
        if (order != null) {
            order.setStatus("Pending");
            order.setShipper(null); // xóa shipper nếu có
            orderService.save(order);
        }
        return "redirect:/manager/orders";
    }
    @Autowired
    private ShipperService shipperService;

    @GetMapping("shipper-list")
    public String listShippers(Model model) {
        model.addAttribute("shippers", shipperService.getAllShippers());
        return "manager-shipper-list"; // 🔹 view Thymeleaf
    }

    @PostMapping("/toggle/{id}")
    public String toggleStatus(@PathVariable("id") Integer id) {
        shipperService.toggleActiveStatus(id);
        return "redirect:/manager/shipper-list";
    }
}

