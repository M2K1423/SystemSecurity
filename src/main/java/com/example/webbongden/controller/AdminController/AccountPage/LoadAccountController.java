package com.example.webbongden.controller.AdminController.AccountPage;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoadAccountController", value = "/list-account")
public class LoadAccountController extends HttpServlet {
    private static final AccountServices accountSevices;

    static {
        accountSevices = new AccountServices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Account> accountList;

        String keyword = request.getParameter("searchValue");

        if (keyword != null && !keyword.trim().isEmpty()) {
            accountList = accountSevices.getAccountByUserName(keyword);
        }
        else {
            accountList = accountSevices.getAllAccounts();
        }

        // Thiết lập kiểu dữ liệu trả về là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Chuyển danh sách sản phẩm thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(accountList);

        // Gửi JSON về client
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
