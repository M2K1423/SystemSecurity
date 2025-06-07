<%@ page import="com.example.webbongden.dao.model.SubCategory" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/19/2024
  Time: 8:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            <div class="tab-content" id="dashboard-content">
                <div class="card-revenue">
                    <div class="container-left">
                        <div class="card" id="revenue-card">
                            <div class="top-card">
                                <div class="top-card-title">
                                    <p>Tổng doanh thu</p>
                                </div>
                                <div class="value">
                                    <fmt:formatNumber value="${totalRevenue}" type="number" pattern="#,###.00"/>
                                </div>
                            </div>

                            <div class="bottom-card">
                                <div class="goal">
                                    <div class="goal-title">
                                        Monthly Goal
                                        <div class="goal-progress">70%</div>
                                    </div>
                                    <div class="bar blue">
                                        <span></span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card" id="orders-card">
                            <div class="top-card">
                                <div class="top-card-title">
                                    <p>Số đơn hàng</p>
                                </div>
                                <div class="value">${totalOrders}</div>
                            </div>

                            <div class="bottom-card">
                                <div class="goal">
                                    <div class="goal-title">
                                        Monthly Goal
                                        <div class="goal-progress">60%</div>
                                    </div>
                                    <div class="bar green">
                                        <span></span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card" id="customers-card">
                            <div class="top-card">
                                <div class="top-card-title">
                                    <p>Số khách hàng</p>
                                </div>
                                <div class="value">${totalUser}</div>
                            </div>

                            <div class="bottom-card">
                                <div class="goal">
                                    <div class="goal-title">
                                        Monthly Goal
                                        <div class="goal-progress">45%</div>
                                    </div>
                                    <div class="bar yellow">
                                        <span></span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card" id="visits-card">
                            <div class="top-card">
                                <div class="top-card-title">
                                    <p>Lượt truy cập</p>
                                </div>
                                <div class="value">N/A</div>
                            </div>

                            <div class="bottom-card">
                                <div class="goal">
                                    <div class="goal-title">
                                        Monthly Goal
                                        <div class="goal-progress">50%</div>
                                    </div>
                                    <div class="bar purple">
                                        <span></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="top-product">
                        <div class="top-product-header">
                            <p>Top 5 sản phẩm bán chạy</p>
                        </div>
                        <div class="top-product-list">
                            <table class="top-product-table">
                                <thead>
                                <tr>
                                    <th>Tên SP</th>
                                    <th>Số lượng bán</th>
                                    <th>Tổng tiền thu được</th>
                                    <th>Số lượng tồn kho</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="product" items="${topProducts}">
                                    <tr>
                                        <td>${product.productName}</td>
                                        <td>${product.quantitySold}</td>
                                        <td>${product.formattedRevenue}</td>
                                        <td>${product.stockQuantity}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="card-container">
                    <div class="recent-order">
                        <div class="recent-order-header">
                            <p>Đơn hàng gần đây</p>
                        </div>
                        <table class="order-table" id="order-table-dashboard">
                            <thead>
                                <tr>
                                    <th>ID Đơn Hàng</th>
                                    <th>Khách Hàng</th>
                                    <th>Ngày Đặt</th>
                                    <th>Trạng Thái</th>
                                    <th>Xác thực</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty orderLastMonth}">
                                    <c:forEach var="order" items="${orderLastMonth}">
                                        <tr>
                                            <td>${order.id}</td>
                                            <td>${order.customerName}</td>
                                            <td>${order.formattedCreateAt}</td>
                                            <td>${order.orderStatus}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${order.signed eq true}">
                                                        Đã ký
                                                    </c:when>
                                                    <c:otherwise>
                                                        Chưa ký
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty orderLastMonth}">
                                    <tr>
                                        <td colspan="4">Không có đơn hàng nào trong tháng này.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <div class="customer-chart" data-chart='${customerPieChart}'>
                        <div class="cus-chart-header">
                            <p>Khách hàng</p>
                            Đơn vị tính: %
                        </div>
                        <canvas id="customerPieChart"></canvas>
                    </div>
                </div>

                <div class="card-container2" data-revenue='${revenueChartData}'>
                    <div class="revenue-year">
                        <div class="revenue-year-header">Doanh thu trong tháng</div>
                        <canvas id="revenueChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/admin/admin_js/dashboardAdmin.js?v=2.0" defer></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
</body>
</html>
