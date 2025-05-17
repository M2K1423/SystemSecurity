<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/19/2024
  Time: 5:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="sidebar">
    <div class="nav">
        <a href="admin?page=dashboard" class="nav-link" data-index="dashboard">
            <div class="nav-item">
                <i class="fa-solid fa-tachometer-alt"></i>
                <p>Tổng quan</p>
            </div>
        </a>
        <a href="admin?page=product-management" class="nav-link" data-index="product-management">
            <div class="nav-item">
                <i class="fa-solid fa-box"></i>
                <p>Quản lý kho hàng</p>
            </div>
        </a>
        <a href="admin?page=customer-management" class="nav-link" data-index="customer-management">
            <div class="nav-item">
                <i class="fa-solid fa-users"></i>
                <p>Quản lý khách hàng</p>
            </div>
        </a>
        <a href="admin?page=order-management" class="nav-link" data-index="order-management">
            <div class="nav-item">
                <i class="fa-solid fa-receipt"></i>
                <p>Quản lý đơn hàng</p>
            </div>
        </a>
        <a href="admin?page=revenue-statistics" class="nav-link" data-index="revenue-statistics">
            <div class="nav-item">
                <i class="fa-solid fa-chart-line"></i>
                <p>Thống kê doanh thu</p>
            </div>
        </a>
        <a href="admin?page=promotion" class="nav-link" data-index="promotion">
            <div class="nav-item">
                <i class="fa-solid fa-tags"></i>
                <p>Quản lý khuyến mãi</p>
            </div>
        </a>
        <a href="admin?page=account" class="nav-link" data-index="account">
            <div class="nav-item">
                <i class="fa-solid fa-user"></i>
                <p>Quản lý tài khoản</p>
            </div>
        </a>
    </div>

    <a href="/SystemSecurity_war/LogoutController" id="login-header-btn" style="display: block">
        <div class="sidebar-bottom" id="sign-up">
            <i class="fa-solid fa-arrow-right-from-bracket"></i>
            <p>Đăng xuất</p>
        </div>
    </a>
</div>
<script>
    const listNav = document.querySelectorAll(".nav-link");

    // Lấy giá trị 'page' từ query string
    const currentPage = new URLSearchParams(window.location.search).get('page');

    // Nếu không có 'page' trong URL, mặc định là 'dashboard'
    const defaultPage = 'dashboard';

    // Gắn class 'active' vào item tương ứng
    listNav.forEach((item) => {
        if (item.dataset.index === (currentPage || defaultPage)) {
            item.classList.add('active');
        }
    });

    // Thêm sự kiện click để chuyển trạng thái 'active'
    listNav.forEach((item) => {
        item.addEventListener('click', function () {
            listNav.forEach(i => {
                i.classList.remove('active');
            });
            item.classList.add('active');
        });
    });
</script>
</body>
</html>
