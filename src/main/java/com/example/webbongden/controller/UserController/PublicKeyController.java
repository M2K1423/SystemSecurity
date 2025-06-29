package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.PublicKey;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.PublicKeyServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@WebServlet(name = "PublicKeyController", value = "/edit-publicKey")
public class PublicKeyController extends HttpServlet {
    private final PublicKeyServices publicKeyServices = new PublicKeyServices();

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

            //Khoá công khai trống hoặc chỉ chứa khí tự khoảng trắng, tab
            if (publicKey == null || publicKey.trim().isEmpty()) {
                json = "{\"success\": false, \"message\": \"Thiếu khóa công khai.\"}";
                response.getWriter().write(json);
                return;
            }

            // Làm sạch chuỗi khóa công khai: loại bỏ phần header/footer và các khoảng trắng thừa
            String cleanedKey = publicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            Account account = (Account) session.getAttribute("account");
            if (account == null) {
                // Nếu không tìm thấy thông tin tài khoản trong session, chuyển hướng về trang login
                response.sendRedirect("/login"); // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
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

            PublicKey publicKey1 = publicKeyServices.getPublicKey(accountId);
//            if(publicKey1.getPublicKey().equals(cleanedKey)) {
//                json = "{\"success\": false, \"message\": \"Khoá công khai mới không được trùng khoá công khai cũ.\"}";
//                response.getWriter().write(json);
//                return;
//            }

            // Gọi phương thức có thể ném ra lỗi
            try {
                publicKeyServices.updatePublicKey(accountId);
                if(publicKeyServices.addPublicKey(accountId, cleanedKey)){
                    json = "{\"success\": true, \"message\": \"Cập nhật khoá công khai thành công.\"}";
                } else {
                    json = "{\"success\": false, \"message\": \"Cập nhật khoá thất bại.\"}";
                }
            } catch (GeneralSecurityException | IllegalArgumentException e) {
                // Bắt lỗi nếu khoá sai định dạng
                json = "{\"success\": false, \"message\": \"Khoá công khai không hợp lệ.\"}";
            }

            String actionType = request.getParameter("actionType");
            System.out.println("actionType: " + actionType);
            if ("leaked".equals(actionType)) {
                try {
                    publicKeyServices.leakedPrivateKey(Integer.parseInt(publicKey1.getId()));
                    System.out.println(publicKey1.getId());
                    json = "{\"success\": true, \"message\": \"Đã vô hiệu hoá các đơn chưa xử lý và cập nhập khoá mới!\"}";
                } catch (Exception e){
                    json = "{\"success\": false, \"message\": \"Xảy ra lỗi khi huỷ các đơn hàng chưa xử lý. Hãy liên hệ trực tiếp với chúng tôi để xử lý!\"}";
                }
            }

        } catch (Exception e) {
            json = "{\"success\": false, \"message\": \"Đã xảy ra lỗi khi cập nhật khoá. Thử lại sau.\"}";
        }
        response.getWriter().write(json);
    }

}