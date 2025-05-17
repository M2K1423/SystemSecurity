package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.User;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.PublicKeyServices;
import com.example.webbongden.services.UserSevices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoginControllerr", value = "/login")
public class LoginControllerr extends HttpServlet {
    private final AccountServices  accountService = new AccountServices();
    private final UserSevices userSevices = new UserSevices();
    private final OrderSevices orderSevices = new OrderSevices();
    private final PublicKeyServices publicKeyServices = new PublicKeyServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Xác thực tài khoản
        Account account = accountService.authenticate(username, password);
        User user = userSevices.getBasicInfoByUsername(username);
        List<Order> orders = orderSevices.getOrdersByUsername(username);
        String publicKey = publicKeyServices.getPublicKey(Integer.parseInt(user.getCustomerId()));
        if (account != null) {
            // Lưu thông tin tài khoản vào session
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            session.setAttribute("username", account.getUsername());
            session.setAttribute("role", account.getRole());
            session.setAttribute("userInfo", user);
            session.setAttribute("orders", orders);
            session.setAttribute("publicKey", publicKey);

            // Phân quyền và chuyển hướng
            if ("admin".equals(account.getRole())) {
                response.sendRedirect("admin?page=dashboard-management"); // Chuyển đến trang admin
            } else {
                response.sendRedirect("home"); // Chuyển đến trang user
            }
        } else {
            // Trả về lỗi nếu đăng nhập thất bại
            request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
    }
}
