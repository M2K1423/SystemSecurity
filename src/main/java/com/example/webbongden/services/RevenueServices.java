package com.example.webbongden.services;
import com.example.webbongden.dao.RevenueDao;

import java.util.List;
import java.util.Map;

public class RevenueServices {
    public final RevenueDao revenueDao;

    public RevenueServices() {
        // Tự khởi tạo ProductDao
        this.revenueDao = new RevenueDao();
    }

    public List<Map<String, Object>> getMonthlyRevenue(int year) {
        return revenueDao.getMonthlyRevenue(year);
    }

    public List<Map<String, Object>> getYearlyRevenue() {
        return revenueDao.getYearlyRevenueForLast5Years();
    }

    public Map<String, Double> getRevenueByPeriodInMonth() {
        return revenueDao.getRevenueByPeriodInMonth();
    }
}
