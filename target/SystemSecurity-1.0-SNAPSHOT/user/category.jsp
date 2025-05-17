<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/15/2024
  Time: 10:21 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/css; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180">
    <title>Danh mục</title>
    <link
            href="https://fonts.googleapis.com/css2?family=Nunito+Sans:ital,opsz,wght@0,6..12,200..1000;1,6..12,200..1000&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
            rel="stylesheet"
    />
    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
            crossorigin="anonymous"
    />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
            integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
            crossorigin="anonymous"
            referrerpolicy="no-referrer"
    />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <!-- jQuery UI -->

    <link
            rel="stylesheet"
            href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"
    />
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
    <script
            src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
            integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
            crossorigin="anonymous"
    ></script>
    <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
            integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
            crossorigin="anonymous"
    ></script>
    <script src="https://cdn.jsdelivr.net/npm/paginationjs/dist/pagination.min.js"></script>
    <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/paginationjs/dist/pagination.css"
    />
<%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/header-footer.css">--%>
<%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">--%>
<%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/Detail.css">--%>
<style>
    :root {
        --color-black: #171C24;
        --color-white: #fff;
        --color-yellow: #ffe31a;
        --color-yellow-dark: #ffd900;
        --color-gray-light: #e9e9e9;
        --color-gray-dark: #ddd;
        --color-gray-mid: #555;
        --color-gray-darkest: #333;
    }

    *,
    *:before,
    *:after {
        box-sizing: border-box;
    }
    * {
        margin: 0;
        padding: 0;
        font-family: "Poppins", sans-serif;
        font-size: 14px;
    }

    img,
    picture,
    svg,
    video {
        display: block;
        max-width: 100%;
    }
    input,
    select,
    textarea {
        background-color: transparent;
        outline: none;
    }
    button {
        cursor: pointer;
        background-color: transparent;
        outline: none;
        border: 0;
    }
    body {
        min-height: 100vh;
        font-weight: 400;
        font-size: 16px;
        line-height: 1;
        background-color: var(--color-gray-light);
    }

    ul li {
        list-style: none;
    }

    hr {
        border: 0;
        height: 1px;
        background-color: #cfcfcf;
        color: #cfcfcf;
    }

    .container {
        max-width: 1240px;
        margin: 0 auto;
    }

    @import url("https://fonts.googleapis.com/css2?family=Lato:wght@400;700&family=Poppins:wght@400;500;600;700&display=swap");
    .wrapper {
        min-height: 100vh;
        padding-top: 110px;
    }

    a {
        color: black;
        text-decoration: none;
    }

    p {
        margin: 0;
    }

    /* Sidebar styles */
    .wrapper .sidebar {
        position: fixed;
        top: 0;
        left: -400px; /* Sidebar initially hidden */
        width: 300px;
        height: 100%;
        background-color: var(--color-black);
        color: #fff;
        transition: 0.3s;
        z-index: 100000000000; /* Make sure sidebar is above other content */
    }

    .wrapper .sidebar .sidebar-header img {
        margin: 0 auto;
    }

    .wrapper .sidebar .sidebar-header {
        border-bottom: 1px solid #dadada;
    }

    .wrapper .sidebar-list {
        list-style-type: none;
        padding: 0;
    }

    .wrapper .sidebar-list li {
        padding: 15px;
        border-bottom: 1px solid #555;
    }

    .wrapper .sidebar a {
        font-size: 14px;
        color: #fff;
        font-weight: 500;
        text-decoration: none;
        display: block;
    }

    .wrapper .sidebar-list li:hover {
        background-color: #444;
    }

    /* Overlay styles */
    .wrapper .overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: none; /* Hidden by default */
        z-index: 10000000; /* Make overlay above content but below sidebar */
    }

    /* Active classes for showing sidebar and overlay */
    .sidebar.active {
        left: 0; /* Slide sidebar in */
    }

    .overlay.active {
        display: block; /* Show overlay */
    }

    .header-top .fa-bars {
        color: white;
        font-size: 22px;
        padding: 10px;
        cursor: pointer;
        display: none;
    }

    .fa-bars.toggle-sidebar {
        display: block;
    }

    /* Header */
    .wrapper .header {
        width: 100%;
        top: 0;
        left: 0;
        position: fixed;
        background-color: var(--color-black);
        z-index: 1000000;
    }

    .wrapper .header .header-top {
        max-width: 1280px;
        margin-right: auto;
        margin-left: auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: var(--color-black);
        padding: 10px 20px;
        z-index: 10000;
    }

    .header .header-left {
        display: flex;
        align-items: center;
    }

    .header .header-right .header-user {
        border-radius: 100%;
        display: none;
        margin-left: 20px;
        position: relative;
        cursor: pointer;
    }

    .header .header-right .header-user img {
        width: 40px;
        height: 40px;
        border-radius: 100%;
        object-fit: cover;
    }

    .header .header-top .logo {
        margin-right: 36px;
        min-width: 100px;
    }

    .header .header-top .menu {
        color: var(--color-white);
        padding: 10px 20px;
        border-radius: 5px;
        margin-right: 20px;
        display: flex;
        align-items: center;
    }
    .header .header-top .menu i {
        margin-right: 10px;
    }
    .header .header-top .search-bar form {
        background-color: var(--color-white);
        border-radius: 5px;
        padding: 5px 10px;
        width: 360px;
        cursor: pointer;
    }

    .header .header-top .search-bar {
        display: flex;
        align-items: center;
        position: relative;
    }

    .header .header-top .search-bar button {
        position: absolute;
        right: 10px;
        padding: 10px 15px;
        background-color: #fff;
    }


    .header .header-top .search-bar input {
        border: none;
        outline: none;
        padding: 5px;
        width: 250px;
    }

    .mobile-search-bar {
        display: flex;
        justify-content: space-between;
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        padding: 10px;
        background-color: #fff;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        z-index: 1000;
        opacity: 0;
        transform: translateY(-100%);
        transition: opacity 0.3s ease, transform 0.3s ease;
    }

    .mobile-search-bar form {
        display: flex;
        align-items: center;
    }

    .mobile-search-bar input {
        flex: 1;
        padding: 8px 10px;
        font-size: 14px;
        border: none;
        border-radius: 5px;
        outline: none;
    }

    .mobile-search-bar form {
        border: 1px solid #eee;
        border-radius: 8px;
        width: 250px;
    }

    .mobile-search-bar button {
        padding: 10px;
        color: #fff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .mobile-search-bar button i {
        font-size: 15px;
        color: black;
    }

    .header .header-top .search-bar i {
        color: #888;
    }
    .header .header-top .info {
        display: flex;
        align-items: center;
        color: var(--color-white);
    }
    .header .header-top .info,
    .header .header-top .info .info-item {
        display: flex;
        text-decoration: none;
        color: white;
        align-items: center;
    }

    .header .header-top .info .info-item {
        margin: 0 10px;
    }

    .header .header-top .info .info-item:hover {
        color: var(--color-yellow-dark);
    }
    .header .header-top .info div i {
        margin-right: 5px;
    }
    .header .header-top .info .login {
        background-color: var(--color-yellow);
        padding: 10px 20px;
        min-width: 140px;
        display: block;
        border-radius: 5px;
    }

    .header .header-top .info a {
        text-decoration: none;
    }

    .header .header-top .info .login:hover {
        background-color: var(--color-yellow-dark);
    }

    .header .navbar {
        background-color: var(--color-yellow);
        width: 100%;
    }

    .header .navbar .navbar-list {
        list-style: none;
        display: flex;
        align-items: center;
        width: 1240px;
        margin-right: auto;
        margin-left: auto;
    }

    .header .navbar .navbar-list>li> a {
        display: block;
        padding: 10px 26px;
        font-size: 14px;
        text-decoration: none;
        text-align: left;
        color: var(--color-black);
        font-weight: 600;
    }

    .header .navbar .navbar-list > li > a:hover {
        background-color: var(--color-yellow-dark);
    }

    .header .navbar .navbar-list .dropdown:hover .submenu {
        display: block;
        width: 1000px;
    }

    .header .navbar .navbar-list .dropdown .submenu {
        display: none;
        position: absolute;
    }

    .header .navbar .navbar-list .dropdown .submenu > ul {
        display: flex;
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        background-color: #fff;
        padding: 12px;
        box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
        text-align: left;
    }

    .header .navbar-list .dropdown .submenu > ul .category {
        background-color: var(--color-white);
        padding: 10px 10px 30px 10px;
    }

    .header .navbar-list .dropdown .category-header {
        font-size: 15px;
        text-transform: uppercase;
        font-weight: 600;
        color: var(--color-black);
        text-align: left;
        color: #e30019;
    }

    .header .navbar-list .dropdown .category-products {
        margin-top: 10px;
        list-style: none;
    }

    .header .navbar-list .dropdown .category-item {
        font-size: 14px;
        color: #555;
        text-align: left;
        margin: 10px 0;
    }

    .header .navbar-list .dropdown .category-item:hover {
        color: #e30019;
        cursor: pointer;
        border-radius: 4px;
    }


    .user-info-dropdown {
        background-color: #fff;
        color: black;
        position: absolute;
        top: 100%;
        transform: translateX(-220px) translateY(12px);
        width: 270px;
        padding: 15px;
        border-radius: 3px;
        opacity: 0;
        visibility: hidden;
        transition: opacity 0.3s ease, transform 0.3s ease, visibility 0s ease 0.3s;
        z-index: 10000;
    }

    .header-user:hover .user-info-dropdown {
        opacity: 1;
        visibility: visible;
        transition: opacity 0.3s ease, transform 0.3s ease, visibility 0s ease 0s;
    }


    .user-info-dropdown::before {
        content: "";
        position: absolute;
        top: -18px;
        right: 20px;
        border-left: 10px solid transparent;
        border-right: 10px solid transparent;
        border-top: 10px solid transparent;
        border-bottom: 10px solid #fff;
    }


    .category-item > a {
        display: block;
        color: black;
        text-decoration: none;
    }


    .user-info-dropdown .dropdown-header {
        padding-bottom: 15px;
        border-bottom: 1px solid #dadada;
        display: flex;
        gap: 15px ;
        align-items: center;
    }

    .user-info-dropdown .dropdown-header p {
        font-weight: 500;
    }

    .user-info-dropdown .dropdown-header img {
        width: 20px !important;
        height: 20px !important;
    }

    .user-info-dropdown .dropdown-content {
        display: flex;
        flex-direction: column;
    }

    .user-info-dropdown .dropdown-content a,
    .dropdown-footer a {
        font-size: 13px;
    }

    .user-info-dropdown .dropdown-content a:hover,
    .user-info-dropdown .dropdown-footer a:hover {
        text-decoration: underline !important;
    }

    .user-info-dropdown .dropdown-content .dropdown-item,
    .dropdown-footer {
        display: flex;
        gap: 10px;
        align-items: center;
    }

    .user-info-dropdown .dropdown-content .dropdown-item {
        padding: 12px 0;
    }

    .user-info-dropdown .dropdown-content .dropdown-item a{
        text-decoration: none;
        color: black;
    }

    .user-info-dropdown .dropdown-footer {
        padding-top: 15px;
        border-top: 1px solid #dadada;
    }

    .user-info-dropdown .dropdown-footer a{
        text-decoration: none;
        color: black;
    }

    .user-info-dropdown i {
        width: 20px;
        height: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .footer {
        background-color: var(--color-black);
        color: #aaa;
        padding: 50px 0 0 0;
        margin-top: 80px;
    }

    .footer .footer-top {
        display: flex;
        gap: 120px;
    }

    .footer .footer-top .tittle {
        margin-bottom: 30px;
        color: #fff;
        font-weight: 600;
        font-size: 16px;
    }

    .footer .footer-top .list-ship {
        display: flex;
        gap: 20px;
    }

    .footer .footer-top .footer-top-contact,
    .footer .footer-top .footer-item {
        display: flex;
        flex-direction: column;
        gap: 20px;
        min-width: 200px;
    }

    .footer .footer-bottom {
        width: 100%;
        height: 60px;
        background-color: #12161d;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .footer-bottom .logo img,
    .wrapper .header-top .logo img {
        height: 55px;
    }

    .footer-bottom .social-media {
        display: flex;
        align-items: center;
    }

    .footer-bottom .social-media p {
        margin-right: 30px;
    }

    .footer-bottom .social-icon {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 30px;
        height: 30px;
        color: #fff;
        text-decoration: none;
        transition: color 0.3s;
        border-radius: 100%;
        margin-left: 10px;
    }

    .footer-bottom .social-icon i {
        font-size: 16px;
    }

    .footer-bottom .social-icon:hover {
        color: var(--color-yellow-dark);
    }

    .footer-wrapper {
        width: 100%;
    }

    .tmp {
        margin-top: 40px;
        width: 100%;
        background-color: #fff;
        background-color: #12161d;
    }

    /* Wrapper and General Header */
    /* Phần fix lại */
    a {
        color: black;
        text-decoration: none;
    }
    .main .breadcrumb {
        font-size: 16px;
        color: #000;
        margin-bottom: 40px;
        margin-top: 20px;
    }
    .main .breadcrumb a {
        text-decoration: none;
        color: #000;
    }

    .main .breadcrumb a:hover {
        color: red;
    }
    .main .breadcrumb .separator {
        margin: 0 5px;
    }

    .header-right .header-user {
        display: none;
    }



    .navbar .navbar-list {
        margin: 0;
        padding: 0;
    }


    .navbar {
        padding: 0;
    }
    .category .category-products {
        padding: 0 !important;
    }

    section .shop_grid_area {
        margin-top: 200px !important;
    }

    .product-badge {
        position: absolute;
        top: 10px;
        left: 10px;
        background-color: #ff6b6b;
        color: #fff;
        padding: 5px 10px;
        border-radius: 4px;
        font-size: 12px;
        font-weight: bold;
    }

    .product-description {
        padding: 15px;
        text-align: center;
    }

    .product-description h6 {
        height: 100px;
        font-size: 16px;
        font-weight: 600;
        overflow: hidden;
        color: #333;
        margin-bottom: 10px;
    }

    .product-price {
        color: red;
        font-size: 16px;
        font-weight: 600;
    }

    .product-price .old-price {
        color: #999;
        text-decoration: line-through;
        margin-right: 5px;
    }

    .hover-content {
        display: none;
    }

    .add-to-cart-btn .btn {
        background-color: #ff6b6b;
        color: #fff;
        font-size: 14px;
        padding: 8px 20px;
        border-radius: 4px;
        text-decoration: none;
        transition: background-color 0.3s;
    }

    /* Hiệu ứng khi hover */
    .add-to-cart-btn .essence-btn:hover {
        background-color: #004cff; /* Màu nền khi hover */
        color: #fff;
        transform: scale(1.05); /* Phóng to nhẹ */
        box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.2);
    }

    /* Hiệu ứng khi nhấn (active) */
    .add-to-cart-btn .essence-btn:active {
        transform: scale(0.95); /* Thu nhỏ nhẹ khi nhấn */
        box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.1);
    }

    .shop_grid_product_area {
        padding: 15px;
        background-color: #fff;
        border-radius: 4px;
    }

    .product-topbar {
        font-size: 16px; /* Kích thước font chữ cho thanh sản phẩm */
        color: #333; /* Màu sắc chữ */
        margin-bottom: 15px;
    }

    .total-products p {
        margin: 0;
        font-weight: 600;
    }

    .product-sorting p {
        margin-right: 10px; /* Thêm khoảng cách giữa các phần tử */
    }

    #sortByselect {
        font-size: 14px; /* Kích thước font chữ cho dropdown */
        padding: 5px; /* Thêm padding để tạo không gian cho chữ */
        background-color: #fff;
        border-radius: 4px;
        border: 1px solid #ccc;
    }
    .single-product-wrapper {
        border: 1px solid #e0e0e0;
        border-radius: 4px;
        overflow: hidden;
        transition: all 0.3s ease-in-out;
        background-color: #fff;
        padding: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        cursor: pointer;
    }

    .product-item .product-name {
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
        margin-bottom: 12px;
    }

    .product-item .img {
        overflow: hidden;
        height: 200px;
    }

    .product-item .img img {
        transition: transform 0.3s ease;
        height: 100%;
        width: 100%;
    }

    .img:hover img {
        transform: scale(1.1);
    }
    .product-info {
        margin-top: 18px;
        text-align: left;
    }

    .original-price {
        font-size: 12px;
        color: #888;
        text-decoration: line-through;
        margin-top: 8px;
        text-align: left;
    }

    .price-discount {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .discount-percentage {
        width: 38px;
        height: 25px;
        display: flex;
        justify-content: center;
        align-items: center;
        color: red;
        font-size: 12px;
        background-color: #ffeded;
        border: 1px solid red;
        border-radius: 2px;
    }

    .product-badge {
        background-color: red; /* Đặt màu nền thành màu đỏ */
        color: white; /* Đặt màu chữ thành trắng để dễ đọc trên nền đỏ */
        font-family: "Ubuntu", sans-serif;
        font-weight: 700;
        font-size: 12px;
        padding: 5px 10px;

        border-radius: 4px;
        position: absolute;
        top: 10px; /* Điều chỉnh khoảng cách từ phía trên */
        left: 10px; /* Điều chỉnh khoảng cách từ bên trái */
        z-index: 10;
        border-radius: 3px; /* Tùy chọn, tạo góc bo tròn nhẹ */
    }
    /* Pagination Container */
    .pagination {
        display: flex;
        justify-content: center;
        align-items: center;
        list-style: none;
        padding: 0;
        margin: 20px 0;
    }

    /* Pagination Items */
    .page-item {
        margin: 0 5px;
    }

    .login {
        height: 40px;
    }

    .page-link {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        font-size: 16px;
        color: #333; /* Text color */
        background-color: #f8f9fa; /* Light background color */
        border: 1px solid #ddd; /* Border color */
        border-radius: 5px; /* Rounded corners */
        text-decoration: none;
        transition: all 0.3s ease;
    }

    /* Hover Effect */
    .page-link:hover {
        background-color: #ffc107; /* Highlight color on hover */
        color: #fff; /* Text color on hover */
        border-color: #ffc107;
    }

    /* Active Page Style */
    .page-item.active .page-link {
        background-color: #333; /* Active background color */
        color: #fff; /* Active text color */
        border-color: #333;
        font-weight: bold;
    }

    /* Icon Styling */
    .page-link i {
        font-size: 18px;
    }

    /* Larger clickable area for left and right arrows */
    .page-item:first-child .page-link,
    .page-item:last-child .page-link {
        width: 50px;
    }

    /* Phong cách chung cho sidebar */
    .shop_sidebar_area {
        background-color: #f8f9fa;
        padding: 10px;
        border-radius: 4px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    ul {
        padding: 0;
    }

    /* Phong cách tiêu đề của widget */
    .widget-title1 {
        font-size: 18px;
        color: #333;
        font-weight: bold;
        margin-bottom: 15px;

        padding-bottom: 8px;
        padding-top: 20px;
    }

    /* Danh mục chính */
    /* Style cho danh mục chính */
    #menu-content2 {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .category-item > a {
        display: block;
        color: black;
        text-decoration: none;
    }

    .category-item > a:hover {
        color: red;
    }

    /* Style cho danh mục phụ */
    .sub-menu {
        list-style: none;
        padding: 0;
        display: none; /* Ẩn danh mục phụ ban đầu */
        background-color: #f3f3f3;
        margin: 0;
    }

    .category-item.active .sub-menu {
        display: block; /* Hiển thị danh mục phụ khi mục chính được kích hoạt */
    }

    .sub-menu li a {
        display: block;
        padding: 8px 20px;
        color: #555;
        text-decoration: none;
        transition: background-color 0.3s;
    }

    .sub-menu li a:hover {
        background-color: #eee; /* Màu nền khi hover trên mục con */
        color: #000;
    }

    /* Kiểu hoạt ảnh khi mở/đóng menu phụ */
    .sub-menu {
        transition: max-height 0.3s ease-out;
        overflow: hidden;
        max-height: 0;
    }

    .category-item.active .sub-menu {
        max-height: 500px; /* Chiều cao tối đa khi menu phụ mở */
    }

    .slider-range-price {
        margin: 10px 0;
    }
    .range-price {
        font-weight: bold;
        margin-top: 10px;
    }

    .slider-range .ui-slider-range {
        background-color: #007bff;
    }

    .slider-range .ui-slider-handle {
        background-color: #007bff;
        border: 2px solid #fff;
        width: 20px;
        height: 5px;
        border-radius: 50%;
    }

    /* Phong cách thương hiệu */
    .widget.brands {
        background-color: #f8f8f8; /* Light background for the widget */
        padding: 20px;
        border-radius: 8px;
    }

    .widget-title3 {
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin-bottom: 20px;
        text-transform: uppercase;
    }

    .widget-desc ul {
        list-style-type: none; /* Remove bullet points */
        padding: 0; /* Remove padding */
        margin: 0; /* Remove margin */
    }

    .widget-desc ul li {
        padding: 10px 0; /* Add padding between items */
        border-bottom: 1px solid #ddd; /* Light border between items */
    }

    .widget-desc ul li:last-child {
        border-bottom: none; /* Remove the border on the last item */
    }

    .widget-desc ul li a {
        color: #333; /* Text color */
        text-decoration: none; /* Remove underline from links */
        font-size: 16px; /* Adjust font size */
        transition: color 0.3s; /* Smooth transition for hover */
    }

    .widget-desc ul li a:hover {
        color: #007bff; /* Color change on hover */
    }

    .brands ul li {
        margin-bottom: 10px;
    }

    .brands ul li a {
        color: #333;
        font-weight: 500;
        transition: color 0.3s;
        text-decoration: none;
    }

    .brands ul li a:hover {
        color: #007bff;
    }

    /* Ẩn các sub-menu */
    .sub-menu {
        display: none;
        padding-left: 15px;
    }

    /* Kiểu cho mục đã mở */
    .category-item.active .sub-menu {
        display: block;
    }

    .news-latest {
        margin-top: 40px;
        padding: 15px;
        background-color: #f9f9f9;
        border: 1px solid #ddd;
        border-radius: 5px;
    }

    .sidebarblog-title {
        font-size: 18px;
        font-weight: 600;
        margin-bottom: 20px;
        color: #000;
    }

    .list-news-latest .item-article {
        display: flex;
        align-items: flex-start;
        margin-bottom: 15px;
    }

    .item-article .post-image {
        flex-shrink: 0;
        width: 80px;
        height: 80px;
        overflow: hidden;
        border-radius: 5px;
    }

    .item-article .post-image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .post-content {
        margin-left: 15px;
    }

    .post-content .title {
        font-size: 15px;
        font-weight: 600;
        color: #000;
        margin-bottom: 5px;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
        margin-bottom: 10px;
    }


    .post-content .title a {
        color: #333;
        text-decoration: none;
    }

    .post-content .title a:hover {
        color: #007bff;
    }

    .date {
        font-size: 12px;
        color: #666;
        display: flex;
        align-items: center;
    }

    .date i {
        margin-right: 5px;
        color: #999;
    }




    .product-container.hide {
        opacity: 0; /* Làm mờ các sản phẩm */
        transform: translateX(20px); /* Trượt nhẹ sang phải */
    }

    /* Hiệu ứng khi các sản phẩm xuất hiện */
    .product-container.show {
        opacity: 1;
        transform: translateX(0); /* Trở về vị trí ban đầu */
    }
    /* Các sản phẩm */
    .single-product-wrapper {
        opacity: 1;
        transition: opacity 0.5s ease-in-out;
    }

    .single-product-wrapper.hide {
        opacity: 0;
    }

    #pagination-controls {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 30px;
        gap: 15px;
        font-family: 'Arial', sans-serif;
    }


    #pagination-controls button {
        width: 70px;
        font-weight: 600;
        height: 40px;
        border-radius: 4px;
        background-color: #dadada;
    }

    .category-container .category-container {
        padding: 10px;
        background-color: #fff;
    }

    .category-container .category-header {
        display: flex;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid #dee2d6;
        font-weight: 500;
    }

    .category-header p {
        font-size: 18px;
        padding-left: 10px;
        font-weight: 500;
    }

    .category-list>li {
        padding: 16px 4px;
        cursor: pointer;
        border-bottom: 1px solid #dee2d6;
    }

    .cate-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    /* Mặc định, ẩn danh sách con */
    .subcategory-list {
        height: 0; /* Ẩn danh sách con */
        overflow: hidden; /* Ẩn phần tử bị tràn */
        transition: height 0.2s ease;
    }

    /* Khi danh sách con được mở */
    .subcategory-list.show {
        height: auto; /* Dễ dàng điều chỉnh chiều cao của danh sách khi mở */
        margin-top: 15px; /* Khoảng cách giữa thẻ cha và danh sách con */
    }

    /* Cài đặt cho các mục con */
    .subcategory-list > li {
        padding: 10px 20px;
        border-radius: 4px;
        font-size: 13px;
    }

    .subcategory-list > li:hover {
        background-color: #f2f2f2;
    }


    .subcategory-list a {
        text-decoration: none;
        color: #000;
    }

    /* Hiệu ứng xoay icon khi mở */
    .cate-item i {
        transition: transform 0.3s ease; /* Hiệu ứng mượt mà khi xoay */
    }

    /* Khi thẻ cha được chọn, icon sẽ xoay */
    .cate-item i.active {
        transform: rotate(180deg);
    }

    .product-item>a {
        padding: 15px;
    }

    .product-item {
        padding: 0 10px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .sub-category-item a.active {
        font-weight: 500;
        color: red; /* Tùy chỉnh màu sắc */
    }
</style>
</head>
<body>
<!-- Header -->

<div class="wrapper">
    <!-- Header -->
    <jsp:include page="../reuse/header.jsp" />
    <div class="main">
        <div class="container">
            <div class="breadcrumb">
                <i class="fa-solid fa-house"></i>
                <a href="home">Trang chủ</a>
                <span class="separator">›</span>

                <!-- Nếu có categoryName và subCategoryName -->
                <c:if test="${not empty categoryName}">
                    <a href="CategoryController?categoryId=${categoryId}">${categoryName}</a>
                    <span class="separator">›</span>
                </c:if>

                <!-- Nếu chỉ có subCategoryName -->
                <c:if test="${not empty subCategoryName}">
                    <span>${subCategoryName}</span>
                </c:if>

                <!-- Nếu không có cả categoryName và subCategoryName -->
                <c:if test="${empty categoryName && empty subCategoryName}">
                    <span>Sản phẩm</span>
                </c:if>
            </div>
        </div>


        <section class="shop_grid_area section-padding-80">
            <div class="container">
                <div class="row row1">
                    <div class="col-8 col-md-4 col-lg-3 d-none d-md-block">
                        <div class="shop_sidebar_area">
                            <!-- ##### Single Widget ##### -->
                            <div class="category-container">
                                <div class="category-header">
                                    <i class="fa-solid fa-list"></i>
                                    <p>Danh mục</p>
                                </div>
                                <ul class="category-list">
                                    <!-- Duyệt qua danh sách categories -->
                                    <c:forEach var="category" items="${categories}">
                                        <li class="a">
                                            <a class="cate-item" href="javascript:void(0);">
                                                <p>
                                                    <span>${category.categoryName}</span>
                                                </p>
                                                <i class="fa-solid fa-caret-down"></i>
                                            </a>
                                            <ul class="subcategory-list">
                                                <c:forEach var="subCategory" items="${subCategoriesMap[category.id]}">
                                                    <li class="sub-category-item">
                                                        <a href="CategoryController?categoryId=${category.id}&subCategoryId=${subCategory.id}" data-id="${subCategory.id}">
                                                            <span>${subCategory.name}</span>
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <div class="news-latest box-layer mb-3">
                            <div class="sidebarblog-title title_block">TIN TỨC MỚI</div>
                            <div class="list-news-latest">
                                <div class="item-article clearfix">
                                    <div class="post-image">
                                        <a href="#">
                                            <img
                                                    class="lazyload"
                                                    src="https://denhoamy.vn/upload/news/3018den-aeon-mall-le-chan-hai-phong-1.jpg"
                                                    alt="Đèn Hoa Mỹ cung cấp đèn trang trí cho Trung tâm thương mại Aeon Mall Lê Chân Hải Phòng"
                                            />
                                        </a>
                                    </div>
                                    <div class="post-content">
                                        <div class="title max">
                                            <a
                                                    href="/tin-tuc/den-hoa-my-thi-cong-den-trang-tri-cho-trung-tam-thuong-mai-aeon-mall-le-chan-hai-phong-n87.html"
                                            >
                                                Đèn Hoa Mỹ cung cấp đèn trang trí cho Trung tâm
                                                thương mại Aeon Mall Lê Chân Hải Phòng
                                            </a>
                                        </div>
                                        <div class="date">
                                            <i class="fa fa-calendar-alt"></i> 09/01/2024
                                        </div>
                                    </div>
                                </div>

                                <div class="item-article clearfix">
                                    <div class="post-image">
                                        <a href="#">
                                            <img
                                                    class="lazyload"
                                                    src="https://denhoamy.vn/upload/news/5664screenshot1622455555.png"
                                                    alt="Đèn Hoa Mỹ cung cấp đèn trang trí cho Trung tâm thương mại Aeon Mall Lê Chân Hải Phòng"
                                            />
                                        </a>
                                    </div>
                                    <div class="post-content">
                                        <div class="title max">
                                            <a
                                                    href="/tin-tuc/den-hoa-my-thi-cong-den-trang-tri-cho-trung-tam-thuong-mai-aeon-mall-le-chan-hai-phong-n87.html"
                                            >
                                                Những mẫu đèn chùm pha lê hiện đại sang trọng cho
                                                nội thất
                                            </a>
                                        </div>
                                        <div class="date">
                                            <i class="fa fa-calendar-alt"></i> 09/01/2024
                                        </div>
                                    </div>
                                </div>

                                <div class="item-article clearfix">
                                    <div class="post-image">
                                        <a href="#">
                                            <img
                                                    class="lazyload"
                                                    src="https://denhoamy.vn/upload/news/54743470den-op-tran-hien-dai-19097.jpg"
                                                    alt="Đèn Hoa Mỹ cung cấp đèn trang trí cho Trung tâm thương mại Aeon Mall Lê Chân Hải Phòng"
                                            />
                                        </a>
                                    </div>
                                    <div class="post-content">
                                        <div class="title max">
                                            <a
                                                    href="/tin-tuc/den-hoa-my-thi-cong-den-trang-tri-cho-trung-tam-thuong-mai-aeon-mall-le-chan-hai-phong-n87.html"
                                            >
                                                60+ MẪU ĐÈN TRANG TRÍ PHÒNG KHÁCH CAO CẤP
                                            </a>
                                        </div>
                                        <div class="date">
                                            <i class="fa fa-calendar-alt"></i> 09/01/2024
                                        </div>
                                    </div>
                                </div>

                                <div class="item-article clearfix">
                                    <div class="post-image">
                                        <a href="#">
                                            <img
                                                    class="lazyload"
                                                    src="https://denhoamy.vn/upload/news/16291329den-nghe-thuat-1.jpg"
                                                    alt="Đèn Hoa Mỹ cung cấp đèn trang trí cho Trung tâm thương mại Aeon Mall Lê Chân Hải Phòng"
                                            />
                                        </a>
                                    </div>
                                    <div class="post-content">
                                        <div class="title max">
                                            <a
                                                    href="/tin-tuc/den-hoa-my-thi-cong-den-trang-tri-cho-trung-tam-thuong-mai-aeon-mall-le-chan-hai-phong-n87.html"
                                            >
                                                50+ mẫu đèn trang trí quán cafe độc đáo hot nhất
                                            </a>
                                        </div>
                                        <div class="date">
                                            <i class="fa fa-calendar-alt"></i> 09/01/2024
                                        </div>
                                    </div>
                                </div>

                                <div class="item-article clearfix">
                                    <div class="post-image">
                                        <a href="#">
                                            <img
                                                    class="lazyload"
                                                    src="https://denhoamy.vn/upload/news/2922khuyen-mai-den-trang-tri-giang-sinh-bviet.jpg"
                                                    alt="Đèn Hoa Mỹ cung cấp đèn trang trí cho Trung tâm thương mại Aeon Mall Lê Chân Hải Phòng"
                                            />
                                        </a>
                                    </div>
                                    <div class="post-content">
                                        <div class="title max">
                                            <a
                                                    href="/tin-tuc/den-hoa-my-thi-cong-den-trang-tri-cho-trung-tam-thuong-mai-aeon-mall-le-chan-hai-phong-n87.html"
                                            >
                                                Khuyến mãi đèn trang trí nghệ thuật mừng ngày quốc
                                                tế phụ nữ 8-3
                                            </a>
                                        </div>
                                        <div class="date">
                                            <i class="fa fa-calendar-alt"></i> 09/01/2024
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-md-8 col-lg-9">
                        <div class="shop_grid_product_area">
                            <div class="row">
                                <div class="col-12">
                                    <div
                                            class="product-topbar d-flex align-items-center justify-content-between"
                                    >

                                        <!-- Sorting -->
                                        <div class="product-sorting d-flex align-items-center">
                                            <p>Sắp xếp :</p>
                                            <form action="CategoryController" method="get" id="sortingForm">
                                                <!-- Giữ subCategoryId nếu có -->
                                                <c:if test="${not empty subCategoryId}">
                                                    <input type="hidden" name="subCategoryId" value="${subCategoryId}" />
                                                </c:if>

                                                <!-- Giữ page hiện tại -->
                                                <input type="hidden" name="page" value="${currentPage}" />

                                                <!-- Dropdown sắp xếp -->
                                                <select name="select" id="sortByselect" onchange="updateSorting()">
                                                    <option value="">-- Chọn sắp xếp --</option>
                                                    <option value="price_desc" ${select == 'price_desc' ? 'selected' : ''}>Giá từ cao đến thấp</option>
                                                    <option value="price_asc" ${select == 'price_asc' ? 'selected' : ''}>Giá từ thấp đến cao</option>
                                                    <option value="name_asc" ${select == 'name_asc' ? 'selected' : ''}>A->Z</option>
                                                    <option value="name_desc" ${select == 'name_desc' ? 'selected' : ''}>Z->A</option>
                                                </select>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <c:forEach var="product" items="${products}">
                                    <div class="col-6 col-md-4 col-lg-3 mb-4">
                                        <div class="product-item">
                                            <a href="product-detail?id=${product.id}">
                                                <div class="img">
                                                    <img src="${product.imageUrl}" alt="${product.productName}" />
                                                </div>
                                                <div class="product-info">
                                                    <div class="product-name">
                                                            ${product.productName}
                                                    </div>
                                                    <p class="original-price">
                                                            ${String.format('%,.0f', product.unitPrice)} VND
                                                    </p>
                                                    <div class="price-discount">
                                                        <p class="product-price">
                                                                ${String.format('%,.0f', product.discountedPrice)} VND
                                                        </p>
                                                        <p class="discount-percentage">
                                                            -${product.discountPercent}%
                                                        </p>
                                                    </div>
                                                </div>
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <nav aria-label="Page navigation example">
                                <ul class="pagination justify-content-center">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" data-page="${currentPage - 1}">
                                                &laquo;
                                            </a>
                                        </li>
                                    </c:if>
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" data-page="${i}">
                                                    ${i}
                                            </a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" data-page="${currentPage + 1}">
                                                &raquo;
                                            </a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <!-- footer -->
    <jsp:include page="../reuse/footer.jsp" />
</div>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        function toggleCategoryMenu() {
            const urlParams = new URLSearchParams(window.location.search);
            const currentSubCategoryId = urlParams.get('subCategoryId'); // Lấy subcategoryId từ URL

            // Lấy tất cả các danh mục chính
            const categoryItems = document.querySelectorAll(".cate-item");

            categoryItems.forEach((category) => {
                // Thêm sự kiện click cho từng danh mục
                category.addEventListener("click", function (event) {
                    event.preventDefault(); // Ngăn hành vi mặc định

                    // Thu lại tất cả các danh mục con khác
                    const allSubcategoryLists = document.querySelectorAll(".subcategory-list");
                    allSubcategoryLists.forEach((subcategoryList) => {
                        subcategoryList.style.height = "0"; // Thu lại tất cả danh mục con
                    });

                    // Xóa trạng thái active của tất cả icon
                    const allIcons = document.querySelectorAll(".cate-item i");
                    allIcons.forEach((icon) => {
                        icon.classList.remove("active");
                    });

                    // Mở danh mục con của danh mục được nhấn
                    const subcategoryList = this.parentElement.querySelector(".subcategory-list");
                    if (subcategoryList) {
                        const isVisible = subcategoryList.style.height === "auto";
                        subcategoryList.style.height = isVisible ? "0" : "auto";

                        // Thêm icon trạng thái cho danh mục cha đang mở
                        const icon = this.querySelector("i");
                        if (icon) {
                            icon.classList.toggle("active");
                        }
                    }
                });

                // Tự động mở rộng danh mục cha và tô đậm danh mục con nếu subcategoryId khớp
                const subcategoryList = category.parentElement.querySelector(".subcategory-list");
                if (subcategoryList) {
                    const subCategoryItems = subcategoryList.querySelectorAll("a");
                    subCategoryItems.forEach((subCategory) => {
                        if (subCategory.dataset.id === currentSubCategoryId) {
                            // Mở rộng danh mục cha
                            subcategoryList.style.height = "auto";

                            // Tô đậm danh mục con
                            subCategory.classList.add("active");

                            // Thêm icon trạng thái
                            const icon = category.querySelector("i");
                            if (icon) {
                                icon.classList.add("active");
                            }
                        }
                    });
                }
            });
        }

        toggleCategoryMenu();

        // Kiểm tra sự kiện 'prev'
        const prevButton = document.getElementById('prev');
        if (prevButton) {
            prevButton.addEventListener("click", function () {
                console.log('Previous button clicked');
            });
        } else {
            console.log("Prev button not found");
        }

    });
    // Hàm xử lý sự kiện khi thay đổi lựa chọn

    function submitForm() {
            const selectElement = document.getElementById("sortByselect");
            if (selectElement.value === "") {
            // Nếu không có giá trị sắp xếp, không thêm tham số "select"
            selectElement.removeAttribute("name");
        }
            document.getElementById("sortingForm").submit();
    }

    function updateSorting() {
        const form = document.getElementById("sortingForm");
        const selectElement = document.getElementById("sortByselect");
        const url = new URL(window.location.href);

        // Nếu giá trị của select rỗng, xóa tham số khỏi URL
        if (!selectElement.value) {
            url.searchParams.delete("select");
        } else {
            // Nếu có giá trị, thêm hoặc cập nhật tham số
            url.searchParams.set("select", selectElement.value);
        }

        // Đặt page về 1 khi thay đổi sắp xếp
        url.searchParams.set("page", 1);

        // Chuyển hướng tới URL mới
        window.location.href = url.toString();
    }

    function createPageUrl(page) {
        const url = new URL(window.location.href);
        url.searchParams.set('page', page); // Thêm hoặc cập nhật tham số page
        return url.toString();
    }

    // Áp dụng vào các nút phân trang
    document.querySelectorAll('.page-link').forEach(link => {
        const page = link.dataset.page; // Giá trị trang từ thuộc tính data-page
        link.href = createPageUrl(page);
    });
</script>
</body>
</html>