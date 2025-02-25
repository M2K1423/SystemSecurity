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
<style>
    /* Tổng quan cho bảng */
    .table {
        width: 100%;
        border-collapse: collapse; /* Loại bỏ khoảng cách giữa các ô */
        margin: 20px 0;
        font-size: 16px;
        text-align: left; /* Căn lề bên trái */
        background-color: #ffffff; /* Màu nền bảng */
        border: 1px solid #ddd; /* Đường viền bảng */
    }

    /* Đầu bảng */
    .table thead {
        background-color: #f8f9fa; /* Màu nền phần đầu */
        border-bottom: 2px solid #dee2e6;
    }

    .table thead th {
        padding: 12px 15px;
        text-transform: uppercase; /* Chữ viết hoa */
        font-weight: bold;
        color: #495057;
        border: 1px solid #ddd; /* Đường viền ô */
    }

    /* Hàng và ô trong bảng */
    .table tbody td {
        padding: 10px 15px;
        border: 1px solid #ddd; /* Đường viền ô */
        color: #343a40;
    }

    /* Hàng chẵn lẻ */
    .table tbody tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    .table tbody tr:nth-child(odd) {
        background-color: #ffffff;
    }

    /* Định dạng ô chứa số tiền */
    .table tbody td:last-child {
        font-weight: bold;
        color: #d9534f; /* Màu đỏ cho tổng tiền */
        text-align: right; /* Căn phải tổng tiền */
    }

    /* Canh giữa STT và ID */
    .table tbody td:first-child {
        text-align: center;
    }

    /* Tăng chiều rộng cho bảng */
    .swal2-html-container .table {
        width: 95%;
        margin: 0 auto; /* Căn giữa bảng */
    }

    /* Tăng chiều rộng modal */
    .swal2-popup {
        width: 80%;
    }
</style>
<body>
<div class="wrapper">
    <%@ include file="header.jsp" %>

    <div class="main">
        <%@ include file="sidebar.jsp" %>

        <div class="main-content">
            <div class="tab-content" id="customer-management-content">
                <div class="stats-container">
                    <!-- Khối 1 -->
                    <div class="stat-box">
                        <div class="stat-image">
                            <img src="${pageContext.request.contextPath}/assets/img/adminpage/client.png" alt="" />
                        </div>
                        <div class="stat-text">
                            <h3>Tổng khách hàng</h3>
                            <span id="monthly-total-customers">0</span>
                            <!-- Đổi id -->
                        </div>
                    </div>
                    <!-- Khối 2 -->
                    <div class="stat-box">
                        <div class="stat-image">
                            <img src="${pageContext.request.contextPath}/assets/img/adminpage/demand.png" alt="" />
                        </div>
                        <div class="stat-text">
                            <h3>Khách hàng mới</h3>
                            <span id="new-customers-today">0</span>
                            <!-- Đổi id -->
                        </div>
                    </div>
                    <!-- Khối 3 -->
                    <div class="stat-box">
                        <div class="stat-image">
                            <img src="${pageContext.request.contextPath}/assets/img/adminpage/rating.png" alt="" />
                        </div>
                        <div class="stat-text">
                            <h3>Khách hàng thường xuyên</h3>
                            <span id="regular-customers">0/5</span>
                            <!-- Đổi id -->
                        </div>
                    </div>
                    <!-- Khối 4 -->
                    <div class="stat-box">
                        <div class="stat-image">
                            <img src="${pageContext.request.contextPath}/assets/img/adminpage/chat.png" alt="" />
                        </div>
                        <div class="stat-text">
                            <h3>Phản hồi từ khách hàng</h3>
                            <span id="customer-feedback-count">0 đánh giá</span>
                            <!-- Đổi id -->
                        </div>
                    </div>
                </div>

                <div class="header-tab">
                    <div class="search-container">
                        <input
                                type="text"
                                id="customer-search"
                                placeholder="Tìm kiếm khách hàng..."
                        />
                        <button id="search-btn-cus">Tìm kiếm</button>
                    </div>
                </div>

                <!-- Bảng danh sách khách hàng -->
                <div class="tab-container">
                    <table class="customer-table">
                        <thead>
                        <tr>
                            <th>Id</th>
                            <th>Tên khách hàng</th>
                            <th>Email</th>
                            <th>Số điện thoại</th>
                            <th>Địa chỉ</th>
                            <th>Ngày đăng ký</th>
                            <th>Lịch sử mua hàng</th>
                        </tr>
                        </thead>
                        <tbody id="customer-table-body">
<%--                        <tr>--%>
<%--                            <td>C001</td>--%>
<%--                            <td>Nguyễn An</td>--%>
<%--                            <td>nguyenan@example.com</td>--%>
<%--                            <td>0123456789</td>--%>
<%--                            <td>123 Đường Lê Lợi, Quận 1, TP.HCM</td>--%>
<%--                            <td>15/10/2023</td>--%>
<%--                            <td>--%>
<%--                                <button class="view-details">Xem chi tiết</button>--%>
<%--                            </td>--%>
<%--                        </tr>--%>
                        </tbody>
                    </table>
                </div>

                <div class="overlay" data-index="2">
                    <div class="purchase-history-details">
                        <div class="purchase-history-header">
                            <p>LỊCH SỬ MUA HÀNG</p>
                            <div class="close-icon" id="close-purchase-history-details">
                                <i class="fa-solid fa-xmark"></i>
                            </div>
                        </div>

                        <div class="customer-info-purchase-history">
                            <p>THÔNG TIN KHÁCH HÀNG</p>
                            <p>
                                <strong>Tên:</strong>
                                <span id="name-info">Nguyễn Văn A</span>
                            </p>
                            <p>
                                <strong>Địa chỉ:</strong>
                                <span id="address-info">123 Đường ABC</span>
                            </p>
                            <p>
                                <strong>Số điện thoại:</strong>
                                <span id="phone-info">0987654321</span>
                            </p>
                        </div>

                        <div class="order-items">
                            <h4>SẢN PHẨM MUA</h4>
                            <table class="items-table">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên SP</th>
                                    <th>SL</th>
                                    <th>Đơn Giá</th>
                                    <th>Thành Tiền</th>
                                </tr>
                                </thead>
                                <tbody id="order-items-cus-body">
                                <tr>
                                    <td>1</td>
                                    <td>Đèn Trang Trí Hoa Mỹ</td>
                                    <td>5</td>
                                    <td>2,000,000 VND</td>
                                    <td>10,000,000 VND</td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>Đèn Chùm Pha Lê</td>
                                    <td>2</td>
                                    <td>3,500,000 VND</td>
                                    <td>7,000,000 VND</td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>Đèn Led Đổi Màu</td>
                                    <td>10</td>
                                    <td>500,000 VND</td>
                                    <td>5,000,000 VND</td>
                                </tr>
                                <tr>
                                    <td>4</td>
                                    <td>Đèn Tường Hiện Đại</td>
                                    <td>3</td>
                                    <td>1,200,000 VND</td>
                                    <td>3,600,000 VND</td>
                                </tr>
                                <tr>
                                    <td>5</td>
                                    <td>Đèn Bàn Gốm Sứ</td>
                                    <td>4</td>
                                    <td>1,000,000 VND</td>
                                    <td>4,000,000 VND</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <div class="total-price">
                            <p>
                                <strong>TỔNG TIỀN:</strong>
                                <span id="total-amount-order">29,600,000 VND</span>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/admin/admin_js/cusAdmin.js?v=2.0" defer></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
</body>
</html>

