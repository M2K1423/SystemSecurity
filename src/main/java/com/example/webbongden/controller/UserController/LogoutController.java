package com.example.webbongden.controller.UserController;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LogoutController", value = "/LogoutController")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy session hiện tại (nếu có)
        HttpSession session = request.getSession(false);

        // Xóa session
        if (session != null) {
            session.invalidate();
        }

        // Chuyển hướng về trang đăng nhập
        response.sendRedirect("home");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
