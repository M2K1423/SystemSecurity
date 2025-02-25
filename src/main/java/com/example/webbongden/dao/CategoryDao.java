package com.example.webbongden.dao;
import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Category;
import com.example.webbongden.dao.model.Product;
import com.example.webbongden.dao.model.ProductImage;
import com.example.webbongden.dao.model.SubCategory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDao {
    private final Jdbi jdbi;

    // Constructor truyền Jdbi
    public CategoryDao() {
        this.jdbi = JDBIConnect.get();
    }

    // Lấy tất cả sản phẩm

    // Lấy danh sách tất cả categories
    public List<Category> getAllCategories() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, category_name FROM categories")
                        .mapToBean(Category.class)
                        .list()
        );
    }

    // Lấy tất cả subcategories theo category_id
    public List<SubCategory> getSubCategoriesByCategoryId(int categoryId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM sub_categories WHERE category_id = :categoryId")
                        .bind("categoryId", categoryId)
                        .mapToBean(SubCategory.class)
                        .list()
        );
    }

    public List<SubCategory> getAllSubCategories() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM sub_categories")
                        .mapToBean(SubCategory.class)
                        .list()
        );
    }

    // Xóa danh mục cha
    public boolean deleteCategoryById(int categoryId) {
        return jdbi.withHandle(handle -> {
            // Kiểm tra xem danh mục con có tồn tại hay không
            List<Integer> subCategories = handle.createQuery("SELECT id FROM sub_categories WHERE category_id = :categoryId")
                    .bind("categoryId", categoryId)
                    .mapTo(Integer.class)
                    .list();
            if (!subCategories.isEmpty()) {
                throw new IllegalStateException("Không thể xóa danh mục cha vì còn danh mục con.");
            }
            return handle.createUpdate("DELETE FROM categories WHERE id = :categoryId")
                    .bind("categoryId", categoryId)
                    .execute() > 0;
        });
    }


    // Xóa danh mục con
    public boolean deleteSubCategoryById(int subCategoryId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate("DELETE FROM sub_categories WHERE id = :subCategoryId")
                        .bind("subCategoryId", subCategoryId)
                        .execute() > 0
        );
    }

    // Thêm danh mục cha
    public boolean addCategory(String categoryName) {
        return jdbi.withHandle(handle -> {
            // Kiểm tra xem danh mục cha đã tồn tại chưa
            boolean exists = handle.createQuery("SELECT COUNT(*) FROM categories WHERE category_name = :categoryName")
                    .bind("categoryName", categoryName)
                    .mapTo(int.class)
                    .findOne()
                    .orElse(0) > 0;

            if (exists) {
                throw new IllegalStateException("Danh mục cha đã tồn tại.");
            }

            // Nếu chưa tồn tại, thêm mới
            return handle.createUpdate("INSERT INTO categories (category_name) VALUES (:categoryName)")
                    .bind("categoryName", categoryName)
                    .execute() > 0;
        });
    }

    // Thêm danh mục con
    public boolean addSubCategory(int categoryId, String subCategoryName) {
        return jdbi.withHandle(handle -> {
            // Kiểm tra xem danh mục con đã tồn tại chưa
            boolean exists = handle.createQuery("SELECT COUNT(*) FROM sub_categories WHERE category_id = :categoryId AND name = :subCategoryName")
                    .bind("categoryId", categoryId)
                    .bind("subCategoryName", subCategoryName)
                    .mapTo(int.class)
                    .findOne()
                    .orElse(0) > 0;

            if (exists) {
                throw new IllegalStateException("Danh mục con đã tồn tại.");
            }

            // Nếu chưa tồn tại, thêm mới
            return handle.createUpdate("INSERT INTO sub_categories (category_id, name) VALUES (:categoryId, :name)")
                    .bind("categoryId", categoryId)
                    .bind("name", subCategoryName)
                    .execute() > 0;
        });
    }

    // Lấy tên danh mục (Category) theo ID
    public String getCategoryNameById(int categoryId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT category_name FROM categories WHERE id = :categoryId")
                        .bind("categoryId", categoryId)
                        .mapTo(String.class)
                        .findOne() // Trả về Optional<String>
                        .orElse(null) // Trả về null nếu không tìm thấy
        );
    }

    // Lấy tên danh mục con (SubCategory) theo ID
    public String getSubCategoryNameById(int subCategoryId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT name FROM sub_categories WHERE id = :subCategoryId")
                        .bind("subCategoryId", subCategoryId)
                        .mapTo(String.class)
                        .findOne() // Trả về Optional<String>
                        .orElse(null) // Trả về null nếu không tìm thấy
        );
    }

    public static void main(String[] args) {

        // Tạo đối tượng DAO
        CategoryDao categoryDao = new CategoryDao();

        // ID của danh mục cha cần xóa
        int categoryId = 7;

        try {
            // Gọi phương thức xóa danh mục cha
            boolean isDeleted = categoryDao.deleteCategoryById(categoryId);

            if (isDeleted) {
                System.out.println("Danh mục đã được xóa thành công.");
            } else {
                System.out.println("Không thể xóa danh mục.");
            }
        } catch (IllegalStateException e) {
            // Trường hợp không thể xóa do có danh mục con
            System.err.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            // Trường hợp xảy ra lỗi khác
            System.err.println("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
}

