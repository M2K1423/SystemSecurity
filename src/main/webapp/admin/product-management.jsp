<%@ page import="com.example.webbongden.dao.model.SubCategory" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/admin.css?v=2.0">
</head>
<style>
  #edit-product-btn, #save-product-btn {
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    float: right;
    margin-bottom: 15px;
    border-radius: 4px;
  }

  .details-form {
    margin-top: 20px;
  }

  /* Form thêm danh mục */
  .add-category-form, .add-sub-category-form {
    margin-bottom: 30px;
    padding: 15px;
    background-color: #f9f9f9;
    border: 1px solid #ddd;
    border-radius: 8px;
  }

  .add-category-form h3, .add-sub-category-form h3 {
    margin-bottom: 10px;
    font-size: 18px;
    font-weight: bold;
    text-align: center;
  }

  .add-category-form label, .add-sub-category-form label {
    font-size: 14px;
    font-weight: bold;
    display: block;
    margin-bottom: 5px;
    color: #555;
  }

  .add-category-form input, .add-sub-category-form input,
  .add-category-form select, .add-sub-category-form select {
    width: 100%;
    padding: 10px;
    margin-bottom: 15px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
    background-color: #ffffff;
    box-sizing: border-box;
  }

  .add-category-form button, .add-sub-category-form button {
    width: 100%;
    padding: 10px 0;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .add-category-form button:hover, .add-sub-category-form button:hover {
    background-color: #0056b3;
  }

  /* Bảng hiển thị danh mục */
  #categories-table-container {
    margin-top: 30px;
  }

  #categories-table {
    width: 100%;
    border-collapse: collapse;
    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  }

  #categories-table thead th {
    color: black;
    font-size: 14px;
    font-weight: 500;
    text-align: left;
    padding: 10px;
    border: 1px solid #ddd;
  }

  #categories-table tbody td {
    padding: 10px;
    border: 1px solid #ddd;
    font-size: 12px;
    color: #333;
  }

  #categories-table tbody tr:nth-child(odd) {
    background-color: #f9f9f9;
  }

  #categories-table tbody tr:nth-child(even) {
    background-color: #ffffff;
  }

  #categories-table tbody td ul {
    list-style: none;
    padding: 0;
    margin: 0;
  }

  #categories-table tbody td ul li {
    padding: 5px 0;
    font-size: 14px;
    color: #555;
  }

  /* Nút xóa */
  button.delete-category-btn, button.delete-sub-category-btn {
    padding: 5px 10px;
    background-color: #dc3545;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 12px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  button.delete-category-btn:hover, button.delete-sub-category-btn:hover {
    background-color: #b02a37;
  }

  .close-overlay-btn {
    position: absolute;
    top: 10px;
    right: 20px;
    background-color: #ff4d4d;
    color: white;
    border: none;
    border-radius: 50%;
    width: 30px;
    height: 30px;
    font-size: 18px;
    font-weight: bold;
    cursor: pointer;
    text-align: center;
    line-height: 30px;
    transition: background-color 0.3s;
  }

  .close-overlay-btn:hover {
    background-color: #cc0000;
  }

  #image-section {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
  }

  #image-section input {
    width: 300px;
    padding: 5px;
  }

  .edit-image-button,
  .cancel-edit-button {
    padding: 5px 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 3px;
    cursor: pointer;
  }

  .cancel-edit-button {
    background-color: #dc3545;
  }

  .cancel-button {
    background-color: #ff4d4d; /* Màu đỏ nổi bật */
    color: white; /* Màu chữ */
    border: none; /* Loại bỏ viền */
    border-radius: 4px; /* Bo góc */
    padding: 10px 20px; /* Khoảng cách trong nút */
    font-size: 14px; /* Kích thước chữ */
    cursor: pointer; /* Hiển thị con trỏ khi hover */
    display: inline-flex; /* Canh giữa icon và text */
    align-items: center; /* Canh giữa icon và text */
    float: right;
    gap: 8px; /* Khoảng cách giữa icon và text */
    margin-right: 10px;
  }

  .alert {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 15px;
    border: 1px solid transparent;
    border-radius: 4px;
    font-size: 16px;
    font-family: Arial, sans-serif;
    z-index: 9999;
    animation: fadeIn 0.5s ease-in-out; /* Hiệu ứng xuất hiện */
  }

  .alert-success {
    color: #155724;
    background-color: #d4edda;
    border-color: #c3e6cb;
  }

  .alert-danger {
    color: #721c24;
    background-color: #f8d7da;
    border-color: #f5c6cb;
  }

  /* Hiệu ứng xuất hiện */
  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateX(-100%);
    }
    to {
      opacity: 1;
      transform: translateX(0);
    }
  }

  .view-sub-categorie-btn {
      background-color: dodgerblue;
      padding: 5px 10px;
      color: white;
  }
