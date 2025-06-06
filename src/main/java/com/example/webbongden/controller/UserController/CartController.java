package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Cart;
import com.example.webbongden.dao.model.Promotion;
import com.example.webbongden.services.PromotionService;
import com.example.webbongden.dao.model.PublicKey;
import com.example.webbongden.services.PublicKeyServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "CartController", value = "/cart")
public class CartController extends HttpServlet {
    public static final Cart cart;
    private static final PromotionService promotionService;
    private static final PublicKeyServices publicKeyServices;
    static {
        cart = new Cart();
        promotionService = new PromotionService();
        publicKeyServices = new PublicKeyServices();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            // Nếu không có thông tin Account trong session, chuyển hướng đến trang đăng nhập
            response.sendRedirect("/SystemSecurity_war/login");
            return;
        }

        if (cart != null) {
            // Kiểm tra và gắn thông tin quà tặng cho từng sản phẩm trong giỏ hàng
            cart.getItems().forEach(item -> {
                // Lấy thông tin khuyến mãi/quà tặng dựa trên productId
                Promotion gift = promotionService.getPromotionById(item.getProductId());
                if (gift != null && "Quà tặng".equals(gift.getPromotionType())) {
                    item.setGiftName(gift.getPromotionName()); // Gắn tên quà tặng vào sản phẩm
                }
            });
        }

        //Lấy khoá công khai và gửi xuống giỏ hàng JSP
        PublicKey publicKey = publicKeyServices.getPublicKey(account.getId());
        request.setAttribute("publicKey", Objects.requireNonNullElseGet(publicKey, PublicKey::new));
        // Gửi giỏ hàng sang JSP
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/user/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
