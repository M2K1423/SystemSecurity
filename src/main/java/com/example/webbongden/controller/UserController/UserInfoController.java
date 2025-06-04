package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.utils.CheckOrder;
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
        HttpSession session = request.getSession(false);

        if (session != null) {
            Account account = (Account) session.getAttribute("account");
            String publicKey = (String) session.getAttribute("publicKey");

            if (account != null) {
                request.setAttribute("account", account);
                request.setAttribute("publicKey", publicKey);

                List<Order> orders = orderDao.getListOrders();

                // Với mỗi order, kiểm tra chữ ký hợp lệ
                for (Order order : orders) {
                    try {
                        boolean valid = CheckOrder.checkOrder(order);
                        order.setValid(valid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        order.setValid(false);
                    }
                }

                request.setAttribute("orders", orders);
            } else {
                response.sendRedirect("/login");
                return;
            }
        } else {
            response.sendRedirect("/login");
            return;
        }

        request.getRequestDispatcher("/user/userinfo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý POST nếu cần
    }
}
