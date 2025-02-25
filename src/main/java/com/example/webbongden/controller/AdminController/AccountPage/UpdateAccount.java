package com.example.webbongden.controller.AdminController.AccountPage;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.services.AccountServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "UpdateAccount", value = "/update-account")
public class UpdateAccount extends HttpServlet {
    private static final AccountServices accountSevices;

    static {
        accountSevices = new AccountServices();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Lấy ID tài khoản từ tham số request
            String accountIdParam = request.getParameter("id");
            if (accountIdParam == null || accountIdParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"message\":\"ID tài khoản không hợp lệ.\"}");
                return;
            }

            int accountId = Integer.parseInt(accountIdParam);

            // Gọi phương thức getAccountById từ AccountServices
            Account account = accountSevices.getAccountById(accountId);

            if (account == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"message\":\"Không tìm thấy tài khoản với ID được cung cấp.\"}");
                return;
            }

            // Trả về JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(account);
            response.getWriter().write(jsonResponse);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"ID tài khoản phải là số.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"Đã xảy ra lỗi trong quá trình xử lý.\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Đọc dữ liệu JSON từ request body
            StringBuilder json = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // Parse JSON thành đối tượng Account
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(json.toString(), Account.class);

            // Cập nhật tài khoản
            boolean isUpdated = accountSevices.updateAccount(account);

            if (isUpdated) {
                response.getWriter().write("{\"success\":true, \"message\":\"Cập nhật thành công!\"}");
            } else {
                response.getWriter().write("{\"success\":false, \"message\":\"Cập nhật thất bại!\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false, \"message\":\"Lỗi: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}
