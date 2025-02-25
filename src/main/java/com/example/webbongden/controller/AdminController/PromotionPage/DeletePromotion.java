package com.example.webbongden.controller.AdminController.PromotionPage;

import com.example.webbongden.services.PromotionService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "DeletePromotion", value = "/delete-promotion")
public class DeletePromotion extends HttpServlet {
    private static final PromotionService promotionService = new PromotionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Đọc dữ liệu JSON từ request body
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Integer> requestData = objectMapper.readValue(request.getReader(), Map.class);

            // Lấy promotionId từ dữ liệu JSON
            int promotionId = requestData.get("promotionId");

            // Gọi service để xóa chương trình khuyến mãi
            boolean isDeleted = promotionService.deletePromotionById(promotionId);

            // Trả về phản hồi JSON
            Map<String, Object> responseData = new HashMap<>();
            if (isDeleted) {
                responseData.put("success", true);
                responseData.put("message", "Chương trình khuyến mãi đã được xóa thành công.");
            } else {
                responseData.put("success", false);
                responseData.put("message", "Không thể xóa chương trình khuyến mãi.");
            }
            response.getWriter().write(objectMapper.writeValueAsString(responseData));
        } catch (Exception e) {
            // Xử lý lỗi và trả về JSON lỗi
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Đã xảy ra lỗi trong quá trình xử lý.");
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
            e.printStackTrace();
        }
    }
}
