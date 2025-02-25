package com.example.webbongden.services;

import com.example.webbongden.dao.ProductDao;
import com.example.webbongden.dao.PromotionDao;
import com.example.webbongden.dao.model.Product;
import com.example.webbongden.dao.model.Promotion;

import java.util.List;

public class PromotionService {
    public final PromotionDao promotionDao;

    public PromotionService() {
        // Tự khởi tạo ProductDao
        this.promotionDao = new PromotionDao();
    }

    public Promotion getPromotionById(int id) {
        return promotionDao.getPromotionByProductId(id);
    }

    public boolean addPromotion(Promotion promotion) {
        return promotionDao.addPromotion(promotion);
    }

    public List<Promotion> getAllPromotions() {
        return promotionDao.getPromotionsWithoutProduct();
    }

    public boolean addProductToPromotion(int productId, int promotionId) {
        return promotionDao.addProductToPromotion(productId, promotionId);
    }

    public List<Product> getProductsByPromotionId(int promotionId) {
        return promotionDao.getProductsByPromotionId(promotionId);
    }

    public boolean deleteProductFromPromotion(int promotionId, int productId) {
        return promotionDao.deleteProductFromPromotion(promotionId, productId);
    }

    public boolean deletePromotionById(int promotionId) {
        return promotionDao.deletePromotionById(promotionId);
    }

    public List<Promotion> getAllPromotionsWithProducts() {
        return promotionDao.getAllPromotionsWithProducts();
    }

}
