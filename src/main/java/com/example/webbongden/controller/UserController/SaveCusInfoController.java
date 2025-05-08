package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Account;
import com.example.webbongden.dao.model.Customer;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "SaveCusInfoController", value = "/save-cus-info")
public class SaveCusInfoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String provinceCode = request.getParameter("province");
        String districtCode = request.getParameter("district");
        String wardCode = request.getParameter("ward");
        String streetAddress = request.getParameter("streetAddress");
        String note = request.getParameter("note");

        String customerAddress = streetAddress + ", " + wardCode + ", " + districtCode + ", " + provinceCode;
         // Lấy ghi chú nếu có

        // Lưu thông tin khách hàng vào session

        Customer customerInfo = new Customer();
        customerInfo.setId(account.getId());
        customerInfo.setCusName(customerName);
        customerInfo.setPhone(customerPhone);
        customerInfo.setAddress(customerAddress);
        customerInfo.setNote(note);

        session.setAttribute("customerInfo", customerInfo);

        // Quay lại trang cart.jsp và hiển thị Tab 3 (Thanh toán)
        response.sendRedirect("/SystemSecurity_war/cart#payment");
    }
}
