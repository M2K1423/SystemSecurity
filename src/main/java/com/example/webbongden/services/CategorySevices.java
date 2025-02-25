package com.example.webbongden.services;

import com.example.webbongden.dao.CategoryDao;
import com.example.webbongden.dao.model.Category;
import com.example.webbongden.dao.model.SubCategory;

import java.util.List;

public class CategorySevices {
    public final CategoryDao categoryDao;

    public CategorySevices() {
        this.categoryDao = new CategoryDao();
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public List<SubCategory> getSubCategoriesByCategoryId(int categoryId) {
        return categoryDao.getSubCategoriesByCategoryId(categoryId);
    }

    public boolean deleteCategory(int categoryId) {
        // Xóa danh mục cha
        return categoryDao.deleteCategoryById(categoryId);
    }

    public boolean deleteSubCategory(int subCategoryId) {
        // Xóa danh mục con
        return categoryDao.deleteSubCategoryById(subCategoryId);
    }

    public List<SubCategory> getAllSubCategories() {
        return categoryDao.getAllSubCategories();
    }

    // Thêm danh mục cha
    public boolean addCategory(String categoryName) {
        return categoryDao.addCategory(categoryName);
    }

    // Thêm danh mục con
    public boolean addSubCategory(int categoryId, String subCategoryName) {
        return categoryDao.addSubCategory(categoryId, subCategoryName);
    }

    public String getCategoryNameById(int categoryId) {
        return categoryDao.getCategoryNameById(categoryId);
    }

    public String getSubCategoryNameById(int subCategoryId) {
        return categoryDao.getSubCategoryNameById(subCategoryId);
    }
}
