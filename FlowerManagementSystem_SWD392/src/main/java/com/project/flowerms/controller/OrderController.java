package com.project.flowerms.controller;

import com.project.flowerms.entity.OrderDetail;
import com.project.flowerms.entity.Orders;
import com.project.flowerms.entity.User;
import com.project.flowerms.service.OrderDetailService;
import com.project.flowerms.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    // ✅ Danh sách đơn hàng của user
    @GetMapping("/view")
    public String viewOrders(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        List<Orders> orders = orderService.searchOrdersByUser(user, keyword, status);

        model.addAttribute("orders", orders);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("user", user);
        return "order-list";
    }


    // ✅ Chi tiết đơn hàng
    @GetMapping("/detail/{id}")
    public String viewOrderDetail(@PathVariable("id") Integer id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Orders order = orderService.getOrderByIdAndUser(id, user);
        if (order == null) return "redirect:/order/view";

        List<OrderDetail> details = orderDetailService.getDetailsByOrder(order);
        model.addAttribute("order", order);
        model.addAttribute("details", details);
        model.addAttribute("user", user);
        return "order-detail";
    }
}

