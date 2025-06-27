<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webbongden.dao.model.Order" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
  HttpSession httpSession = request.getSession(false);
  String username = (String) httpSession.getAttribute("username");
  Order order = (Order) request.getAttribute("latestOrder");
%>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Xác nhận chữ ký đơn hàng</title>

  <!-- Google Fonts + Font Awesome -->
  <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans&family=Poppins&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" crossorigin="anonymous"/>

  <style>
    * {
      box-sizing: border-box;
      font-family: 'Poppins', sans-serif;
      margin: 0;
      padding: 0;
    }

    body {
      background: url('${pageContext.request.contextPath}/assets/img/background.png') no-repeat center center fixed;
      background-size: cover;
      font-family: 'Poppins', sans-serif;
      padding: 40px 20px;
      color: #333;
    }

    .container {
      max-width: 800px;
      margin: auto;
    }

    .box {
      background-color: rgba(255, 255, 255, 0.95);
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    /* Các phần còn lại như trước */

    h2 {
      color: #0d6efd;
      margin-bottom: 20px;
    }

    p {
      margin-bottom: 8px;
      font-size: 15px;
    }

    ul {
      margin-left: 20px;
      margin-bottom: 20px;
    }

    textarea, input[type="file"] {
      width: 100%;
      margin-top: 10px;
      margin-bottom: 20px;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 6px;
      font-size: 14px;
    }

    button {
      background-color: #198754;
      color: white;
      padding: 10px 16px;
      font-size: 14px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    button:hover {
      background-color: #146c43;
    }

    a {
      color: #0d6efd;
      font-weight: 500;
      display: inline-block;
      margin-bottom: 15px;
      text-decoration: none;
    }

    a:hover {
      text-decoration: underline;
    }

    hr {
      margin: 30px 0;
      border: none;
      border-top: 1px solid #ddd;
    }

    .error {
      color: red;
      font-weight: bold;
    }
  </style>
</head>

<body>
<div class="container">
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
    <ul>
      <li>Dán thủ công nội dung chữ ký vào khung dưới</li>
      <li>Hoặc tải file `.sig` đã ký lên</li>
    </ul>

    <!-- Dán chữ ký -->
    <form action="${pageContext.request.contextPath}/verify-signature" method="post">
      <input type="hidden" name="orderId" value="<%= order.getId() %>">
      <label for="signature">🔽 Dán chữ ký (Base64):</label>
      <textarea name="signature" rows="6" placeholder="Dán nội dung chữ ký số ở đây..."></textarea>
      <button type="submit">🔐 Xác minh chữ ký (dán thủ công)</button>
    </form>

    <hr>

    <!-- Tải file chữ ký -->
    <form action="${pageContext.request.contextPath}/verify-signature" method="post" enctype="multipart/form-data">
      <input type="hidden" name="orderId" value="<%= order.getId() %>">
      <label for="signatureFile">📤 Hoặc chọn file chữ ký (.sig):</label>
      <input type="file" name="signatureFile" accept=".sig" required>
      <button type="submit">🔐 Xác minh chữ ký (từ file)</button>
    </form>
    <% } else { %>
    <p class="error">Không tìm thấy thông tin đơn hàng.</p>
    <% } %>
  </div>
</div>
</body>
</html>
