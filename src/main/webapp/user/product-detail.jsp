<%@ page import="com.example.webbongden.dao.model.Account" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/15/2024
  Time: 10:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%
    HttpSession accountSession = request.getSession();
    Account account = (Account) accountSession.getAttribute("account");
    int accountId = account != null ? account.getId() : -1; // -1 nếu chưa đăng nhập
%>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180" />
    <title>Chi tiết sản phẩm</title>
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
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/slick-carousel/slick/slick.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/header-footer.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/product.css">
</head>
<body>
<div class="wrapper">
    <jsp:include page="../reuse/header.jsp" />
    <div class="main">
        <div class="container">
            <div class="breadcrumb">
                <i class="fa-solid fa-house"></i>
                <a href="/SystemSecurity_war/home">Trang chủ</a>
                <span class="separator">›</span>
                <span>${breadCum}</span>
            </div>


            <div class="product-info-container">
                <div class="left-container">
                    <div class="product-view">
                        <div class="product-image-list">
                            <c:forEach var="imageUrl" items="${listImg}">
                                <div class="product-image">
                                    <img src="${imageUrl}" alt="Hình ảnh sản phẩm" />
                                </div>
                            </c:forEach>
                        </div>

                        <div class="product-detail">
                            <div class="product-name">
                                ${productDetail.getProductName()}"
                            </div>

                            <div class="product-status">
                                <span class="product-code">Id: ${productDetail.getId()}</span>
                                <span class="product-availability"
                                >Tình trạng:  ${productDetail.getProductStatus()}</span
                                >
                                <span class="product-reviews"
                                >Đánh giá: <span class="star-review"> ${productDetail.getRating()}</span>/5</span
                                >
                            </div>
                            <p class="product-price">${productDetail.getFormattedDiscountedPrice()} VND</p>

                            <!-- Phần mã giảm giá đặt ngay dưới giá sản phẩm -->

                            <div class="product-desc">
                                <ul>
                                    <li>${productDetail.getProductName()}</li>
                                    <li>
                                        ${productDetail.getDescription()}
                                    </li>
                                    <li>
                                        Bóng Led ánh sáng: ${productDetail.getLightColor()}<br>
                                        Tuổi thọ  bóng trung bình: ${productDetail.getUsageAge()} <br>
                                        Điện áp: ${productDetail.getVoltage()}
                                    </li>
                                    <li>Chất liệu: ${productDetail.getMaterial()}</li>
                                </ul>
                            </div>

                            <div class="product-detail-bottom">
                                <button class="buy-product" id="addToCart" data-id = ${productDetail.getId()}>MUA NGAY</button>
                            </div>
                            <div id="cartNotification" class="notification">
                                Bạn đã thêm vào giỏ hàng thành công!
                            </div>
                        </div>
                    </div>

                    <div class="product-info">
                        <div class="left-product-info">
                            <div class="product-main">
                                <div class="product-main-head">
                                    <p class="title" style="text-align: center !important">
                                        CHI TIẾT SẢN PHẨM
                                    </p>
                                </div>
                                <div class="product-main-detail">
                                    <ul class="policy-list">
                                        <li>
                                            <i class="fa-solid fa-check"></i>
                                            <strong>Chính sách bảo hành</strong>
                                            <p>Đèn Hoa Mỹ bảo hành "Mạch điều khiển" 24 tháng.</p>
                                            <p>
                                                Còn thân đèn, bóng đèn quý khách kiểm tra thực tế
                                                trước khi nhận hàng.
                                            </p>
                                            <p>
                                                Bảo hành tất cả những lỗi do thiết kế, kỹ thuật của
                                                nhà sản xuất. Không bảo hành những lỗi do người sử
                                                dụng gây ra.
                                            </p>
                                        </li>
                                        <li>
                                            <i class="fa-solid fa-check"></i>
                                            <strong>Chính sách đổi trả</strong>
                                            <p>Giao nhầm mẫu</p>
                                            <p>Hàng bị móp méo, vỡ</p>
                                        </li>
                                        <li>
                                            <i class="fa-solid fa-check"></i>
                                            <strong>Chính sách bán hàng</strong>
                                            <p>
                                                Nhận đặt làm theo thiết kế, lắp đặt, bán sỉ, bán lẻ
                                                các sản phẩm đèn nghệ thuật, đèn trang trí, đồ nội
                                                thất cao cấp.
                                            </p>
                                            <p>Giao hàng và thanh toán tại nhà trên toàn quốc.</p>
                                        </li>
                                    </ul>

                                    <div class="product-main-desc">
                                        Đèn Hoa Mỹ - Thế giới đèn nghệ thuật không chỉ phân
                                        phối, bán lẻ Đèn thả trang trí, đèn ốp trần, đèn chùm,
                                        đèn bàn, đèn vách, đèn bàn, đèn cây, đèn quạt tại các
                                        thành phố lớn như Hải Phòng, Hà Nội và TP HCM mà còn tại
                                        nhiều tỉnh thành khác trên toàn quốc như: Cần Thơ, Đà
                                        Nẵng, Hải Phòng, An Giang, Bà Rịa – Vũng Tàu, Bạc Liêu,
                                        Bắc Giang, Bắc Kạn, Bắc Ninh, Bến Tre, Bình Dương, Bình
                                        Định, Bình Phước, Bình Thuận, Cà Mau, Cao Bằng, Đắk Lắk,
                                        Đắk Nông, Điện Biên, Đồng Nai, Đồng Tháp, Gia Lai, Hà
                                        Giang, Hà Nam, Hà Tĩnh, Hải Dương, Hậu Giang, Hòa Bình,
                                        Hưng Yên, Khánh Hòa, Kiên Giang, Kon Tum, Lai Châu, Lạng
                                        Sơn, Lào Cai, Lâm Đồng, Long An, Nam Định, Nghệ An, Ninh
                                        Bình, Ninh Thuận, Phú Thọ, Phú Yên, Quảng Bình, Quảng
                                        Nam, Quảng Ngãi, Quảng Ninh, Quảng Trị, Sóc Trăng, Sơn
                                        La, Tây Ninh, Thái Bình, Thái Nguyên, Thanh Hóa, Thừa
                                        Thiên – Huế, Tiền Giang, Trà Vinh, Tuyên Quang, Vĩnh
                                        Long, Vĩnh Phúc, Yên Bái.
                                    </div>
                                </div>
                            </div>
                            <div class="product-main">
                                <div class="product-main-head">
                                    <div class="title">SẢN PHẨM LIÊN QUAN</div>
                                    <p>Xem tất cả</p>
                                </div>
                                <ul class="list-product">
                                    <c:forEach var="product" items="${relatedProducts}">
                                        <li class="product-item">
                                            <a href="product-detail?id=${product.id}">
                                            <div class="img">
                                                <img src="${product.imageUrl}" alt="${product.productName}" />
                                            </div>
                                            <div class="product-info2">
                                                <div class="product-name">${product.productName}</div>
                                                <p class="original-price">
                                                        ${String.format('%,.0f', product.unitPrice)} VND
                                                </p>
                                                <div class="price-discount">
                                                    <p class="product-price">
                                                            ${String.format('%,.0f', product.discountedPrice)} VND
                                                    </p>
                                                    <p class="discount-percentage">${product.discountPercent}%</p>
                                                </div>
                                            </div>
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="right-container">
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
                                                <a href="/SystemSecurity_war/CategoryController?categoryId=${category.id}&subCategoryId=${subCategory.id}">
                                                    <span>${subCategory.name}</span>
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                    <div class="news">
                        <div class="news-title">TIN TỨC</div>

                        <div class="list-new">
                            <div class="new-item">
                                <div class="img">
                                    <img
                                            src="https://denhoamy.vn/upload/news/3018den-aeon-mall-le-chan-hai-phong-1.jpg"
                                            alt=""
                                    />
                                </div>
                                <div class="new-info">
                                    <div class="new-title">
                                        Đèn hoa mỹ cung cấp đèn trang trí
                                    </div>
                                    <p class="new-time">
                                        <i class="fa-regular fa-clock"></i>
                                        Saturday, June 01, 2024
                                    </p>
                                </div>
                            </div>

                            <div class="new-item">
                                <div class="img">
                                    <img
                                            src="https://denhoamy.vn/upload/news/5664screenshot1622455555.png"
                                            alt=""
                                    />
                                </div>
                                <div class="new-info">
                                    <div class="new-title">
                                        Những mẫu đèn chùm pha lê hiện đại sang trọng
                                    </div>
                                    <p class="new-time">
                                        <i class="fa-regular fa-clock"></i>
                                        Saturday, June 01, 2024
                                    </p>
                                </div>
                            </div>

                            <div class="new-item">
                                <div class="img">
                                    <img
                                            src="https://denhoamy.vn/upload/news/54743470den-op-tran-hien-dai-19097.jpg"
                                            alt=""
                                    />
                                </div>
                                <div class="new-info">
                                    <div class="new-title">
                                        60+ mẫu đèn trang trí phòng khách
                                    </div>
                                    <p class="new-time">
                                        <i class="fa-regular fa-clock"></i>
                                        Saturday, June 01, 2024
                                    </p>
                                </div>
                            </div>

                            <div class="new-item">
                                <div class="img">
                                    <img
                                            src="https://denhoamy.vn/upload/news/16291329den-nghe-thuat-1.jpg"
                                            alt=""
                                    />
                                </div>
                                <div class="new-info">
                                    <div class="new-title">
                                        50+ mẫu đèn trang trí quán cafe độc đáo hot
                                    </div>
                                    <p class="new-time">
                                        <i class="fa-regular fa-clock"></i>
                                        Saturday, June 01, 2024
                                    </p>
                                </div>
                            </div>

                            <div class="new-item">
                                <div class="img">
                                    <img
                                            src="https://denhoamy.vn/upload/news/2922khuyen-mai-den-trang-tri-giang-sinh-bviet.jpg"
                                            alt=""
                                    />
                                </div>
                                <div class="new-info">
                                    <div class="new-title">
                                        Khuyến mãi đèn trang trí nghệ thuật mừng ngày quốc tế
                                        phụ nữ 8-3
                                    </div>
                                    <p class="new-time">
                                        <i class="fa-regular fa-clock"></i>
                                        Saturday, June 01, 2024
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="review-product">
                <!-- Title -->
                <h2>KHÁCH HÀNG NÓI VỀ SẢN PHẨM</h2>

                <!-- Review Submission Form -->
                <form id="review-form" method="post">
                    <!-- Hidden inputs -->
                    <input type="hidden" id="product-id" name="productId" value="${productDetail.getId()}" />
                    <input type="hidden" id="account-id" name="accountId" value="${account != null ? account.getId() : ''}" />

                    <!-- Bình luận -->
                    <div class="review-input">
                        <textarea id="comment-content" name="content" placeholder="Nhập nội dung bình luận"></textarea>
                        <div class="rating-selection">
                            <label for="rating">Chọn đánh giá:</label>
                            <select id="rating" name="rating">
                                <option value="1">1 Sao</option>
                                <option value="2">2 Sao</option>
                                <option value="3">3 Sao</option>
                                <option value="4">4 Sao</option>
                                <option value="5">5 Sao</option>
                            </select>
                        </div>
                        <button type="submit" class="submit-review-btn">Gửi bình luận</button>
                    </div>
                </form>

                <!-- Display Comments Section -->
                <div class="comments-section">
                    <div class="comments-header">
                        <p>Đánh giá của khách hàng về sản phẩm</p>
                    </div>
                    <div id="comments-list">
                        <c:forEach var="review" items="${reviews}">
                            <div class="comment">
                                <div class="comment-header">
                                    <div class="avatar">
                                        <img
                                                src="https://www.w3schools.com/w3images/avatar2.png"
                                                alt="Avatar"
                                        />
                                    </div>
                                    <div class="comment-info">
                                        <span class="username">${review.getCusName()}</span>
<%--                                        <span class="comment-date">${review.}</span>--%>
                                    </div>
                                </div>
                                <p class="comment-content">${review.getContent()}</p>
                                <div class="rating">
                                    <c:forEach var="star" begin="1" end="${review.getRating()}">
                                        <i class="fa-solid fa-star"></i>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../reuse/footer.jsp" />
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/slick-carousel/slick/slick.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/assets/Js/product.js?v=${System.currentTimeMillis()}"></script>
</body>
</html>

