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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/admin.css?v=2.1">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/admin/admin_css/promotionAdmin.css?v=2.0">
</head>
<style>
    /* Tổng quát: Thiết kế bảng */
    .table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
        font-size: 16px;
        text-align: left;
    }

    .table th, .table td {
        padding: 12px 15px;
        border: 1px solid #ddd;
    }

    /* Header của bảng */
    .table thead th {
        background-color: #f4f4f4;
        font-weight: bold;
        text-align: left;
    }

    /* Dòng dữ liệu */
    .table tbody tr:nth-child(even) {
        background-color: #f9f9f9; /* Màu nền cho dòng chẵn */
    }

    .table tbody tr:nth-child(odd) {
        background-color: #fff; /* Màu nền cho dòng lẻ */
    }

    .table tbody tr:hover {
        background-color: #f1f1f1; /* Hiệu ứng hover */
    }

    /* Nút xóa */
    .btn-danger {
        background-color: #e74c3c;
        color: #fff;
        padding: 6px 12px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 14px;
        text-align: center;
    }

    .btn-danger:hover {
        background-color: #c0392b; /* Màu đỏ đậm hơn khi hover */
        transition: 0.3s;
    }

    /* Căn chỉnh các ô */
    .table th, .table td {
        text-align: left; /* Canh giữa nội dung */
        vertical-align: middle;
    }
</style>
<body>
<div class="wrapper">
    <%@ include file="header.jsp" %>

    <div class="main">
        <%@ include file="sidebar.jsp" %>

        <div class="main-content">
            <div class="tab-content" id="promotion-content">
                <div class="super-sale-container">
                    <h2>QUẢN LÝ CHƯƠNG TRÌNH GIẢM GIÁ</h2>
                    <p>Quản lý các sản phẩm đang nằm trong chương trình giảm giá</p>

                    <div class="add-super-sale">
                        <!-- Khu vực thêm sản phẩm giảm giá -->
                        <div id="add-promotion-area">
                            <h3>THÊM CHƯƠNG TRÌNH ĐẶC BIỆT</h3>
                            <form id="promotion-form" method="post" action="add-promotion">
                                <div class="form-group">
                                    <label for="promotionName">Tên Chương Trình:</label>
                                    <input type="text" id="promotionName" name="promotionName" placeholder="Nhập tên chương trình" required />
                                </div>
                                <div class="form-group">
                                    <label for="promotionEndDate">Ngày Kết Thúc:</label>
                                    <input type="date" id="promotionEndDate" name="promotionEndDate" required />
                                </div>
                                <div class="form-group">
                                    <label for="promotionDiscount">Mức Giảm Giá (%):</label>
                                    <input type="number" id="promotionDiscount" name="promotionDiscount" min="1" max="100" placeholder="Nhập mức giảm" required />
                                </div>
                                <div class="form-group">
                                    <label for="promotionType">Loại Khuyến Mãi:</label>
                                    <select id="promotionType" name="promotionType">
                                        <option value="gift">Quà Tặng</option>
                                        <option value="discount">Giảm Giá</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <button type="submit" id="add-promotion-btn">Thêm Chương Trình</button>
                                </div>
                            </form>
                        </div>

                        <div id="add-product-to-promotion-area">
                            <h3>THÊM SẢN PHẨM VÀO CHƯƠNG TRÌNH</h3>
                            <form id="product-to-promotion-form" method="post" action="add-product-to-promotion">
                            <div class="form-group">
                                    <label for="promotion-select">Chọn Chương Trình:</label>
                                    <select id="promotion-select" name="promotionId" required>
                                        <option value="">Chọn chương trình</option>
                                        <!-- Các chương trình sẽ được thêm vào đây -->
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="product-id">ID Sản Phẩm:</label>
                                    <input
                                            type="text"
                                            id="product-id"
                                            name="productId"
                                            placeholder="Nhập ID sản phẩm"
                                            required
                                            pattern="^[0-9]+$"
                                    title="ID sản phẩm phải là số"
                                    />
                                </div>
                                <div class="form-group">
                                    <button type="submit" id="add-product-to-promotion-btn">Thêm Sản Phẩm</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Danh sách sản phẩm giảm giá -->
                    <div class="list-discount">
                        <h3>DANH SÁCH CHƯƠNG TRÌNH ĐẶC BIỆT</h3>
                        <table id="promotion-table">
                            <thead>
                            <tr>
                                <th>Tên chương trình</th>
                                <th>Ngày bắt đầu</th>
                                <th>Ngày kết thúc</th>
                                <th>Loại chương trình</th>
                                <th>DSSP</th>
                                <th>Hành động</th>
                            </tr>
                            </thead>
                            <tbody id="super-sale-list">
                            <!-- Dữ liệu mẫu -->
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="overlay" data-index="4">
                    <div class="modal">
                        <h2 id="modal-discount-code">Mã giảm giá: SALE20</h2>
                        <div class="product-list">
                            <h3>Danh sách sản phẩm:</h3>
                            <ul id="modal-product-list">
                                <!-- Sản phẩm sẽ được thêm động -->
                            </ul>
                        </div>
                        <button class="close-overlay-btn">Đóng</button>
                    </div>
                </div>
                <div class="overlay" data-index="5">
                    <div class="modal">
                        <h2 id="sale-modal-title">Chi tiết chương trình</h2>
                        <div class="product-list">
                            <p>
                                <strong>ID Chương trình:</strong>
                                <span id="sale-program-id"></span>
                            </p>
                            <p>
                                <strong>Ngày bắt đầu:</strong>
                                <span id="sale-start-date"></span>
                            </p>
                            <p>
                                <strong>Ngày kết thúc:</strong>
                                <span id="sale-end-date"></span>
                            </p>
                            <p>
                                <strong>Tên chương trình:</strong>
                                <span id="sale-program-name"></span>
                            </p>
                            <h3>Danh sách sản phẩm:</h3>
                            <ul id="sale-product-list">
                                <!-- Sản phẩm sẽ được thêm động -->
                            </ul>
                        </div>
                        <button class="close-overlay-btn">Đóng</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/admin/admin_js/promotionAdmin.js?v=${System.currentTimeMillis()}" defer></script>
</body>
</html>
