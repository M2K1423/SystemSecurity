package com.example.webbongden.controller.AdminController.AccountPage;

import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "deleteAccount", value = "/deleteAccount")
public class DeleteAccountController extends HttpServlet {
    private static final AccountServices accountSevices;

    static {
        accountSevices = new AccountServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println(action);
        try {
            // 1. Lấy `id` của account từ request
            int accountId = Integer.parseInt(request.getParameter("id"));

            // 3. Xóa sản phẩm bằng ProductService
            boolean isDeleted = accountSevices.deleteAccountById(accountId);

            // 4. Xử lý phản hồi
            if (isDeleted) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Xóa tài khoản thành công!\"}");
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Không thể xóa sản phẩm!\"}");
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Đã xảy ra lỗi khi xóa tài khoan!\"}");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
