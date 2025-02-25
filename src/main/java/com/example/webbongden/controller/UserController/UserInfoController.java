package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UserInfoController", value = "/userinfo")
public class UserInfoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy session hiện tại (nếu có)
        HttpSession session = request.getSession(false); // false để không tạo session mới nếu chưa có

        if (session != null) {
            // Lấy thông tin tài khoản từ session
            Account account = (Account) session.getAttribute("account");

            if (account != null) {
                // Thêm thông tin tài khoản vào request để truyền xuống JSP
                request.setAttribute("account", account);
            } else {
                // Nếu không tìm thấy thông tin tài khoản trong session, chuyển hướng về trang login
                response.sendRedirect("/login"); // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
                return;
            }
        } else {
            // Nếu không có session (người dùng chưa đăng nhập), chuyển hướng về trang login
            response.sendRedirect("/login");
            return;
        }

        // Chuyển tiếp đến trang userinfo.jsp
        request.getRequestDispatcher("/user/userinfo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
