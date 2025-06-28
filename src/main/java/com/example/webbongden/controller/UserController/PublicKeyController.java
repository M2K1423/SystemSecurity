package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.PublicKeyServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@WebServlet(name = "PublicKeyController", value = "/edit-publicKey")
public class PublicKeyController extends HttpServlet {
    private final PublicKeyServices publicKeyServices = new PublicKeyServices();
    private final OrderSevices orderServices = new OrderSevices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy session hiện tại (nếu có)
        HttpSession session = request.getSession(false); // false để không tạo session mới nếu chưa có

        if (session != null) {
            // Lấy thông tin tài khoản từ session
            Account account = (Account) session.getAttribute("account");
            int id = account.getId();
            session.setAttribute("publicKey", publicKeyServices.getPublicKey(id).getPublicKey());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String json;

        try {
            String publicKey = request.getParameter("publicKey");
            String authPassword = request.getParameter("authPassword");

            if (publicKey == null || publicKey.trim().isEmpty()) {
                json = "{\"success\": false, \"message\": \"Thiếu khóa công khai.\"}";
                response.getWriter().write(json);
                return;
            }

            String cleanedKey = publicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            Account account = (Account) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("/login");
                return;
            }

            String password = account.getPassword();
            int accountId = account.getId();
            AccountServices accountServices = new AccountServices();
            if (!accountServices.checkPassword(authPassword, password)) {
                json = "{\"success\": false, \"message\": \"Mật khẩu không đúng.\"}";
                response.getWriter().write(json);
                return;
            }

            try {
                publicKeyServices.updatePublicKey(accountId);
                if(publicKeyServices.addPublicKey(accountId, cleanedKey)){
                    // ------ Thêm dòng này để reset trạng thái ký ------
                    orderServices.resetAllSignatureOfAccount(accountId);
                    // ---------------------------------------------------
                    json = "{\"success\": true, \"message\": \"Cập nhật khoá công khai thành công.\"}";
                } else {
                    json = "{\"success\": false, \"message\": \"Cập nhật khoá thất bại.\"}";
                }
            } catch (GeneralSecurityException | IllegalArgumentException e) {
                json = "{\"success\": false, \"message\": \"Khoá công khai không hợp lệ.\"}";
            }

        } catch (Exception e) {
            json = "{\"success\": false, \"message\": \"Đã xảy ra lỗi khi cập nhật khoá.\"}";
        }
        response.getWriter().write(json);
    }

}