package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.model.ProductDetail;
import com.example.webbongden.dao.model.ProductImage;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "EditProductController", value ="/edit-product-detail")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class EditProductController extends HttpServlet {
    private static final ProductServices productServices;

    static {
        productServices = new ProductServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        try {
            // Lấy các tham số từ form
            int id = Integer.parseInt(request.getParameter("id"));
            String productName = request.getParameter("productName");
            double unitPrice = Double.parseDouble(request.getParameter("unitPrice"));
            int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
            String productStatus = request.getParameter("productStatus");
            double rating = Double.parseDouble(request.getParameter("rating"));
            String description = request.getParameter("description");
            String warrantyPeriod = request.getParameter("warrantyPeriod");
            String lightColor = request.getParameter("lightColor");
            String material = request.getParameter("material");
            String voltage = request.getParameter("voltage");
            String usageAge = request.getParameter("usageAge");
            double discountPercent = Double.parseDouble(request.getParameter("discountPercent"));
            int subCategoryId = Integer.parseInt(request.getParameter("subCategoryId"));



            // Đường dẫn thư mục upload file
            String uploadPath = "D:/Nam3/LTWEB/WebDemo/WebBongDen/src/main/webapp/assets/images";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean isCreated = uploadDir.mkdirs();
                if (!isCreated) {
                    throw new IOException("Không thể tạo thư mục upload: " + uploadPath);
                }
            }

            // Xử lý upload nhiều ảnh
            List<ProductImage> productImages = new ArrayList<>();
            boolean isMainImageSet = false; // Đánh dấu ảnh chính
            for (Part part : request.getParts()) {
                if (part.getName().equals("imageFiles") && part.getSize() > 0) {
                    String fileName = extractFileName(part);
                    String filePath = uploadPath + File.separator + fileName;
                    part.write(filePath); // Lưu file lên server

                    // Tạo đối tượng ProductImage
                    ProductImage productImage = new ProductImage();
                    productImage.setUrl("assets/images/" + fileName);
                    productImage.setMainImage(!isMainImageSet); // Đánh dấu ảnh đầu tiên là ảnh chính
                    isMainImageSet = true;

                    productImages.add(productImage);
                }
            }

            System.out.println("Danh sách ảnh sau khi upload:");
            for (ProductImage image : productImages) {
                System.out.println("URL: " + image.getUrl() + ", Main: " + image.isMainImage());
            }


            // Tạo đối tượng ProductDetail từ dữ liệu form
            ProductDetail productDetail = new ProductDetail();
            productDetail.setId(id);
            productDetail.setProductName(productName);
            productDetail.setUnitPrice(unitPrice);
            productDetail.setStockQuantity(stockQuantity);
            productDetail.setProductStatus(productStatus);
            productDetail.setRating(rating);
            productDetail.setDescription(description);
            productDetail.setWarrantyPeriod(warrantyPeriod);
            productDetail.setLightColor(lightColor);
            productDetail.setMaterial(material);
            productDetail.setVoltage(voltage);
            productDetail.setUsageAge(usageAge);
            productDetail.setDiscountPercent(discountPercent);
            productDetail.setSubCategoryId(subCategoryId);
            productDetail.setListImages(productImages);

            // Cập nhật sản phẩm trong cơ sở dữ liệu
            boolean isUpdated = productServices.editProductDetail(productDetail);

            if (isUpdated) {
                response.getWriter().write("{\"success\": true, \"message\": \"Cập nhật sản phẩm thành công!\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể cập nhật sản phẩm!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra trong quá trình cập nhật!\"}");
        }
    }

    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
}
