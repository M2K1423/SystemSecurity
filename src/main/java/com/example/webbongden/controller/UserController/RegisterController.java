package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.ReviewService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "RegisterController", value = "/register")
public class RegisterController extends HttpServlet {
    private final AccountServices accountServices = new AccountServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Chuyển hướng tới trang đăng ký
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Đọc dữ liệu JSON từ request
            BufferedReader reader = request.getReader();
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // Parse JSON thành đối tượng
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> formData = objectMapper.readValue(json.toString(), new TypeReference<>() {});

            // Lấy thông tin từ formData
            String cusname = formData.get("cusname");
            String username = formData.get("username");
            String email = formData.get("email");
            String password = formData.get("password");
            String rePassword = formData.get("rePassword");

            // Kiểm tra mật khẩu nhập lại
            if (!password.equals(rePassword)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\":false,\"message\":\"Mật khẩu không khớp!\"}");
                return;
            }

            // Tạo tài khoản mới
            Account account = new Account();
            account.setCusName(cusname);
            account.setUsername(username);
            account.setEmail(email);
            account.setPassword(password); // Mật khẩu plaintext sẽ được hash trong service

            // Đăng ký tài khoản qua service
            boolean isRegistered = accountServices.registerAccount(account);

            if (isRegistered) {
                response.getWriter().write("{\"success\":true,\"message\":\"Đăng ký thành công!\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"Tên tài khoản đã tồn tại!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"Đã xảy ra lỗi trong quá trình xử lý!\"}");
        }
    }
}
