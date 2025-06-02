package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserInfoController", value = "/userinfo")
public class UserInfoController extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy session hiện tại (nếu có)
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Lấy thông tin tài khoản từ session
            Account account = (Account) session.getAttribute("account");
            String publicKey = (String) session.getAttribute("publicKey");

            if (account != null) {
                // Thêm thông tin tài khoản vào request để truyền xuống JSP
                request.setAttribute("account", account);
                // Thêm khoá công khai vào request để truyền xuống JSP
                request.setAttribute("publicKey", publicKey);

                // Lấy danh sách đơn hàng từ DB trực tiếp qua OrderDao
                List<Order> orders = orderDao.getListOrders();

                // Truyền danh sách đơn hàng vào request để JSP hiển thị
                request.setAttribute("orders", orders);
            } else {
                // Nếu không tìm thấy thông tin tài khoản trong session, chuyển hướng về trang login
                response.sendRedirect("/login");
                return;
            }
        } else {
            // Nếu không có session, chuyển hướng về trang login
            response.sendRedirect("/login");
            return;
        }

        // Chuyển tiếp đến trang userinfo.jsp
        request.getRequestDispatcher("/user/userinfo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Nếu có xử lý post thì thêm ở đây
    }
}
