package com.project.flowerms.controller;

import com.project.flowerms.entity.Orders;
import com.project.flowerms.entity.User;
import com.project.flowerms.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    @Autowired
    private OrderService orderService;

    /** ✅ Dashboard - Đơn hàng PENDING */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User shipper = (User) session.getAttribute("loggedInUser");
        if (shipper == null || !"SHIPPER".equalsIgnoreCase(shipper.getRole().getRoleName())) {
            return "redirect:/login";
        }

        model.addAttribute("pendingOrders", orderService.getPendingOrders());
        return "shipper-dashboard";
    }

    /** ✅ Nhận đơn (PENDING → PROCESSING) */
    @PostMapping("/accept/{id}")
    public String acceptOrder(@PathVariable Integer id, HttpSession session) {
        User shipper = (User) session.getAttribute("loggedInUser");
        if (shipper == null || !"SHIPPER".equalsIgnoreCase(shipper.getRole().getRoleName())) {
            return "redirect:/login";
        }

        if (orderService.isShipperProcessingOrder(shipper.getUserId())) {
            return "redirect:/shipper/dashboard?error=busy";
        }

        orderService.assignOrderToShipper(id, shipper);
        return "redirect:/shipper/dashboard?success";
    }

    /** ✅ Đơn đang giao (PROCESSING) */
    @GetMapping("/processing")
    public String processingOrders(HttpSession session, Model model) {
        User shipper = (User) session.getAttribute("loggedInUser");
        if (shipper == null || !"SHIPPER".equalsIgnoreCase(shipper.getRole().getRoleName())) {
            return "redirect:/login";
        }

        model.addAttribute("processingOrders", orderService.getProcessingOrdersByShipper(shipper));
        return "shipper-processing";
    }

    /** ✅ Đơn hoàn tất hoặc bị hủy */
    @GetMapping("/completed")
    public String completedOrders(HttpSession session, Model model) {
        User shipper = (User) session.getAttribute("loggedInUser");
        if (shipper == null || !"SHIPPER".equalsIgnoreCase(shipper.getRole().getRoleName())) {
            return "redirect:/login";
        }

        model.addAttribute("completedOrders", orderService.getCompletedOrCanceledOrdersByShipper(shipper));
        return "shipper-completed";
    }

    /** ✅ Chuyển PROCESSING → SHIPPING */
    @PostMapping("/startShipping/{id}")
    public String startShipping(@PathVariable("id") Integer orderId) {
        Orders order = orderService.getOrderById(orderId);
        if (order != null && "Processing".equals(order.getStatus())) {
            order.setStatus("Shipping");
            orderService.save(order);
        } else if (order != null && "Shipping".equals(order.getStatus())) {
            order.setStatus("Completed");
            orderService.save(order);
        }
        return "redirect:/shipper/processing";
    }


    /** ✅ Hủy đơn: PROCESSING → CANCELED */
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Integer id) {
        orderService.updateStatus(id, "Cancelled");
        return "redirect:/shipper/processing?canceled";
    }

    /** ✅ Trả đơn: PROCESSING → PENDING (bỏ shipper) */
    @PostMapping("/return/{id}")
    public String returnOrder(@PathVariable Integer id) {
        orderService.returnToPending(id);
        return "redirect:/shipper/processing?returned";
    }
}
