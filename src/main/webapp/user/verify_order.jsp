<%--
  Created by IntelliJ IDEA.
  User: PHAN PHAT
  Date: 5/7/2025
  Time: 4:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webbongden.dao.model.Order" %>

<%
    Order order = (Order) request.getAttribute("order");
    Boolean isValid = (Boolean) request.getAttribute("signatureValid");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xác minh chữ ký đơn hàng</title>
    <style>
        body { font-family: Arial; padding: 20px; }
        .success { color: green; }
        .error { color: red; }
    </style>
</head>
<body>
<h2>Kết quả xác minh chữ ký số</h2>

<% if (error != null) { %>
<p class="error"><%= error %></p>
<% } else if (order != null) { %>
<p><strong>Mã đơn hàng:</strong> <%= order.getId() %></p>
<p><strong>Khách hàng:</strong> <%= order.getCustomerName() %></p>
<p><strong>Ngày đặt:</strong> <%= order.getFormattedCreateAt() %></p>
<p><strong>Tổng tiền:</strong> <%= order.getTotalPrice() %></p>

<h3>Trạng thái chữ ký:</h3>
<% if (isValid != null && isValid) { %>
<p class="success">✔ CHỮ KÝ HỢP LỆ</p>
<% } else { %>
<p class="error">❌ CHỮ KÝ KHÔNG HỢP LỆ</p>
<% } %>
<% } else { %>
<p class="error">Không có dữ liệu đơn hàng để hiển thị.</p>
<% } %>

</body>
</html>

