package com.project.flowerms.controller;

import com.project.flowerms.entity.*;
import com.project.flowerms.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    // 🛒 Thêm sản phẩm vào giỏ
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Integer productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            model.addAttribute("error", "Vui lòng đăng nhập trước khi thêm vào giỏ hàng!");
            return "redirect:/login";
        }

        Products product = productService.getProductById(productId);
        Carts cart = cartService.getOrCreateCart(user);

        cartItemService.addToCart(cart, product, quantity);

        // 👉 Quay lại trang cũ thay vì chuyển hẳn sang /cart/view
        return "redirect:/home";
    }

    // 👀 Xem giỏ hàng
    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        Carts cart = cartService.getOrCreateCart(user);
        List<CartItems> items = cartItemService.getItemsByCart(cart);

        // ✅ Tính tổng tiền
        double total = 0;
        for (CartItems item : items) {
            double price = item.getProduct().getPrice();
            double discount = item.getProduct().getDiscount() != null ? item.getProduct().getDiscount() : 0;
            double finalPrice = price * (1 - discount);
            total += finalPrice * item.getQuantity();
        }

        model.addAttribute("cartItems", items);
        model.addAttribute("total", total);
        model.addAttribute("user", user); // ✅ THÊM DÒNG NÀY

        return "cart";
    }


    // 🧮 Cập nhật số lượng sản phẩm
    @PostMapping("/update")
    public String updateQuantity(@RequestParam("itemId") Integer itemId,
                                 @RequestParam("quantity") int quantity,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        cartItemService.updateQuantity(itemId, quantity);
        return "redirect:/cart/view";
    }

    // ❌ Xóa sản phẩm khỏi giỏ
    @PostMapping("/remove")
    public String removeItem(@RequestParam("itemId") Integer itemId,
                             HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        cartItemService.removeItem(itemId);
        return "redirect:/cart/view";
    }
}
