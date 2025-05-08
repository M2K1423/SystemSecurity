<%@ page import="com.example.webbongden.dao.model.SubCategory" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/19/2024
  Time: 8:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="icon" href="./img/logo-fold.png" sizes="180x180" />
  <title>Admin</title>
  <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
          integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
          crossorigin="anonymous"
          referrerpolicy="no-referrer"
  />
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link
          href="https://fonts.googleapis.com/css2?family=Nunito+Sans:ital,opsz,wght@0,6..12,200..1000;1,6..12,200..1000&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
          rel="stylesheet"
  />
  <link
          rel="stylesheet"
          href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css"
  />
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body>
<div class="wrapper">
  <%@ include file="header.jsp" %>

  <div class="main">
    <%@ include file="sidebar.jsp" %>

    <div class="main-content">
      <div class="tab-content" id="revenue-statistics-content">
        <div class="revenue-statistics">
          <div class="revenue-header">
            <p>THỐNG KÊ DOANH THU</p>
          </div>
          <div class="chart-selection">
            <div class="left">
              <label for="statistic-type">Chọn loại thống kê:</label>
              <select id="statistic-type" name="statisticType">
                <option value="monthly" selected>Thống kê theo tháng</option>
                <option value="yearly">Thống kê theo năm</option>
              </select>
            </div>
            <div class="right">
              <div class="input-group" id="month-input-group" style="display: block;"> <!-- Mặc định hiển thị -->
                <label for="month">Năm:</label>
                <input
                        type="number"
                        id="month"
                        name="year"
                        placeholder="Nhập năm"
                        value="2025"
                        min="2000"
                        max="2100"
                        required
                />
              </div>
              <button id="submit-btn" type="button">Xem Thống Kê</button>
            </div>
          </div>

          <!-- Biểu đồ -->
          <div class="revenue-body">
            <canvas id="revenue-chart"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="${pageContext.request.contextPath}/admin/admin_js/thongke.js?version=${System.currentTimeMillis()}" defer></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
</body>
</html>
