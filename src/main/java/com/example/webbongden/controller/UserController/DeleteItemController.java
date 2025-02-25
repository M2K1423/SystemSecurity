package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteItemController", value = "/delete-cart-item")
public class DeleteItemController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy `productId` từ request
        int productId = Integer.parseInt(request.getParameter("productId"));

        // Lấy giỏ hàng từ session
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            cart.removeItem(productId);
            session.setAttribute("cart", cart);
        }

        // Chuyển hướng về trang giỏ hàng
        response.sendRedirect("/WebBongDen_war/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