</style>
<body>
<div class="wrapper">
  <%@ include file="header.jsp" %>

  <div class="main">
    <%@ include file="sidebar.jsp" %>

    <div class="main-content">
      <div class="tab-content" id="product-management-content">
        <div class="product-stats">
          <div class="stat-card">
            <div class="card-image">
              <img src="${pageContext.request.contextPath}/assets/img/adminpage/product.png" alt="" />
            </div>
            <div class="card-content">
              <h3>Tổng sản phẩm trong kho</h3>
              <p id="total-stock">${totalProducts}</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="card-image">
              <img src="${pageContext.request.contextPath}/assets/img/adminpage/category.png" alt="" />
            </div>
            <div class="card-content">
              <h3>Số loại sản phẩm</h3>
              <p id="total-categories">${categoryQuantity}</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="card-image">
              <img src="${pageContext.request.contextPath}/assets/img/adminpage/out-of-stock.png" alt="" />

            </div>
            <div class="card-content">
              <h3>Sản phẩm hết hàng</h3>
              <p id="out-of-stock">${outOfStockProducts}</p>
            </div>
          </div>
          <div class="stat-card">
            <div class="card-image">
              <img src="${pageContext.request.contextPath}/assets/img/adminpage/new-product.png" alt="" />
            </div>
            <div class="card-content">
              <h3>Sản phẩm mới nhất</h3>
              <p id="latest-product">${newProductsInLast7Days}</p>
            </div>
          </div>
        </div>

        <div class="tab-content-container">
          <div class="header-tab">
            <div class="search-container">
              <input
                      type="text"
                      id="product-search"
                      placeholder="Tìm kiếm sản phẩm..."
              />
              <button id="search-btn-product">Tìm kiếm</button>
            </div>
            <div>

              <button class="add-category">
                  <i class="fa-solid fa-plus"></i>
                  Thêm loại sản phẩm
              </button>

              <button class="add-product">
                <i class="fa-solid fa-plus"></i>
                Thêm sản phẩm
              </button>
            </div>
          </div>

          <!-- Overlay và Form thêm sản phẩm -->
          <div class="overlay" id="overlay-add-product" data-index="0">
            <div class="add-product-form" id="add-product-form">
              <div class="form-header" style="display: flex; justify-content: space-between">
                <p>Thêm sản phẩm mới</p>
                <button class="close-btn" id="close-form">
                  <i class="fa-solid fa-xmark"></i>
                </button>
              </div>
              <form id="product-form" action="add-product" method="post" enctype="multipart/form-data">
                <div class="form-grid">
                  <!-- Cột 1 -->
                  <div class="form-column">
                    <!-- Tên sản phẩm -->
                    <div class="form-group">
                      <label for="product-name">Tên sản phẩm:</label>
                      <input
                              type="text"
                              id="product-name"
                              name="productName"
                              placeholder="Nhập tên sản phẩm"
                              required
                      />
                    </div>

                    <!-- Giá sản phẩm -->
                    <div class="form-group">
                      <label for="product-price">Giá sản phẩm:</label>
                      <input
                              type="number"
                              id="product-price"
                              name="unitPrice"
                              placeholder="Nhập giá sản phẩm"
                              required
                      />
                    </div>

                    <!-- Số lượng sản phẩm -->
                    <div class="form-group">
                      <label for="product-stock">Số lượng:</label>
                      <input
                              type="number"
                              id="product-stock"
                              name="stockQuantity"
                              placeholder="Nhập số lượng sản phẩm"
                              required
                      />
                    </div>

                    <!-- Trạng thái sản phẩm -->
                    <div class="form-group">
                      <label for="product-status">Trạng thái sản phẩm:</label>
                      <select id="product-status" name="productStatus" required>
                        <option value="">Chọn trạng thái</option>
                        <option value="Available" selected>Còn hàng</option>
                        <option value="Unavailable">Hết hàng</option>
                      </select>
                    </div>

                    <div class="form-group">
                      <label for="product-category">Danh mục sản phẩm:</label>
                      <select id="product-category" name="categoryName" required>
                        <option value="">Chọn danh mục</option>
                        <!-- Duyệt qua danh sách categories -->
                        <%
                          List<SubCategory> subCategories = (List<SubCategory>) request.getAttribute("listSubCate");
                          if (subCategories != null) {
                            for (SubCategory category : subCategories) {
                        %>
                        <option value="<%= category.getName() %>">
                          <%= category.getName() %>
                        </option>
                        <%
                            }
                          }
                        %>
                      </select>
                    </div>

                    <div class="form-group">
                      <label for="product-image-upload">Hình ảnh sản phẩm:</label>
                      <input
                              type="file"
                              id="product-image-upload"
                              name="productImages"
                              multiple
                              accept="image/*"
                              required
                      />
                    </div>
                  </div>

                  <!-- Cột 2 -->
                  <div class="form-column">

                    <!-- Mô tả sản phẩm -->
                    <div class="form-group">
                      <label for="product-desc">Mô tả sản phẩm:</label>
                      <textarea
                              id="product-desc"
                              name="description"
                              placeholder="Nhập mô tả sản phẩm"
                      ></textarea>
                    </div>

                    <!-- Thời gian bảo hành -->
                    <div class="form-group">
                      <label for="product-warranty">Thời gian bảo hành:</label>
                      <input
                              type="text"
                              id="product-warranty"
                              name="warrantyPeriod"
                              placeholder="Nhập thời gian bảo hành"
                      />
                    </div>

                    <!-- Màu sắc ánh sáng -->
                    <div class="form-group">
                      <label for="product-light-color">Màu ánh sáng:</label>
                      <input
                              type="text"
                              id="product-light-color"
                              name="lightColor"
                              placeholder="Nhập màu ánh sáng"
                      />
                    </div>

                    <!-- Chất liệu sản phẩm -->
                    <div class="form-group">
                      <label for="product-material">Chất liệu:</label>
                      <input
                              type="text"
                              id="product-material"
                              name="material"
                              placeholder="Nhập chất liệu sản phẩm"
                      />
                    </div>

                    <!-- Điện áp sản phẩm -->
                    <div class="form-group">
                      <label for="product-voltage">Điện áp:</label>
                      <input
                              type="text"
                              id="product-voltage"
                              name="voltage"
                              placeholder="Nhập điện áp sản phẩm"
                      />
                    </div>

                    <!-- Tuổi thọ sử dụng -->
                    <div class="form-group">
                      <label for="product-usage-age">Tuổi thọ sử dụng:</label>
                      <input
                              type="text"
                              id="product-usage-age"
                              name="usageAge"
                              placeholder="Nhập tuổi thọ sản phẩm"
                      />
                    </div>

                    <!-- Giảm giá -->
                    <div class="form-group">
                      <label for="product-discount-add">Phần trăm giảm giá:</label>
                      <input
                              type="number"
                              id="product-discount-add"
                              name="discountPercent"
                              placeholder="Nhập phần trăm giảm giá"
                              step="0.1"
                              min="0"
                      />
                    </div>
                  </div>
                </div>

                <!-- Nút lưu -->
                <div class="form-group">
                  <button type="submit" id="save-product" class="save-btn">
                    Lưu sản phẩm
                  </button>
                </div>
              </form>
            </div>
          </div>

          <div class="overlay" id="overlay-add-category" data-index="1">
            <div class="categories-container">
              <!-- Nút Close -->
              <button class="close-overlay-btn" id="close-overlay-add-category">X</button>
              <h1>Quản lý Danh mục</h1>

              <!-- Form Thêm Danh mục Cha -->
              <div style="display: flex; justify-content: space-between">
                <div class="add-category-form">
                  <h3>Thêm Danh mục Cha</h3>
                  <form id="category-form">
                    <label for="category-name">Tên danh mục:</label>
                    <input type="text" id="category-name" placeholder="Tên danh mục" required />
                    <button type="submit" id="add-category-btn" class="add-category-button">Thêm Danh mục</button>
                  </form>
                </div>

                <!-- Form Thêm Danh mục Con -->
                <div class="add-sub-category-form">
                  <h3>Thêm Danh mục Con</h3>
                  <form id="sub-category-form">
                    <label for="parent-category">Danh mục Cha:</label>
                    <select id="parent-category">
                      <option value="" disabled selected>Chọn danh mục cha</option>
                      <c:forEach var="category" items="${categories}">
                        <option value="${category.id}">${category.categoryName}</option>
                      </c:forEach>
                    </select>
                    <label for="sub-category-name">Tên danh mục con:</label>
                    <input type="text" id="sub-category-name" placeholder="Tên danh mục con" required />
                    <button type="submit" id="add-sub-category-btn" class="add-category-button">Thêm Danh mục Con</button>
                  </form>
                </div>
              </div>

              <!-- Bảng hiển thị Danh mục -->
              <div id="categories-table-container">
                <h3>Danh sách Danh mục</h3>
                <table id="categories-table" class="display" style="width: 100%;">
                  <thead>
                  <tr>
                    <th>ID</th>
                    <th>Tên Danh mục</th>
                    <th>DS Danh mục con</th>
                    <th>Thao tác</th>
                  </tr>
                  </thead>
                  <tbody id="categories-table-body">
                  <!-- Danh mục cha sẽ được tải động -->
                  </tbody>
                </table>
              </div>
            </div>
          </div>


          <!-- Bảng danh sách sản phẩm -->
          <div class="tab-container">
            <table class="product-table" id="product-table">
              <thead>
              <tr>
                <th>Id</th>
                <th>Hình ảnh</th>
                <th>Tên sản phẩm</th>
                <th>Giá</th>
                <th>Loại sản phẩm</th>
                <th>Ngày thêm</th>
                <th>Thao tác</th>
                <th></th>
              </tr>
              </thead>
              <tbody id="product-table-body">

              </tbody>
            </table>
          </div>
        </div>

        <!-- Khung chi tiết sản phẩm -->

        <div class="product-details" id="product-details" style="display: none;">
          <button id="close-details-btn">
            <i class="fa-solid fa-arrow-left"></i> Quay lại
          </button>
          <!-- Phần tiêu đề và thông tin cơ bản -->
          <div class="details-header">
            <div class="product-info">
              <div class="product-info-left">
                <div class="product-image">
                  <img id="product-image-main" src="" alt="Hình ảnh sản phẩm" />
                </div>
                <div class="basic-details">
                  <p>
                    <strong>Tên sản phẩm:</strong>
                    <span id="product-name-details"></span>
                  </p>
                  <p>
                    <strong>Mã sản phẩm:</strong>
                    <span id="product-id-details"></span>
                  </p>
                </div>
              </div>
            </div>
          </div>

          <div class="details-main">

            <!-- Phần chi tiết sản phẩm -->
            <div class="details-content">
              <button id="edit-product-btn" class="edit-product">
                <i class="fa-solid fa-pen"></i> Chỉnh sửa
              </button>
              <form class="details-form" action="edit-product-detail" method="post" enctype="multipart/form-data">
                <button id="save-product-btn" class="save-button" type="submit" style="display: none;">
                  <i class="fa-solid fa-pen"></i> Lưu
                </button>

                <button id="cancel-product-btn" class="cancel-button" type="button" style="display: none;">
                  <i class="fa-solid fa-cancel"></i> Hủy
                </button>
                <div>
                  <strong>Id:</strong>
                  <span id="product-id-view"></span>
                  <input type="text" id="edit-product-id" value="" style="display: none" name="id" readonly />
                </div>

                <div>
                  <strong>Hình ảnh:</strong>
                  <div id="image-section">
                    <!-- Hiển thị URL hình ảnh hiện tại -->
