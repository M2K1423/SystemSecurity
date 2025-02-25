package com.example.webbongden.controller.AdminController.RevenuePage;

import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.services.RevenueServices;
import com.example.webbongden.services.UserSevices;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RevenueController", value = "/revenue")
public class RevenueController extends HttpServlet {
    private static final RevenueServices revenueServices;
    private final Gson gson = new Gson();

    static {
        revenueServices = new RevenueServices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String statisticType = request.getParameter("statisticType");
        String yearParam = request.getParameter("year");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            if ("monthly".equals(statisticType)) {
                // Thống kê theo tháng
                int year = Integer.parseInt(yearParam);
                List<Map<String, Object>> monthlyRevenue = revenueServices.getMonthlyRevenue(year);
                response.getWriter().write(gson.toJson(monthlyRevenue));
            } else if ("yearly".equals(statisticType)) {
                // Thống kê theo năm
                List<Map<String, Object>> yearlyRevenue = revenueServices.getYearlyRevenue();
                response.getWriter().write(gson.toJson(yearlyRevenue));
            } else {
                response.getWriter().write(gson.toJson(Map.of("error", "Invalid statistic type")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "Error processing request")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
