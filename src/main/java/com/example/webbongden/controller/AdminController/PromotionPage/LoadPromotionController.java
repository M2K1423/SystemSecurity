package com.example.webbongden.controller.AdminController.PromotionPage;

import com.example.webbongden.dao.model.Promotion;
import com.example.webbongden.services.AccountServices;
import com.example.webbongden.services.PromotionService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoadPromotionController", value = "/list-promotion")
public class LoadPromotionController extends HttpServlet {
    private static final PromotionService promotionServices;

    static {
        promotionServices = new PromotionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Promotion> listPromotion;

        listPromotion = promotionServices.getAllPromotions();
        // Thiết lập kiểu dữ liệu trả về là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Chuyển danh sách sản phẩm thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(listPromotion);

        // Gửi JSON về client
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
