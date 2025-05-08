<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    HttpSession httpSession  = request.getSession(false); // Lấy session hiện tại (nếu có)
    String username = (String) httpSession.getAttribute("username");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180">
    <title>Trang chủ</title>
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
            type="text/css"
            href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css"
    />
<%--    <link rel="stylesheet" type="text/css"--%>
<%--          href="${pageContext.request.contextPath}/assets/css/reset.css">--%>

<%--    <link rel="stylesheet" type="text/css"--%>
<%--          href="${pageContext.request.contextPath}/assets/css/header-footer.css">--%>
<%--    <link rel="stylesheet" type="text/css"--%>
<%--          href="${pageContext.request.contextPath}/assets/css/home.css">--%>
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

        .wrapper {
            min-height: 100vh;
            padding-top: 110px;
        }

        a {
            text-decoration: none !important;
            color: black;
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
            background-color: #fff;
            padding: 0 10px;
        }
        .header .header-top .search-bar #search-form {
            display: flex;
            justify-content: space-between;
        }

        .header .header-top .search-bar input {
            border: none;
            outline: none;
            padding: 5px;
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

        .header .navbar .navbar-list > li > a {
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

        .header .navbar-list .dropdown .category-item a:hover {
            color: #e30019;
            cursor: pointer;
            border-radius: 4px;
        }


        .header .navbar-list .dropdown .category-item a {
            text-decoration: none;
            color: black;
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

        /* End Header */

        /* Slider */

        .slider {
            width: 900px;
        }


        .slider-wrapper {
            width: 100%;
            height: 100%;
        }


        .slider-item {
            width: 100%;
            height: 100%;
            flex: 1 0 100%;
            user-select: none;
        }

        .slick-prev {
            position: absolute;
            top: 50%;
            left: 10px;
            border-radius: 100%;
            transform: translateY(-50%);
            color: #404033;
            background-color: #eee;
            border: none;
            z-index: 2;
            width: 40px;
            height: 40px;
        }

        .slick-next {
            position: absolute;
            top: 50%;
            right: 10px;
            border-radius: 100%;
            transform: translateY(-50%);
            color: #404033;
            background-color: #eee;
            border: none;
            z-index: 2;
            width: 40px;
            height: 40px;
        }

        .slick-dots {
            position: absolute;
            bottom: 10px;
            left: 50%;
            transform: translateX(-50%);
            display: flex !important;
            align-items: center;
            justify-self: center;
            gap: 15px;
        }

        .slick-dots button {
            font-size: 0;
            background-color: #ccc;
            width: 8px;
            border-radius: 100%;
            height: 8px;
            transition: width 0.3s ease, background-color 0.3s ease, border-radius 0.3s ease;
        }

        .slick-dots .slick-active button {
            width: 20px;
            border-radius: 4px;
            background-color: var(--color-yellow-dark);
        }

        .product-wrapper .slick-prev {
            left: -20px;
        }

        .product-wrapper .slick-next {
            right: -20px;
        }

        .wrapper .container {
            max-width: 1280px;
            padding: 0 20px;
            margin: 0 auto;
            display: flex;
            gap: 20px;
        }

        .container .banner {
            width: 420px;
            height: 471px;
        }

        .container .banner-top {
            height: 230px;
            margin-bottom: 10px;
        }

        .container .banner-top img {
            height: 100%;
            width: 100%;
            object-fit: cover;
        }

        .container .banner-bottom {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
            color: white;
        }
        .container .banner-bottom .box {
            display: flex;
            align-items: center;
            padding: 10px;
            background-color: #000;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .container .banner-bottom .box i {
            font-size: 20px;
            color: #007bff;
            margin-right: 7px;
        }

        .container .banner-bottom .box-desc {
            display: flex;
            flex-direction: column;
        }

        .container .banner-bottom .box-desc .title {
            font-size: 12px;
            font-weight: 600;
            color: var(--color-yellow);
            margin-bottom: 10px;
        }

        .container .banner-bottom .box-desc p {
            font-size: 0.9rem;
            color: #555;
            margin: 0;
        }


        .wrapper .product-wrapper .wrapper-title {
            width: 100%;
            padding: 16px 0;
            font-weight: 600;
            color: black;
            background-color: var(--color-yellow-dark);
        }

        .wrapper .product-wrapper .list-product-hot,
        .container-sale .list-product-hot {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 5px;
            background-color: #fff;
            padding: 10px;
            border-radius: 4px;
            width: 100%;
        }

        .wrapper .product-wrapper .list-product-hot .product-hot-item,
        .container-sale .list-product-hot .product-hot-item {
            padding: 10px;
            background-color: #fff;
            border: 2px solid #ededed;
            border-radius: 4px;
            margin: 0 10px;
            cursor: pointer;
        }

        .wrapper .product-wrapper .product-hot-item .product-info,
        .container-sale, .product-hot-item .product-info{
            margin-top: 18px;
            text-align: left;
        }

        .wrapper .product-wrapper .product-hot-item .img img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .wrapper .product-wrapper .product-info .product-price,
        .product-info .product-price{
            margin-top: 6px;
            color: red;
            font-weight: 500;
            font-size: 15px;
        }

        .product-hot-item .product-info .product-name {
            height: 40px;
            line-height: 1.5em;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
            margin-bottom: 12px;
        }

        .wrapper .container .product-wrapper {
            margin-top: 60px;
            width: 100%;
            text-align: center;
        }

        .wrapper .home-product .product-wrapper {
            padding: 10px;
            background-color: #fff;
            border-radius: 4px;
            border: 1px solid #e6e6e6;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
        }

        .home-product .product-wrapper .wrapper-top {
            padding: 16px 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .home-product .product-wrapper .wrapper-top a {
            text-decoration: none;
            color: #000;
            font-weight: 400;
            font-size: 14px;
        }

        .home-product .product-wrapper .wrapper-top a:hover {
            color: #e30019;
        }

        .home-product .product-wrapper .wrapper-top .wrapper-category {
            font-weight: 600;
        }

        .list-product {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            background-color: #fff;
            padding: 10px;
            border-radius: 4px;
        }

        .list-product .product-item {
            padding: 10px;
            width: calc(20% - 16px);
            background-color: #fff;
            border: 2px solid #ededed;
            border-radius: 4px;
            cursor: pointer;
        }

        .list-product .product-item .product-name {
            height: 40px;
            line-height: 1.5em;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
            margin-bottom: 12px;
        }


        .list-product .product-item .img,
        .list-product-hot .product-hot-item .img {
            overflow: hidden;
            height: 230px;
        }

        .list-product .product-item .img img,
        .list-product-hot .product-hot-item .img img  {
            transition: transform 0.3s ease;
            height: 100%;
            width: 100%;
        }

        .list-product .product-item .img:hover img,
        .list-product-hot .product-hot-item .img:hover img {
            transform: scale(1.1);
        }
        .list-product .product-item .product-info {
            margin-top: 18px;
            text-align: left;
        }

        .product-info .original-price {
            font-size: 12px;
            color: #888;
            text-decoration: line-through;
            margin-top: 8px;
            text-align: left;
        }

        .product-info .price-discount {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .product-info .discount-percentage {
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

        .home-info {
            margin-top: 30px;
        }

        .home-info .info-wrapper {
            margin-top: 40px;
            width: 100%;
        }

        .home-info .info-wrapper .header-info {
            text-align: center;
            font-weight: 600;
            font-size: 18px;
            margin-bottom: 12px;
        }

        .home-info .info-wrapper .list-info {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            padding: 10px;
        }

        .home-info .info-wrapper .list-info .info-item {
            padding: 16px;
            background-color: #fff;
            border-radius: 4px;
            width: calc(25% - 16px);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .home-info .info-wrapper .list-info .info-item .info-img img {
            border-radius: 4px;
            width: 257px;
            height: 257px;
        }

        .home-info .info-wrapper .list-info .info-item:hover {
            transform: translateY(-4px);
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
        }

        .home-info .info-wrapper .list-info .info-main {
            margin-top: 20px;
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .home-info .info-wrapper .list-info .info-main .info-name {
            font-size: 16px;
            font-weight: 600;
            color: #171c24;
            line-height: 1.4;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .home-info .info-wrapper .list-info .info-main .info-time {
            font-size: 12px;
            color: #6c6c6c;
            margin-left: 10px;
            margin-bottom: 10px;
        }

        .home-info .info-wrapper .list-info .info-main p {
            font-size: 13px;
            color: #848484;
            line-height: 1.2;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
            text-align: justify;
        }


        .main .feedback {
            width: 100%;
            margin-top: 60px;
            padding: 20px;
            border-radius: 10px;
        }

        .main .feedback .feedback-title {
            font-size: 18px;
            margin-bottom: 30px;
            text-align: center;
        }

        .feedback-list {
            display: block; /* Bỏ Flexbox ở đây */
            max-width: 1000px;
            height: auto;
            margin: 0 auto;
            border-radius: 10px;
        }

        .feedback-list .feedback-item {
            width: 100%; /* Đảm bảo Slick Slider có thể điều khiển chiều rộng */
            padding: 20px;
            border-radius: 10px;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            display: block; /* Đảm bảo item hiển thị theo block */
        }

        .feedback-list .feedback-img {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            overflow: hidden;
            float: left;
            margin-right: 30px;
        }

        .feedback-list .feedback-content {
            width: auto;
            text-align: justify;
        }

        .feedback-list .feedback-content .user-name {
            margin-bottom: 10px;
            font-weight: 600;
            font-size: 16px;
        }

        .feedback-list .feedback-content .feedback-text {
            line-height: 1.6;
            font-size: 14px;
        }

        .feedback-list .feedback-img img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .feedback ul .slick-prev{
            margin-left: -70px;
        }

        .feedback ul .slick-next{
            margin-right: -70px;
        }

        .feedback .slick-dots {
            bottom: -40px;
        }

        .bar {
            width: 50px;
            height: 4px;
            border-radius: 3px;
            background-color: var(--color-yellow-dark);
            margin: 5px auto;
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

        .sale-info {
            font-size: 18px;
            color: #ff5722;
        }

        .sale-info h2 {
            font-size: 24px;
            font-weight: 600;
            margin: 0;
            color: black;
        }

        .container-sale {
            margin-top: 80px !important;
            border-radius: 4px;
            width: 100%;
        }

        .container-sale-header {
            padding: 4px 16px;
            display: flex;
            align-items: center;
            background-color: var(--color-yellow-dark);
            gap: 20px;
        }

        .countdown-timer {
            display: flex;
            width: 50%;
        }

        .time-box {
            display: flex;
            justify-content: center;
            flex-direction: column;
            align-items: center;
            background-color: #fff;
            color: #000;
            padding: 10px 8px;
            border-radius: 8px;
            margin: 0 8px;
            font-size: 20px;
            font-weight: bold;
            height: 45px;
        }

        .time-box p {
            font-size: 16px;
            font-weight: 600;
        }

        .time-box span {
            font-size: 10px;
            margin-top: 5px;
            font-weight: 400;
        }

        .container-sale .slick-prev {
            left: -20px;
        }

        .container-sale .slick-next {
            right: -20px;
        }

        .main .slider-container {
            width: 100%;
            background-color: #fff;
            padding: 15px 0;
        }




    </style>
</head>
<body>
<div class="wrapper">
    <!-- Header -->
    <jsp:include page="/reuse/header.jsp" />
    <div class="main">
        <!-- Slider -->
        <div class="slider-container">
            <div class="container">
                <div class="slider">
                    <div class="slider-wrapper">
                        <div class="slider-item">
                            <div class="slider-image">
                                <img
                                        src="https://denhoamy.vn/upload/partner/9303the-gioi-den-nghe-thuat.png"
                                        alt=""
                                />
                            </div>
                        </div>
                        <div class="slider-item">
                            <div class="slider-image">
                                <img
                                        src="https://denhoamy.vn/upload/partner/3103noi-that-thong-minh.png"
                                        alt=""
                                />
                            </div>
                        </div>
                        <div class="slider-item">
                            <div class="slider-image">
                                <img
                                        src="https://denhoamy.vn/upload/partner/2247banner-the-gioi-den-nghe-thuat-va-noi-that-trang-tri.png"
                                        alt=""
                                />
                            </div>
                        </div>
                        <div class="slider-item">
                            <div class="slider-image">
                                <img
                                        src="https://denhoamy.vn/upload/partner/3470benner-the-gioi-den-nghe-thuat-va-noi-that-trang-tri-den-hoa-my-vn-1.png"
                                        alt=""
                                />
                            </div>
                        </div>
                    </div>
                </div>

                <div class="banner">
                    <div class="banner-top">
                        <img
                                src="https://denhoamy.vn/upload/partner/7850den-hoa-my-dia-chi-mua-den-uy-tin-tai-hai-phong.jpg"
                                alt=""
                        />
                    </div>
                    <div class="banner-bottom">
                        <div class="box">
                            <i class="fas fa-gem"></i>
                            <div class="box-desc">
                                <div class="title">Mẫu mới đa dạng</div>
                                <p>Luôn đi đầu xu hướng sản phẩm</p>
                            </div>
                        </div>

                        <div class="box">
                            <i class="fa-solid fa-truck-fast"></i>
                            <div class="box-desc">
                                <div class="title">Shipcode toàn quốc</div>
                                <p>Vận chuyển giá rẻ, nhanh chóng, tiết kiệm</p>
                            </div>
                        </div>

                        <div class="box">
                            <i class="fa-solid fa-wrench"></i>
                            <div class="box-desc">
                                <div class="title">Tư vấn mua hàng và lắp đặt 24/7</div>
                                <p>Nhận đặt làm các mẫu theo thiết kế</p>
                            </div>
                        </div>

                        <div class="box">
                            <i class="fa-solid fa-money-bill-transfer"></i>
                            <div class="box-desc">
                                <div class="title">Hoàn trả 100%</div>
                                <p>Một đổi một trong vòng 7 ngày</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <c:forEach var="promotion" items="${listPromotion}">
            <div class="container">
                <div class="container-sale">
                    <div class="container-sale-header">
                        <div class="sale-info">
                            <h2>${promotion.promotionName}</h2>
                        </div>
                        <div class="countdown-timer" data-end-time="${promotion.endDay}">
                            <div class="time-box">
                                <p id="days-${promotion.id}">00</p>
                                <span>Ngày</span>
                            </div>
                            <div class="time-box">
                                <p id="hours-${promotion.id}">00</p>
                                <span>Giờ</span>
                            </div>
                            <div class="time-box">
                                <p id="minutes-${promotion.id}">00</p>
                                <span>Phút</span>
                            </div>
                            <div class="time-box">
                                <p id="seconds-${promotion.id}">00</p>
                                <span>Giây</span>
                            </div>
                        </div>
                    </div>
                    <div class="container-sale-product">
                        <ul class="list-product-hot">
                            <c:forEach var="product" items="${promotion.products}">
                                <li class="product-hot-item">
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
                                                        ${String.format('%,.0f', product.unitPrice * (1 - product.discountPercent / 100))} VND
                                                </p>
                                                <p class="discount-percentage">
                                                    -${product.discountPercent}%
                                                </p>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>

        <!-- BestSeller - Products -->
        <div class="container">
            <div class="product-wrapper">
                <div class="wrapper-title">SẢN PHẨM BÁN CHẠY</div>
                <ul class="list-product-hot">
                    <c:forEach var="product" items="${listHotProduct}">
                        <li class="product-hot-item">
                            <a href="product-detail?id=${product.id}">
                                <div class="img">
                                    <div class="img">
                                        <img src="${product.imageUrl}" alt="${product.productName}" />
                                    </div>
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
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>


        <!-- Home Products -->

        <c:forEach var="category" items="${categories}">
            <section class="home-product">
                <div class="container">
                    <div class="product-wrapper">
                        <div class="wrapper-top">
                            <div class="wrapper-category">${category.categoryName}</div>
                            <a href="/SystemSecurity_war/CategoryController?categoryId=${category.id}">Xem thêm</a>
                        </div>
                        <div class="wrapper-bottom">
                            <ul class="list-product" id="list-product-${category.id}">
                                <c:forEach var="product" items="${productsByCategoryMap[category.id]}">
                                    <li class="product-item">
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
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </section>
        </c:forEach>


        <!-- Tin tức -->
        <section class="home-info">
            <div class="container">
                <div class="info-wrapper">
                    <div class="header-info">
                        TIN TỨC
                        <div class="bar"></div>
                    </div>
                    <div class="list-info">
                        <div class="info-item">
                            <div class="info-img">
                                <img
                                        src="https://denhoamy.vn/upload/news/5157sofa-da-nhap-khau-cao-cap-trang-tri-noi-that-phong-khach-phong-cach-hien-dai-nt00401-01.jpg"
                                        alt=""
                                />
                            </div>

                            <div class="info-main">
                                <div class="info-name">
                                    999+MẪU THIẾT KẾ NỘI THẤT ĐẸP - SANG TRỌNG - CAO CẤP XU
                                    HƯỚNG MỚI NHẤT NĂM
                                </div>
                                <div class="info-time">
                                    <i class="fa-regular fa-clock"></i>
                                    Saturday, June 01, 2024
                                </div>
                                <p>
                                    Nội thất hay còn gọi là vật dụng, thiết bị nội thất được
                                    xem là điểm nhấn trang trí ấn tượng ở không gian phòng
                                    ngủ, phòng khách, phòng bếp hay văn phòng công ty.
                                </p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-img">
                                <img
                                        src="https://denhoamy.vn/upload/news/32475008den-chum-pha-le-trang-tri-thong-tang-phong-khach-dc03620-03.jpg"
                                        alt=""
                                />
                            </div>

                            <div class="info-main">
                                <div class="info-name">
                                    +99 MẪU ĐÈN TRANG TRÍ PHA LÊ CAO CẤP
                                </div>
                                <div class="info-time">
                                    <i class="fa-regular fa-clock"></i>
                                    Monday, Sep 01, 2024
                                </div>
                                <p>
                                    Đèn chùm pha lê được trang trí rộng rãi trong các công trình nhằm tăng độ chiếu sáng và tính thẩm mỹ c
                                </p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-img">
                                <img
                                        src="https://denhoamy.vn/upload/news/161tong-hop-cac-mau-quat-tran-den-cho-khong-gian-biet-thu-dep.png"
                                        alt=""
                                />
                            </div>

                            <div class="info-main">
                                <div class="info-name">
                                    Tổng hợp các mẫu quạt trần đèn cho không gian biệt thự đẹp
                                </div>
                                <div class="info-time">
                                    <i class="fa-regular fa-clock"></i>
                                    Sunday, Nov 01, 2023
                                </div>
                                <p>
                                    Hiện nay việc sử dụng quạt trần đèn cho không gian biệt thự cao cấp là điều rất cần thiết để làm tăng lên sự sang trọng, đẳng cấp cho không gian thiết kế này.
                                </p>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-img">
                                <img
                                        src="https://denhoamy.vn/upload/news/1333184den-quat-sai-canh-trang-tri-noi-that-phong-cach-trung-hoa-dq00684.jpg"
                                        alt=""
                                />
                            </div>

                            <div class="info-main">
                                <div class="info-name">
                                    Đèn quạt trần – sản phẩm nội thất thông minh cho mọi gia đình
                                </div>
                                <div class="info-time">
                                    <i class="fa-regular fa-clock"></i>
                                    Tuesday, July 01, 2024
                                </div>
                                <p>
                                    Chạy theo sự phát triển của thời đại, nội thất thông minh ngày càng trở thành xu hướng và được ưa chuộng. Trong đó đèn quạt trần là món đồ trang trí mang tính ứng dụng cao.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="container">
            <div class="feedback">
                <h2 class="feedback-title">
                    ĐÁNH GIÁ KHÁCH HÀNG
                    <div class="bar"></div>
                </h2>
                <ul class="feedback-list">
                    <li class="feedback-item">
                        <div class="feedback-img">
                            <img
                                    src="https://images.unsplash.com/photo-1543610892-0b1f7e6d8ac1?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8YXZhdGFyfGVufDB8fDB8fHww"
                                    alt="User Image"
                            />
                        </div>
                        <div class="feedback-content">
                            <h3 class="user-name">Nguyễn Minh Tú</h3>
                            <p class="feedback-text">
                                "Trang web này thật sự đã mang đến cho tôi một trải nghiệm
                                tuyệt vời. Giao diện dễ sử dụng, sản phẩm đa dạng và chất
                                lượng rất tốt. Tôi đặc biệt ấn tượng với chính sách giao
                                hàng nhanh chóng và dịch vụ hỗ trợ khách hàng thân thiện.
                                Tôi sẽ quay lại và giới thiệu cho bạn bè!"
                            </p>
                        </div>
                    </li>

                    <li class="feedback-item">
                        <div class="feedback-img">
                            <img
                                    src="https://plus.unsplash.com/premium_photo-1671656349322-41de944d259b?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D"
                                    alt="User Image"
                            />
                        </div>
                        <div class="feedback-content">
                            <h3 class="user-name">Lê Anh Tuấn</h3>
                            <p class="feedback-text">
                                "Tôi thực sự hài lòng với trang web này. Các sản phẩm luôn
                                có thông tin chi tiết và dễ dàng tìm kiếm. Tôi đặc biệt
                                thích giao diện dễ sử dụng và khả năng duyệt qua các sản
                                phẩm nhanh chóng. Dịch vụ chăm sóc khách hàng rất tuyệt vời
                                và luôn sẵn sàng hỗ trợ tôi khi có vấn đề."
                            </p>
                        </div>
                    </li>

                    <li class="feedback-item">
                        <div class="feedback-img">
                            <img
                                    src="https://plus.unsplash.com/premium_photo-1702598525684-fe2f8b75eb62?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDV8fHxlbnwwfHx8fHw%3D"
                                    alt="User Image"
                            />
                        </div>
                        <div class="feedback-content">
                            <h3 class="user-name">Bùi Minh Anh</h3>
                            <p class="feedback-text">
                                "Mua sắm trực tuyến thật dễ dàng với trang web này. Sản phẩm
                                phong phú, chất lượng tuyệt vời và dịch vụ khách hàng cực kỳ
                                nhanh chóng. Tôi rất vui với việc giao hàng đúng hẹn và sản
                                phẩm đến tay tôi trong tình trạng hoàn hảo. Chắc chắn sẽ
                                quay lại!"
                            </p>
                        </div>
                    </li>

                    <li class="feedback-item">
                        <div class="feedback-img">
                            <img
                                    src="https://images.unsplash.com/photo-1640951613773-54706e06851d?q=80&w=1780&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                                    alt="User Image"
                            />
                        </div>
                        <div class="feedback-content">
                            <h3 class="user-name">Vũ Quang Hải</h3>
                            <p class="feedback-text">
                                "Tôi rất hài lòng với trải nghiệm mua sắm trên trang web
                                này. Giao diện rất dễ hiểu, dễ điều hướng và sản phẩm đa
                                dạng. Dịch vụ chăm sóc khách hàng luôn hỗ trợ nhanh chóng và
                                hiệu quả. Tôi rất mong sẽ có nhiều ưu đãi hấp dẫn hơn trong
                                tương lai."
                            </p>
                        </div>
                    </li>
                </ul>
            </div>
        </section>
    </div>

    <!-- Footer -->
    <jsp:include page="/reuse/footer.jsp" />
</div>
<script src="assets/Js/home.js?v=2.0"></script>
<script
        type="text/javascript"
        src="https://code.jquery.com/jquery-1.11.0.min.js"
></script>
<script
        type="text/javascript"
        src="https://code.jquery.com/jquery-migrate-1.2.1.min.js"
></script>
<script
        type="text/javascript"
        src="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"
></script>
</body>
</html>