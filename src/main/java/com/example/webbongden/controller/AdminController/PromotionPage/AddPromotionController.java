package com.example.webbongden.controller.AdminController.PromotionPage;

import com.example.webbongden.dao.model.Promotion;
import com.example.webbongden.services.PromotionService;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "AddPromotionController", value = "/add-promotion")
public class AddPromotionController extends HttpServlet {
    private static final PromotionService promotionService = new PromotionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
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
            Promotion promotion = gson.fromJson(jsonData, Promotion.class);

            // Xử lý logic thêm promotion
            boolean success = promotionService.addPromotion(promotion);

            String message = success
                    ? "Thêm chương trình khuyến mãi thành công!"
                    : "Không thể thêm chương trình khuyến mãi.";
            response.getWriter().write("{\"status\":\"success\",\"message\":\"" + message + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Đã xảy ra lỗi.\"}");
        }
    }
}
