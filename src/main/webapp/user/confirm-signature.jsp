<%--
  Created by IntelliJ IDEA.
  User: PHAN PHAT
  Date: 5/10/2025
  Time: 5:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webbongden.dao.model.Order" %>

<%
  Order order = (Order) request.getAttribute("latestOrder");
%>

<html>
<head>
  <title>Xác nhận chữ ký đơn hàng</title>
  <style>
    body { font-family: Arial; padding: 20px; }
    textarea { width: 100%; font-size: 14px; }
    button {
      background-color: #4CAF50; color: white; padding: 10px 20px;
      border: none; border-radius: 4px; cursor: pointer;
    }
    .box {
      max-width: 600px; margin: auto; border: 1px solid #ccc; padding: 20px;
      border-radius: 10px; background: #f9f9f9;
    }
  </style>
</head>
<body>
<div class="box">
  <% if (order != null) { %>
  <h2>Xác nhận chính chủ đơn hàng</h2>
  <p><strong>Mã đơn hàng:</strong> #<%= order.getId() %></p>
  <p><strong>Khách hàng:</strong> <%= order.getCustomerName() %></p>
  <p><strong>Tổng tiền:</strong> <%= order.getTotalPrice() %> VND</p>
  <p><strong>Ngày đặt:</strong> <%= order.getCreatedAt() %></p>

  <hr>
  <h3>Bước 1: Tải file hóa đơn</h3>
  <a href="${pageContext.request.contextPath}/download-invoice?id=<%= order.getId() %>" target="_blank">
    📥 Tải file đơn hàng (.txt)
  </a>

  <h3>Bước 2: Ký file bằng ứng dụng Java (.p12)</h3>
  <p>Dùng phần mềm bạn đã tải để ký file. Sau đó dán chữ ký tại đây:</p>

  <form action="${pageContext.request.contextPath}/upload-signature" method="post">
    <input type="hidden" name="orderId" value="<%= order.getId() %>">
    <label for="signature">Dán chữ ký (Base64):</label><br>
    <textarea name="signature" rows="6" placeholder="Dán nội dung chữ ký số ở đây..." required></textarea><br><br>
    <button type="submit">🔐 Xác minh chữ ký</button>
  </form>
  <% } else { %>
  <p style="color:red;">Không tìm thấy thông tin đơn hàng.</p>
  <% } %>
</div>
</body>
</html>
