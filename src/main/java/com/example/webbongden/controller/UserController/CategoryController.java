package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.ProductDao;
import com.example.webbongden.dao.model.Product;
import com.example.webbongden.services.CategorySevices;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "CategoryController", value = "/CategoryController")
public class CategoryController extends HttpServlet {
    private static final ProductServices productServices;
    private static final CategorySevices categoryServices;

    static {
        categoryServices = new CategorySevices();
        productServices = new ProductServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageParam = request.getParameter("page");
        String select = request.getParameter("select");
        String subCategoryIdParam = request.getParameter("subCategoryId");
        String categoryIdParam = request.getParameter("categoryId");

        int page = 1;
        int pageSize = 16; // Số sản phẩm mỗi trang

        // Xử lý tham số page
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1; // Trang mặc định nếu không hợp lệ
            }
        }

        List<Product> products;
        String categoryName = null;
        String subCategoryName = null;

        // Kiểm tra subCategoryId
        if (subCategoryIdParam != null && !subCategoryIdParam.trim().isEmpty()) {
            int subCategoryId = Integer.parseInt(subCategoryIdParam.trim());
            int categoryId = Integer.parseInt(categoryIdParam.trim());
            products = productServices.getProductsBySubCategory(subCategoryId);
            subCategoryName = categoryServices.getSubCategoryNameById(subCategoryId); // Lấy tên danh mục con
            categoryName = categoryServices.getCategoryNameById(categoryId);
        }else if(categoryIdParam != null) {
            int categoryId = Integer.parseInt(categoryIdParam.trim());
            products = productServices.getProductsByCategory2(categoryId);
            categoryName = categoryServices.getCategoryNameById(categoryId);
        }else {
            products = productServices.getAllProduct();

        }

        // Xử lý sắp xếp nếu có tham số select
        if (select != null) {
            switch (select) {
                case "price_desc":
                    products.sort(Comparator.comparingDouble(Product::getUnitPrice).reversed());
                    break;
                case "price_asc":
                    products.sort(Comparator.comparingDouble(Product::getUnitPrice));
                    break;
                case "name_desc":
                    products.sort(Comparator.comparing(Product::getProductName));
                    break;
                case "name_asc":
                    products.sort(Comparator.comparing(Product::getProductName).reversed());
                    break;
                default:
                    break;
            }
        }

        // Phân trang
        int totalProducts = products.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalProducts);
        List<Product> paginatedProducts = products.subList(fromIndex, toIndex);

        // Gửi các thuộc tính đến JSP
        request.setAttribute("products", paginatedProducts);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("select", select);
        request.setAttribute("subCategoryId", subCategoryIdParam);
        request.setAttribute("categoryId", categoryIdParam);
        request.setAttribute("categoryName", categoryName);
        request.setAttribute("subCategoryName", subCategoryName);

        request.getRequestDispatcher("/user/category.jsp").forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
