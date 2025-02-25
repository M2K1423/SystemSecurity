package com.example.webbongden.services;

import com.example.webbongden.dao.PromotionDao;
import com.example.webbongden.dao.ShippingDao;
import com.example.webbongden.dao.model.Shipping;

public class ShippingServices {
    public final ShippingDao shippingDao;

    public ShippingServices() {
        // Tự khởi tạo ProductDao
        this.shippingDao = new ShippingDao();
    }

    public void insertShipping(Shipping shipping) {
        shippingDao.insertShipping(shipping);
    }
}
