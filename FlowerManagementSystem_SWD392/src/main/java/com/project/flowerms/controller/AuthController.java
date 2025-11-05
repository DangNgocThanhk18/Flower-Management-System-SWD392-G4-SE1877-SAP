package com.project.flowerms.controller;

import com.project.flowerms.entity.Products;
import com.project.flowerms.entity.User;
import com.project.flowerms.service.ProductService;
import com.project.flowerms.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String loginAction(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {

        User user = userService.login(username, password);

        if (user == null) {
            model.addAttribute("error", "Sai username hoặc password!");
            return "login";
        }

        String role = user.getRole().getRoleName().toUpperCase();

        // Lưu session
        session.setAttribute("loggedInUser", user);

        // Điều hướng theo role
        switch (role) {
            case "CUSTOMER":
                return "redirect:/home";
            case "SHIPPER":
                return "redirect:/shipper/dashboard";
            case "MANAGER":
                return "redirect:/manager/orders";
            default:
                model.addAttribute("error", "Tài khoản không có quyền truy cập!");
                session.invalidate();
                return "login";
        }
    }


    @GetMapping("/home")
    public String homePage(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedInUser");
        List<Products> list = productService.getAllProducts();
        model.addAttribute("products", list);
        model.addAttribute("user", loggedUser);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
