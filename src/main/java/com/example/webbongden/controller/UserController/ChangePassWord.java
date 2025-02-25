package com.example.webbongden.controller.UserController;

import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ChangePassWord", value = "/changePassword")
public class ChangePassWord extends HttpServlet {
    private final AccountServices accountServices = new AccountServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Lấy thông tin từ form
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Lấy username từ session
        String username = (String) session.getAttribute("username");

        // Kiểm tra username
        if (username == null) {
            request.setAttribute("errorMessage", "Bạn chưa đăng nhập.");
            request.getRequestDispatcher("/user/userinfo.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu khớp nhau
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
            request.getRequestDispatcher("/user/userinfo.jsp").forward(request, response);
            return;
        }

        // Thực hiện đổi mật khẩu
        boolean isPasswordChanged = accountServices.changePassword(username, oldPassword, newPassword);

        // Xử lý kết quả
        if (isPasswordChanged) {
            session.setAttribute("successMessage", "Đổi mật khẩu thành công.");
            request.getRequestDispatcher("/user/userinfo.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Mật khẩu cũ không chính xác.");
            request.getRequestDispatcher("/user/userinfo.jsp").forward(request, response);
        }
    }
}
