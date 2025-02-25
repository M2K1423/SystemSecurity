package com.example.webbongden.controller.AdminController.AccountPage;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.services.AccountServices;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "AddAccountController", value = "/add-account")
public class AddAccountController extends HttpServlet {
    private static final AccountServices accountServices = new AccountServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Đọc JSON từ body của request
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        String jsonData = jsonBuilder.toString();
        System.out.println("JSON Received: " + jsonData);

        // Parse JSON sang đối tượng Java
        Gson gson = new Gson();
        Account account = gson.fromJson(jsonData, Account.class);

//        System.out.println("Username: " + account.getUsername());
//        System.out.println("Email: " + account.getEmail());
//        System.out.println("Password: " + account.getPassword());
//        System.out.println("Role: " + account.getRole());
//        System.out.println("Customer Name: " + account.getCusName());

        boolean success = false;
        String message;

        try {
            success = accountServices.addAccount(account);

            if (success) {
                message = "Thêm tài khoản thành công!";
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                message = "Username đã tồn tại! Không thể thêm tài khoản.";
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = "Đã xảy ra lỗi khi thêm tài khoản: " + e.getMessage();
        }

        // Trả phản hồi JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"" + (success ? "success" : "error") + "\", \"message\": \"" + message + "\"}");
    }
}




