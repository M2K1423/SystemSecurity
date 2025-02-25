package com.example.webbongden.controller.UserController;

import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ResetPasswordController", value = "/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final AccountServices accountServices = new AccountServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("reset-email");
        System.out.println("Email: " + email);

        // Kiểm tra email có tồn tại trong hệ thống không
        if (email == null || email.isEmpty()) {
            request.setAttribute("errorMessageReset", "Email không hợp lệ.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response); // Chuyển hướng về form reset password
            return;
        }

        if (!accountServices.checkEmailExists(email)) {
            // Trả về thông báo lỗi nếu email không tồn tại
            request.setAttribute("errorMessageReset", "Email không tồn tại.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response); // Chuyển hướng về form reset password
            return;
        }

        // Tạo mật khẩu tạm thời
        String temporaryPassword = accountServices.generateTemporaryPassword();

        // Cập nhật mật khẩu tạm thời vào cơ sở dữ liệu
        boolean isPasswordUpdated = accountServices.updatePassword(email, temporaryPassword);
        if (isPasswordUpdated) {
            // Gửi email chứa mật khẩu tạm thời
            boolean isEmailSent = accountServices.sendTemporaryPasswordEmail(email, temporaryPassword);
            if (isEmailSent) {
                request.setAttribute("successMessageReset", "Mật khẩu tạm thời đã được gửi đến email của bạn.");
            } else {
                request.setAttribute("errorMessageReset", "Có lỗi xảy ra khi gửi email.");
            }
        } else {
            request.setAttribute("errorMessageReset", "Không thể cập nhật mật khẩu.");
        }

        // Chuyển hướng lại về trang reset-password.jsp với thông báo
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }
}
