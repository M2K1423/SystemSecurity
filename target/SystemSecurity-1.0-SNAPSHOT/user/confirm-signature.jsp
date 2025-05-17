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
    textarea, input[type="file"] { width: 100%; font-size: 14px; margin-top: 5px; }
    button {
      background-color: #4CAF50; color: white; padding: 10px 20px;
      border: none; border-radius: 4px; cursor: pointer;
    }
    .box {
      max-width: 700px; margin: auto; border: 1px solid #ccc; padding: 20px;
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
  <p>Sau khi ký, bạn có thể:</p>
  <ul>
    <li>Dán thủ công nội dung chữ ký vào khung dưới</li>
    <li>Hoặc <strong>tải file .sig</strong> đã ký lên</li>
  </ul>

  <!-- Dán chữ ký -->
  <form action="${pageContext.request.contextPath}/verify-signature" method="post">
    <input type="hidden" name="orderId" value="<%= order.getId() %>">
    <label for="signature">🔽 Dán chữ ký (Base64):</label><br>
    <textarea name="signature" rows="6" placeholder="Dán nội dung chữ ký số ở đây..."></textarea><br><br>
    <button type="submit">🔐 Xác minh chữ ký (dán thủ công)</button>
  </form>

  <hr>

  <!-- Tải file chữ ký -->
  <form action="${pageContext.request.contextPath}/verify-signature" method="post" enctype="multipart/form-data">
    <input type="hidden" name="orderId" value="<%= order.getId() %>">
    <label for="signatureFile">📤 Hoặc chọn file chữ ký (.sig):</label>
    <input type="file" name="signatureFile" accept=".sig" required><br><br>
    <button type="submit">🔐 Xác minh chữ ký (từ file)</button>
  </form>
  <% } else { %>
  <p style="color:red;">Không tìm thấy thông tin đơn hàng.</p>
  <% } %>
</div>
</body>
</html>
