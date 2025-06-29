<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.example.webbongden.dao.model.Cart" %>
<%
    HttpSession httpSession  = request.getSession(false); // Lấy session hiện tại (nếu có)
    String username = (String) httpSession.getAttribute("username");
    Cart cart = (Cart) httpSession.getAttribute("cart");
    int totalQuantity = 0;

    if (cart != null) {
        totalQuantity = cart.getTotalQuantity(); // Giả sử bạn đã có phương thức getTotalQuantity() trong lớp Cart
    }
%>
<header class="header">
    <div class="header-top">
        <div class="header-left">
            <!-- Hamburger icon for opening sidebar -->
            <div style="display: flex; align-items: center">
                <i class="fa-solid fa-bars" id="menu-toggle"></i>
                <a href="/SystemSecurity_war/home" class="logo">
                    <img src="./assets/img/logo2.png" alt="Description">
                </a>
            </div>
            <div class="search-bar">
                <form action="/SystemSecurity_war/search" method="GET" id="search-form">
                    <input placeholder="Bạn cần tìm gì?" type="text" name="value" />
                    <button type="submit" aria-label="Search" class="search-btn">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
            </div>
            <div class="mobile-search-bar" id="mobile-search-bar">
                <form action="/search" method="GET">
                    <input type="text" name="query" placeholder="Bạn cần tìm gì?" />
                    <button type="submit" aria-label="mobile-search">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
                <button
                        type="button"
                        id="close-search-button"
                        aria-label="Close Search"
                >
                    <i class="fas fa-times"></i>
                </button>
            </div>
        </div>

        <div class="header-right">
            <div class="info">
                <div class="info-item">
                    <i class="fas fa-headphones-alt"></i>
                    <span>Hotline 1900.5301</span>
                </div>

                <a class="info-item" href="support.html">
                    <i class="fa-solid fa-circle-question"></i>
                    <span>Hỗ trợ</span>
                </a>

                <a class="info-item" href="/SystemSecurity_war/cart">
                    <i class="fas fa-shopping-cart"></i>
                    <span>Giỏ hàng</span>
                    <p>(<span class="quantity-product"><%= totalQuantity %></span>)</p>
                </a>

                <%
                    if (username != null) { // Người dùng đã đăng nhập
                %>
                <div class="header-user" style="display:block;">
                    <img
                            src="https://images.unsplash.com/photo-1636041282694-aa4e52370419?q=80&w=1854&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                            alt=""
                            class="avt-user"
                    />

                    <div class="user-info-dropdown">
                        <div class="dropdown-header">
                            <img src="./assets/img/icon-dropdownuser.png" alt="" />
                            <p>Xin chào <span><%= username != null ? username : "Khách" %>.</span></p>
                        </div>
                        <div class="dropdown-content">
                            <div class="dropdown-item">
                                <i class="fa fa-user"></i>
                                <a href="/SystemSecurity_war/userinfo">Thông tin cá nhân</a>
                            </div>

                            <div class="dropdown-item">
                                <i class="fa-regular fa-eye"></i>
                                <a href="/SystemSecurity_war/userinfo">Đơn hàng gần đây</a>
                            </div>
                        </div>
                        <div class="dropdown-footer">
                            <i class="fa-solid fa-arrow-right-from-bracket"></i>
                            <a href="/SystemSecurity_war/LogoutController">Đăng xuất</a>
                        </div>
                    </div>
                </div>
                <%
                } else { // Người dùng chưa đăng nhập
                %>
                <a href="/SystemSecurity_war/login" id="login-header-btn" style="display: block">
                    <button class="login" id="login-btn">
                        <i class="fas fa-user"></i>
                        <span>Đăng nhập</span>
                    </button>
                </a>
                <%
                    }
                %>
            </div>
        </div>
    </div>

    <nav class="navbar">
        <ul class="navbar-list">
            <li><a href="/SystemSecurity_war/home">TRANG CHỦ</a></li>
            <li class="dropdown">
                <a href="/SystemSecurity_war/CategoryController"
                >DANH MỤC
                    <i class="fa-solid fa-caret-down"></i>
                </a>
                <div class="submenu">
                    <ul id="category-list">
                        <c:forEach var="category" items="${categories}">
                            <li class="category">
                                <p class="category-header">${category.categoryName}</p>
                                <ul class="category-products">
                                    <!-- Hiển thị danh mục con -->
                                    <c:forEach var="subCategory" items="${subCategoriesMap[category.id]}">
                                        <li class="category-item">
                                            <a href="/SystemSecurity_war/CategoryController?categoryId=${category.id}&subCategoryId=${subCategory.id}" class="category-link">${subCategory.name}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </li>
            <li><a href="News.html">TIN TỨC</a></li>
            <li><a href="Contact.html">LIÊN HỆ</a></li>
            <li><a href="https://drive.usercontent.google.com/download?id=1m7sxy13MU5OJGIK9pwExP3F2pRcaXkTk&export=download&authuser=0">TẢI ỨNG DỤNG</a><li>
            <li><a href="https://drive.google.com/uc?export=download&id=1dWRiMQDHrvB7bqYzksNxVGURNFq4cvd6
">Tải tool kí xác thực </a></li>
        </ul>
    </nav>
</header>
