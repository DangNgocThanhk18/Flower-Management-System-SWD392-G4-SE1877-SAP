package com.project.flowerms.controller;

import com.project.flowerms.entity.*;
import com.project.flowerms.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showCheckoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Carts cart = cartService.getOrCreateCart(user);
        List<CartItems> items = cartItemService.getItemsByCart(cart);

        if (items.isEmpty()) {
            model.addAttribute("message", "Giỏ hàng của bạn đang trống!");
            return "cart";
        }

        double total = items.stream().mapToDouble(item -> {
            double discount = item.getProduct().getDiscount() != null ? item.getProduct().getDiscount() : 0;
            double price = item.getProduct().getPrice() * (1 - discount);
            return price * item.getQuantity();
        }).sum();

        model.addAttribute("cartItems", items);
        model.addAttribute("total", total);
        model.addAttribute("user", user);

        return "checkout";
    }

    @PostMapping("/confirm")
    public String confirmCheckout(@RequestParam String address,
                                  @RequestParam String phone,
                                  @RequestParam(required = false) String note,
                                  HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Carts cart = cartService.getOrCreateCart(user);
        List<CartItems> items = cartItemService.getItemsByCart(cart);

        if (items.isEmpty()) return "redirect:/cart/view";

        // Tạo đơn hàng
        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        order.setAddress(address);
        order.setPhone(phone);
        order.setNote(note);
        order.setStatus("Pending");

        double total = 0;
        for (CartItems item : items) {
            double discount = item.getProduct().getDiscount() != null ? item.getProduct().getDiscount() : 0;
            double price = item.getProduct().getPrice() * (1 - discount);
            total += price * item.getQuantity();
        }
        order.setTotal(total);

        // Lưu order và chi tiết
        orderService.createOrder(order, items);

        // Xóa giỏ hàng
        cartItemService.clearCart(cart);

        return "redirect:/checkout/success";
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }
}

