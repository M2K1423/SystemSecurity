<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webbongden.dao.model.Order" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    Order order = (Order) request.getAttribute("order");
    Boolean valid = (Boolean) request.getAttribute("valid");

    // 🐞 DEBUG in ra valid (tùy chọn)
    System.out.println("<p style='color: gray;'>[DEBUG] valid = " + valid + "</p>");

    String error = (String) request.getAttribute("error");
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
%>

<html>
<head>
    <title>Kết quả xác minh chữ ký số</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/confirm-signature.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            padding: 30px;
        }

        .container {
            background-color: white;
            max-width: 650px;
            margin: auto;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        .order-info p {
            margin: 6px 0;
        }

        .status {
            font-size: 18px;
            font-weight: bold;
            margin-top: 20px;
        }

        .success {
            color: green;
        }

        .fail {
            color: red;
        }

        .button-back {
            display: inline-block;
            margin-top: 25px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            transition: background-color 0.3s ease;
        }

        .button-back:hover {
            background-color: #0056b3;
        }

        hr {
            margin: 20px 0;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>KẾT QUẢ XÁC MINH CHỮ KÝ SỐ</h1>

    <% if (order != null) { %>
    <div class="order-info">
        <p><strong>Mã đơn hàng:</strong> #<%= order.getId() %></p>
        <p><strong>Khách hàng:</strong> <%= order.getCustomerName() %></p>
        <p><strong>Tổng tiền:</strong> <%= order.getTotalPrice() %> VND</p>
        <p><strong>Ngày đặt:</strong> <%= sdf.format(order.getCreatedAt()) %></p>
    </div>

    <hr/>

    <% if (error != null) { %>
    <p class="status fail">❌ <%= error %></p>
    <% } else if (valid != null && valid) { %>
    <p class="status success">✅ Chữ ký HỢP LỆ - Chủ đơn hàng đã được xác thực.</p>
    <% } else if (valid == null) { %>
    <p class="status fail">⚠️ `valid` bị null - không có kết quả xác minh!</p>
    <% } else { %>
    <p class="status fail">❌ Chữ ký KHÔNG HỢP LỆ - Dữ liệu đã bị thay đổi hoặc chữ ký không trùng khớp.</p>
    <% } %>

    <% } else { %>
    <p class="status fail">❌ Không có dữ liệu đơn hàng để xác minh.</p>
    <% } %>

    <a href="${pageContext.request.contextPath}/home" class="button-back">⬅ Quay lại trang chủ</a>
</div>
</body>
</html>
