package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.model.ProductDetail;
import com.example.webbongden.dao.model.ProductImage;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AdminAddProductController", value = "/add-product")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class AddProductController extends HttpServlet {
    private final ProductServices productServices = new ProductServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng đến form thêm sản phẩm
        request.getRequestDispatcher("admin/product-management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Các thông tin sản phẩm
        String productName = request.getParameter("productName");
        String subCategoryName = request.getParameter("categoryName");
        double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
        String productStatus = request.getParameter("productStatus");
        String description = request.getParameter("description");
        String warrantyPeriod = request.getParameter("warrantyPeriod");
        String lightColor = request.getParameter("lightColor");
        String material = request.getParameter("material");
        String voltage = request.getParameter("voltage");
        String usageAge = request.getParameter("usageAge");
        double discountPercent = Double.parseDouble(request.getParameter("discountPercent"));

        ProductDetail product = new ProductDetail();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setStockQuantity(stockQuantity);
        product.setProductStatus(productStatus);
        product.setDescription(description);
        product.setWarrantyPeriod(warrantyPeriod);
        product.setLightColor(lightColor);
        product.setMaterial(material);
        product.setVoltage(voltage);
        product.setUsageAge(usageAge);
        product.setDiscountPercent(discountPercent);
        product.setCreatedAt(new Date());

        // Đường dẫn lưu ảnh
        String uploadPath = "D:/Nam3/LTWEB/WebDemo/WebBongDen/src/main/webapp/assets/images";
        System.out.println("Đường dẫn uploadPath: " + uploadPath);
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean isCreated = uploadDir.mkdirs();
            if (isCreated) {
                System.out.println("Tạo thư mục thành công: " + uploadPath);
            } else {
                System.err.println("Lỗi: Không thể tạo thư mục: " + uploadPath);
            }
        }

        // Xử lý các file ảnh tải lên
        List<ProductImage> images = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getName().equals("productImages") && part.getSize() > 0) {
                // Lấy tên file
                String fileName = extractFileName(part);

                // Tạo đường dẫn đầy đủ
                String filePath = uploadPath + File.separator + fileName;

                // Lưu file lên server
                part.write(filePath);

                // Thêm vào danh sách hình ảnh
                ProductImage image = new ProductImage();
                image.setUrl("assets/images/" + fileName); // Đường dẫn tương đối
                image.setMainImage(images.isEmpty()); // Ảnh đầu tiên là ảnh chính
                images.add(image);
            }
        }
        product.setListImages(images);

        // Gọi service để lưu sản phẩm vào DB
        boolean isAdded = productServices.addProduct(product, subCategoryName);

        // Xử lý phản hồi
        if (isAdded) {
            request.setAttribute("message", "Thêm sản phẩm thành công!");
        } else {
            request.setAttribute("error", "Thêm sản phẩm thất bại!");
        }

        request.getRequestDispatcher("admin/product-management.jsp").forward(request, response);
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String content : contentDisp.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
}