<%--                    <input--%>
<%--                            type="text"--%>
<%--                            id="product-image-url"--%>
<%--                            name="mainImageUrl"--%>
<%--                            value=""--%>
<%--                            readonly--%>
<%--                    />--%>
                    <input
                            type="file"
                            id="upload-product-image"
                           name="imageFiles"
                            accept="image/*"
                            multiple/>

<%--                    <button id="edit-image-btn" class="edit-image-button" type="button">--%>
<%--                      Chỉnh sửa--%>
<%--                    </button>--%>
                  </div>

<%--                  <!-- Input upload file ẩn mặc định -->--%>
<%--                  <div id="upload-file-section" style="display: none;">--%>
<%--                    <input--%>
<%--                            type="file"--%>
<%--                            id="upload-product-image"--%>
<%--                            name="imageFiles"--%>
<%--                            accept="image/*"--%>
<%--                            multiple--%>
<%--                    />--%>
<%--                    <button id="cancel-edit-btn" class="cancel-edit-button" type="button">--%>
<%--                      Hủy--%>
<%--                    </button>--%>
<%--                  </div>--%>
                </div>

                <div>
                  <strong>Tên sản phẩm:</strong>
                  <span id="product-name-view"></span>
                  <input type="text" id="edit-product-name" value="" name="productName" style="display: none" />
                </div>

                <div>
                  <strong>Giá:</strong>
                  <span id="product-price-view"></span>
                  <input type="text" id="edit-product-price" value="" name = "unitPrice" style="display: none" />
                </div>

                <div>
                  <strong>Loại sản phẩm:</strong>
                  <span id="product-category-view">$</span>
                  <select id="edit-product-category" name="subCategoryId" style="display: none" required>
                    <option value="">Chọn danh mục</option>
                    <!-- Duyệt qua danh sách categories -->
                    <%
                      List<SubCategory> subCategoriess = (List<SubCategory>) request.getAttribute("listSubCate");
                      if (subCategoriess != null) {
                        for (SubCategory category : subCategoriess) {
                    %>
                    <option value="<%= category.getId() %>">
                      <%= category.getName() %>
                    </option>
                    <%
                        }
                      }
                    %>
                  </select>
                </div>

                <div>
                  <strong>Tình trạng:</strong>
                  <span id="product-status-view"></span>
                  <input type="text" id="edit-product-status" value="" name="productStatus" style="display: none" />
                </div>

                <div>
                  <strong>Mô tả:</strong>
                  <span id="product-description-view"></span>
                  <textarea id="edit-product-description" name="description" style="display: none"></textarea>
                </div>

                <div>
                  <strong>Ngày thêm:</strong>
                  <span id="product-date-view"></span>
                  <input type="date" id="edit-product-date" name="createdAt" value="" style="display: none" />
                </div>

                <div>
                  <strong>Giảm giá:</strong>
                  <span id="product-discount-view"></span>
                  <input type="text" id="edit-product-discount" name="discountPercent" value="%" style="display: none" />
                </div>

                <div>
                  <strong>Số lượng tồn kho:</strong>
                  <span id="product-stock-view"></span>
                  <input type="text" id="edit-product-stock" name="stockQuantity" value="" style="display: none" />
                </div>

                <div>
                  <strong>Đánh giá:</strong>
                  <span id="product-rating-view"></span>
                  <input type="text" id="edit-product-rating" name="rating" value="" style="display: none" />
                </div>

                <div>
                  <strong>Thời gian bảo hành:</strong>
                  <span id="product-warranty-view"></span>
                  <input type="text" id="edit-product-warranty" name="warrantyPeriod" value="" style="display: none" />
                </div>

                <div>
                  <strong>Chất liệu:</strong>
                  <span id="product-material-view"></span>
                  <input type="text" id="edit-product-material" name="material" value="" style="display: none" />
                </div>

                <div>
                  <strong>Màu sắc:</strong>
                  <span id="product-color-view"></span>
                  <input type="text" id="edit-product-color" name="lightColor" value="" style="display: none" />
                </div>

                <div>
                  <strong>Tuổi thọ:</strong>
                  <span id="product-lifespan-view"></span>
                  <input type="text" id="edit-product-lifespan" name="usageAge" value="" style="display: none" />
                </div>

                <div>
                  <strong>Công suất:</strong>
                  <span id="product-power-view"></span>
                  <input type="text" id="edit-product-power" name="voltage" value="" style="display: none" />
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<c:if test="${not empty message}">
  <div class="alert alert-success">
      ${message}
  </div>
</c:if>

<c:if test="${not empty error}">
  <div class="alert alert-danger">
      ${error}
  </div>
</c:if>
<script src="${pageContext.request.contextPath}/admin/admin_js/productAdmin.js?v=${System.currentTimeMillis()}" defer></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script>
  // Tự động ẩn thông báo sau 3 giây
  setTimeout(() => {
    const alertBox = document.querySelector('.alert');
    if (alertBox) {
      alertBox.style.transition = 'opacity 0.5s ease-out'; // Hiệu ứng mờ dần
      alertBox.style.opacity = '0';
      setTimeout(() => alertBox.remove(), 500); // Xóa phần tử sau khi mờ dần
    }
  }, 3000);
</script>
</body>
</html>
