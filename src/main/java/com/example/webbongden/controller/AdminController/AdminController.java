package com.example.webbongden.controller.AdminController;

import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.TopProduct;
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

@WebServlet(name = "AdminController", value = "/admin")
public class AdminController extends HttpServlet {
    private static final ProductServices productServices;
    private static final OrderSevices orderServices;
    private static final UserSevices userServices;
    private static final RevenueServices revenueServices;

    static {
        productServices = new ProductServices();
        orderServices = new OrderSevices();
        userServices = new UserSevices();
        revenueServices = new RevenueServices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("page");

        // Điều hướng đến trang tương ứng
        if (page == null || page.equals("dashboard")) {
            loadTopProduct(request, response);
            loadStats(request, response);
            loadOrdersInLastMonth(request, response);
            loadUserChart(request, response);
            loadRevenueChart(request, response);
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } else if (page.equals("product-management")) {
            loadProducStats(request, response);
            request.getRequestDispatcher("/admin/product-management.jsp").forward(request, response);
        } else if (page.equals("customer-management")) {
            request.getRequestDispatcher("/admin/customer-management.jsp").forward(request, response);
        } else if (page.equals("order-management")) {
            loadOrderStats(request, response);
            request.getRequestDispatcher("/admin/order-management.jsp").forward(request, response);
        } else if (page.equals("revenue-statistics")) {
            request.getRequestDispatcher("/admin/thongke.jsp").forward(request, response);
        } else if (page.equals("promotion")) {
            request.getRequestDispatcher("/admin/promotion-management.jsp").forward(request, response);
        } else if (page.equals("account")) {
            request.getRequestDispatcher("/admin/account-management.jsp").forward(request, response);
        } else {
            // Trang mặc định nếu `page` không khớp
            response.sendRedirect("admin?page=dashboard");
        }
    }

    // Trang DashBoard


    public void loadUserChart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ Service
        Map<String, Integer> userChart = userServices.getCustomerTypes();

        // Chuyển Map thành JSON String
        String userChartJson = new Gson().toJson(userChart);

        // Đưa JSON vào request attribute
        request.setAttribute("customerPieChart", userChartJson);
    }

    public void loadRevenueChart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ Service
        Map<String, Double> revenueData = revenueServices.getRevenueByPeriodInMonth();

        // Chuyển Map thành JSON String
        String revenueDataJson = new Gson().toJson(revenueData);

        // Đưa JSON vào request attribute
        request.setAttribute("revenueChartData", revenueDataJson);
    }




    public void loadTopProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TopProduct> topProducts = productServices.getTopProducts();
        // Đưa dữ liệu vào request attribute
        request.setAttribute("topProducts", topProducts);
    }

    public void loadStats(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalOrders = orderServices.getTotalOrders();
        double totalRevenue = orderServices.getTotalRevenue();
        int totalUser = userServices.getTotalUser();
        request.setAttribute("totalUser", totalUser);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalRevenue", totalRevenue);
    }

    public void loadOrdersInLastMonth(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> orders = orderServices.getOrdersInLastMonth();
        request.setAttribute("orderLastMonth", orders);
    }



    //Trang product
    public void loadProducStats(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalProducts = productServices.getTotalProducts();
        int categoryQuantity = productServices.getCategoryQuantity();
        int outOfStockProducts = productServices.getOutOfStockProducts();
        int newProductsInLast7Days = productServices.getNewProductsInLast7Days();

        // Đưa dữ liệu vào request để truyền sang view
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("categoryQuantity", categoryQuantity);
        request.setAttribute("outOfStockProducts", outOfStockProducts);
        request.setAttribute("newProductsInLast7Days", newProductsInLast7Days);
    }



    //Trang order
    public void loadOrderStats(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalOrders = orderServices.getTotalOrders();
        int totalOrdersPending = orderServices.getPendingOrders();
        int totalOrdersShipping = orderServices.getShippingOrders();

        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalOrdersPending", totalOrdersPending);
        request.setAttribute("totalOrdersShipping", totalOrdersShipping);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
