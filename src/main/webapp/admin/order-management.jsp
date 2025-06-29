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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/admin/admin_css/productAdmin.css">
</head>
<body>
<div class="wrapper">
    <%@ include file="header.jsp" %>

    <div class="main">
        <%@ include file="sidebar.jsp" %>

        <div class="main-content">
            <div class="tab-content" id="product-management-content">
                <div class="stats-container">
                    <!-- Khối 1 -->
                    <div class="stat-box">
                        <div class="stat-image">
                            <img src="${pageContext.request.contextPath}/assets/img/adminpage/shopping-list.png" alt="" />
                        </div>
                        <div class="stat-text">
                            <h3>Tổng đơn hàng</h3>
                            <span id="total-orders">${totalOrders}</span>
                            <!-- Đổi id -->
                        </div>
                    </div>
                    <!-- Khối 2 -->
                    <div class="stat-box">
                        <div class="stat-image">
                            <img src="${pageContext.request.contextPath}/assets/img/adminpage/pending.png" alt="" />
                        </div>
                        <div class="stat-text">
                            <h3>Đơn hàng chưa xử lý</h3>
                            <span id="pending-orders">${totalOrdersPending}</span>
                            <!-- Đổi id -->
                        </div>
                    </div>
                    <!-- Khối 3 -->
                    <div class="stat-box">
                        <div class="stat-image">
                            <img src="${pageContext.request.contextPath}/assets/img/adminpage/fast-delivery.png" alt="" />
                        </div>
                        <div class="stat-text">
                            <h3>Đơn hàng đang giao</h3>
                            <span id="in-transit-orders">${totalOrdersShipping}</span>
                            <!-- Đổi id -->
                        </div>
                    </div>
                </div>

                <div class="header-tab">
                    <div class="search-container">
                        <input
                                type="text"
                                id="order-search"
                                placeholder="Tìm kiếm đơn hàng..."
                        />
                        <button id="search-btn-order">Tìm kiếm</button>
                    </div>

                    <div style="display: flex; gap: 20px">
                        <select id="sort-select-order" class="sort-select">
                            <option value="" disabled selected>Tất cả</option>
                            <option value="Pending">Đơn hàng chưa duyệt</option>
                            <option value="InProgress">Đang giao hàng</option>
                            <option value="Completed">Đã giao</option>
                            <option value="Rejected">Đã hủy</option>
                        </select>

                        <button id="sort-btn-order" class="sort-button">
                            <i class="fa-solid fa-sort"></i> Lọc
                        </button>
                    </div>
                </div>

                <div class="overlay">
                    <div class="invoice-details">
                        <div class="invoice-header">
                            <p>CHI TIẾT ĐƠN HÀNG</p>
                            <div class="close-icon" id="close-invoice-details">
                                <i class="fa-solid fa-xmark"></i>
                            </div>
                        </div>
                        <p style="padding-bottom:10px">
                            Mã đơn hàng: <span id="order_id">N/A</span>
                        </p>
                        <p style="padding-bottom:10px">
                            Ngày tạo: <span id="create_at">N/A</span>
                        </p>
                        <p style="padding-bottom:10px">
                            Hình thức giao hàng: <span id="form_of_delivery">N/A</span>
                        </p>
                        <p style="padding-bottom:10px">
                            Phí giao hàng: <span id="delivery_fee"></span>
                        </p>
                        <p style="border-bottom: 1px solid #dadada; padding-bottom:10px">
                            Ghi chú: <span id="note"></span>
                        </p>
                        <div class="customer-info-invoice">
                            <p>THÔNG TIN KHÁCH HÀNG</p>
                            <p>
                                <strong>Tên:</strong>
                                <span id="customer-name">N/A</span>
                            </p>
                            <p>
                                <strong>Địa chỉ:</strong>
                                <span id="customer-address">N/A</span>
                            </p>
                            <p>
                                <strong>Số điện thoại:</strong>
                                <span id="customer-phone">N/A</span>
                            </p>
                        </div>
                        <div class="order-items">
                            <h4>SẢN PHẨM MUA</h4>
                            <table class="items-table">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên SP</th>
                                    <th>Số Lượng</th>
                                    <th>Đơn Giá</th>
                                    <th>Thành Tiền</th>
                                </tr>
                                </thead>
                                <tbody id="order-items-body">

                                </tbody>
                            </table>
                        </div>
                        <div class="total-price">
                            <p>
                                <strong>TỔNG TIỀN:</strong>
                                <span id="total-amount">0 VND</span>
                            </p>
                        </div>
                        <div class="verification-result" style="padding-top: 10px;">
                            <p>
                                <strong>Trạng thái xác thực:</strong>
                                <span id="verification-status">Chưa xác thực</span>
                            </p>
                        </div>
                        <div class="edit-order-wrapper">
                            <button id="download-order-btn">Tải đơn hàng</button>
                        </div>
                    </div>
                </div>

                <!-- Bảng danh sách sản phẩm -->
                <div class="tab-container">
                    <table class="product-table" id="order-table">
                        <thead>
                        <tr>
                            <th>Id</th>
                            <th>Tên KH</th>
                            <th>Ngày đặt hàng</th>
                            <th>Tổng tiền</th>
                            <th>Địa chỉ</th>
                            <th>Trạng thái</th>
                            <th>Chi tiết</th>
                            <th>Thao tác</th>
                        </tr>
                        </thead>
                        <tbody id="order-table-body">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/admin/admin_js/orderAdmin.js?v=${System.currentTimeMillis()}" defer></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
</body>
</html>
