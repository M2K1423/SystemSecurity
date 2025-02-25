package com.example.webbongden.services;

import com.example.webbongden.dao.ProductDao;
import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.*;

import java.util.Comparator;
import java.util.List;

public class ProductServices {
    public final ProductDao productDao;

    public ProductServices() {
        // Tự khởi tạo ProductDao
        this.productDao = new ProductDao();
    }

    public List<Product> getAllProduct() {
        return productDao.getAllProduct();
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDao.getProductsByCategory(categoryId);
    }

    public List<Product> getProductsByCategory2(int categoryId) {
        return productDao.getProductsByCategory2(categoryId);
    }

    public List<Product> getListProductForAdminPage() {
        return productDao.getProductsForAdminPage();
    }

    public int getTotalProducts() {
        return productDao.getTotalProducts();
    }

    public int getCategoryQuantity() {
        return productDao.getCategoryQuantity();
    }

    public int getOutOfStockProducts() {
        return productDao.getOutOfStockProducts();
    }

    public int getNewProductsInLast7Days() {
        return productDao.getNewProductsInLast7Days();
    }

    public ProductDetail getProductDetailById(int productId) {
        return productDao.getProductDetailById(productId);
    }

    public boolean addProduct(ProductDetail product, String subCategoryName) {
        // Xử lý logic liên quan đến hình ảnh
        for (ProductImage image : product.getListImages()) {
            // Kiểm tra URL có hợp lệ không
            if (image.getUrl() == null || image.getUrl().isEmpty()) {
                System.err.println("Lỗi: URL hình ảnh không hợp lệ!");
                return false;
            }
        }

        try {
            return productDao.addProduct(product, subCategoryName);
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteProduct(int productId) {
        return productDao.deleteProductById(productId);
    }
    public List<Product> getProductsByKeyword(String a) {
        return productDao.getProductsByKeyword(a);
    }

    public List<TopProduct> getTopProducts() {
        return productDao.getTopSellingProducts();
    }

    public List<Product> getProductsBySubCategory(int subCategoryId) {
        return productDao.getProductsBySubCategory(subCategoryId); // Gọi hàm với ID của danh mục con
    }

    public boolean editProductDetail(ProductDetail productDetail) {
        return productDao.editProductDetail(productDetail);
    }

    public Product getProductById(int productId) {
        return productDao.getProductById(productId);
    }

    //Kiểm tra sp còn hàng k
    public boolean isProductInStock(int productId) {
        return productDao.isProductInStock(productId);
    }

    public List<Product> getProductsByPage(int page, int pageSize) {
        return productDao.getProductsByPage(page, pageSize);
    }

    public List<Product> searchProductByName(String productName) {
        return productDao.searchProductsByName(productName);
    }

    public void decreaseStockQuantity(int productId, int quantity) {
        productDao.decreaseStockQuantity(productId, quantity);
    }

    public List<Product> getListProductByPromotion(int promotionId) {
        return productDao.getProductsByPromotion(promotionId);
    }

    public List<String> getAllProductUrls(int productId) {
        return productDao.getAllProductUrls(productId);
    }

    public List<Product> getBestSellingProducts() {
        return  productDao.getBestSellingProducts();
    }

    public String getCategoryNameByProductId(int productId) {
        return productDao.getCategoryNameByProductId(productId);
    }

    public List<Product> fetchRelatedProducts(int productId) {
        return productDao.getRelatedProducts(productId);
    }
}
