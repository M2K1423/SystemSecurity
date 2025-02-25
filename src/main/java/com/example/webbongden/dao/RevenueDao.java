package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import org.jdbi.v3.core.Jdbi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RevenueDao {
    private final Jdbi jdbi;

    public RevenueDao() {
        this.jdbi = JDBIConnect.get();
    }

    // Thống kê doanh thu theo tháng
    public List<Map<String, Object>> getMonthlyRevenue(int year) {
        String query = "SELECT MONTH(created_at) AS month, SUM(total_price) AS revenue " +
                "FROM orders " +
                "WHERE YEAR(created_at) = :year " +
                "GROUP BY MONTH(created_at) " +
                "ORDER BY MONTH(created_at)";

        return jdbi.withHandle(handle ->
                handle.createQuery(query)
                        .bind("year", year)
                        .mapToMap()
                        .list());
    }

    // Thống kê doanh thu của 5 năm gần nhất
    public List<Map<String, Object>> getYearlyRevenueForLast5Years() {
        String query = "SELECT YEAR(created_at) AS year, SUM(total_price) AS revenue " +
                "FROM orders " +
                "WHERE YEAR(created_at) >= YEAR(CURDATE()) - 5 " +
                "GROUP BY YEAR(created_at) " +
                "ORDER BY YEAR(created_at) DESC";

        return jdbi.withHandle(handle ->
                handle.createQuery(query)
                        .mapToMap()
                        .list());
    }

    public Map<String, Double> getRevenueByPeriodInMonth() {
        String sql = "SELECT " +
                "    CONCAT(DATE_FORMAT(created_at, '%d-%m-%Y'), ' - ', DATE_FORMAT(DATE_ADD(created_at, INTERVAL 4 DAY), '%d-%m-%Y')) AS period, " +
                "    SUM(total_price) AS revenue " +
                "FROM orders " +
                "WHERE MONTH(created_at) = MONTH(CURDATE()) AND YEAR(created_at) = YEAR(CURDATE()) " +
                "GROUP BY FLOOR((DAY(created_at) - 1) / 5) " +
                "ORDER BY created_at";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToMap()
                        .reduce(new HashMap<>(), (map, row) -> {
                            map.put(row.get("period").toString(), Double.parseDouble(row.get("revenue").toString()));
                            return map;
                        })
        );
    }



    public static void main(String[] args) {
        RevenueDao revenueDao = new RevenueDao();

        // Thống kê doanh thu theo tháng cho năm cụ thể (ví dụ: năm 2024)
        int year = 2025;
        List<Map<String, Object>> monthlyRevenue = revenueDao.getMonthlyRevenue(year);

        System.out.println("Doanh thu theo tháng cho năm " + year + ":");
        for (Map<String, Object> row : monthlyRevenue) {
            int month = (int) row.get("month");
            double revenue = ((Number) row.get("revenue")).doubleValue();
            System.out.printf("Tháng %d: %.2f VND\n", month, revenue);
        }

        System.out.println();

        // Thống kê doanh thu theo năm
        List<Map<String, Object>> yearlyRevenue = revenueDao.getYearlyRevenueForLast5Years();

        System.out.println("Doanh thu theo năm:");
        for (Map<String, Object> row : yearlyRevenue) {
            int revenueYear = (int) row.get("year");
            double revenue = ((Number) row.get("revenue")).doubleValue();
            System.out.printf("Năm %d: %.2f VND\n", revenueYear, revenue);
        }
    }
}
