package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateCartController", value = "/update-cart")
public class UpdateCartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);

            if (cart != null) {
                cart.updateQuantity(productId, quantity); // Cập nhật số lượng trong giỏ hàng
            }

            // Lấy tổng tiền sau khi cập nhật
            double totalPrice = cart != null ? cart.getTotalPriceNumber() : 0;
            int totalQuantity = cart != null ? cart.getTotalQuantity() : 0;

            // Trả về JSON chứa tổng tiền và tổng số lượng
            response.getWriter().write("{\"success\": true, \"totalPrice\": " + totalPrice + ", \"totalQuantity\": " + totalQuantity + "}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi khi cập nhật\"}");
        }
    }
}

