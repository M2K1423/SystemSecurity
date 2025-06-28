package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Order;
import com.example.webbongden.services.OrderSevices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user/confirm-signature")
public class ConfirmSignatureServlet extends HttpServlet {
    private OrderSevices orderService = new OrderSevices(); // hoặc OrderDao

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        Order order = null;
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                order = orderService.getOrderById(id); // nhớ kiểm tra trả về đúng order
            } catch (Exception e) {
                // Xử lý nếu cần
            }
        }
        request.setAttribute("latestOrder", order);
        request.getRequestDispatcher("/user/confirm-signature.jsp").forward(request, response);
    }
}
