package com.example.webbongden.controller.AdminController.CustomerPage;

import com.example.webbongden.dao.model.User;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.UserSevices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoadCusController", value = "/list-customer")
public class LoadCusController extends HttpServlet {
    private static final UserSevices userSevices;

    static {
        userSevices = new UserSevices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList;

        String keyword = request.getParameter("searchValue");

        if (keyword != null && !keyword.trim().isEmpty()) {
            userList = userSevices.searchCustomerByName(keyword);
        }
        else {
            userList = userSevices.getAllUsers();
        }


    // Thiết lập kiểu dữ liệu trả về là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

    // Chuyển danh sách sản phẩm thành JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(userList);

    // Gửi JSON về client
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
