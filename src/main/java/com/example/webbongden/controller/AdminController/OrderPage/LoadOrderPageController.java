package com.example.webbongden.controller.AdminController.OrderPage;

import com.example.webbongden.services.OrderSevices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LoadOrderPageController", value = "/order-management")
public class LoadOrderPageController extends HttpServlet {
    private static final OrderSevices orderServices;

    static {
        orderServices = new OrderSevices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int totalOrders = orderServices.getTotalOrders();
            request.setAttribute("totalOrders", totalOrders);
            request.getRequestDispatcher("admin/order-management.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
