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
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/admin.css?v=2.0">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <script src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
  <style>
    #edit-account-form-container {
      width: 100%;
      max-width: 500px;
      margin: 0 auto;
      background-color: #f9f9f9; /* Màu nền */
      padding: 20px;            /* Khoảng cách bên trong */
      border-radius: 5px;       /* Bo góc */
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Đổ bóng */
      position: fixed;          /* Hiển thị như modal */
      top: 50%;                 /* Canh giữa màn hình */
      left: 50%;
      transform: translate(-50%, -50%);
      z-index: 1000;            /* Nổi trên các phần khác */
    }

    #edit-account-form-container .close-button {
      position: absolute;
      top: 10px;
      right: 10px;
      background-color: transparent;
      border: none;
      font-size: 20px;
      font-weight: bold;
      color: #333;
      cursor: pointer;
    }

    #edit-account-form-container .close-button:hover {
      color: #ff0000; /* Màu đỏ khi hover */
    }

    #edit-account-form {
      border-radius: 8px;
    }

    #edit-account-form .form-group {
      margin-bottom: 15px;
    }

    #edit-account-form .form-group label {
      display: block;
      font-size: 14px;
      font-weight: 500;
      color: #333;
      margin-bottom: 5px;
    }

    #edit-account-form .form-group input,
    #edit-account-form .form-group select {
      width: 100%;
      padding: 10px;
      font-size: 14px;
      border: 1px solid #ccc;
      border-radius: 4px;
      outline: none;
      box-sizing: border-box;
      transition: border-color 0.2s ease-in-out;
    }

    #edit-account-form .form-group input:focus,
    #edit-account-form .form-group select:focus {
      border-color: #007bff;
      outline: none;
    }

    #edit-account-form .form-group input[type="date"] {
      padding: 8px 10px;
    }

    #edit-account-form .action-buttons {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
    }

    #edit-account-form .action-buttons button {
      padding: 10px 15px;
      font-size: 14px;
      font-weight: 600;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: background-color 0.2s ease-in-out;
    }

    #edit-account-form .action-buttons .save-button {
      background-color: #28a745;
      color: #fff;
    }

    #edit-account-form .action-buttons .save-button:hover {
      background-color: #218838;
    }

    #edit-account-form .action-buttons .cancel-button {
      background-color: #dc3545;
      color: #fff;
    }

    #edit-account-form .action-buttons .cancel-button:hover {
      background-color: #c82333;
    }
  </style>
</head>
<body>
<div class="wrapper">
  <%@ include file="header.jsp" %>

  <div class="main">
    <%@ include file="sidebar.jsp" %>

    <div class="main-content">
      <div class="tab-content" id="account-content">
        <div class="account-management">
          <!-- Form thêm tài khoản mới -->
          <div class="account-form">
            <h3>THÊM TÀI KHOẢN MỚI</h3>
            <form id="add-account-form" action="add-account" method="post">
              <div class="form-group">
                <label for="cusName">Tên khách hàng:</label>
                <input type="text" id="cusName" name="cusName" placeholder="Nhập tên khách hàng" required />
              </div>
              <!-- Tên đăng nhập -->
              <div class="form-group">
                <label for="username">Tên đăng nhập:</label>
                <input
                        type="text"
                        id="username"
                        name="username"
                        placeholder="Nhập tên đăng nhập"
                        required
                />
              </div>

              <!-- Email -->
              <div class="form-group">
                <label for="email">Email:</label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="Nhập email"
                        required
                />
              </div>

              <!-- Mật khẩu -->
              <div class="form-group">
                <label for="password">Mật khẩu:</label>
                <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Nhập mật khẩu"
                        required
                />
              </div>


              <!-- Vai trò -->
              <div class="form-group">
                <label for="role">Phân quyền:</label>
                <select id="role" name="role" required>
                  <option value="" disabled selected>Chọn vai trò</option>
                  <option value="admin">Quản trị viên</option>
                  <option value="customer">Khách hàng</option>
                </select>
              </div>

              <!-- Nút thêm tài khoản -->
              <button
                      type="submit"
                      id="add-account-btn"
                      class="primary-button"
              >
                Thêm tài khoản
              </button>
            </form>
          </div>
        </div>

        <div class="header-tab">
          <div class="search-container">
            <input
                    type="text"
                    id="account-search"
                    placeholder="Tìm kiếm tài khoản..."
            />
            <button id="search-btn-account">Tìm kiếm</button>
          </div>
        </div>

        <!-- Danh sách tài khoản -->
        <div id="account-list">
          <h3>DANH SÁCH TÀI KHOẢN</h3>
          <table id="account-table">
            <thead>
            <tr>
              <th>ID</th>
              <th>Tên đăng nhập</th>
              <th>Email</th>
              <th>Ngày tạo</th>
              <th>Phân quyền</th>
              <th>Hành động</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
          </table>
        </div>
      </div>
      <div class="overlay">
        <div id="edit-account-form-container">
          <button class="close-button">×</button>
          <form id="edit-account-form">
            <div class="form-group">
              <label for="edit-id">Tên khách hàng:</label>
              <input type="text" id="edit-id" name="idAc" readonly />
            </div>
            <div class="form-group">
              <label for="edit-cusName">Tên khách hàng:</label>
              <input type="text" id="edit-cusName" name="cusName" />
            </div>
            <div class="form-group">
              <label for="edit-username">Tên đăng nhập:</label>
              <input type="text" id="edit-username" name="username" />
            </div>
            <div class="form-group">
              <label for="edit-email">Email:</label>
              <input type="email" id="edit-email" name="email" />
            </div>
            <div class="form-group">
              <label for="edit-password">Mật khẩu:</label>
              <input type="password" id="edit-password" name="password" />
            </div>
            <div class="form-group">
              <label for="edit-role">Phân quyền:</label>
              <select id="edit-role" name="role">
                <option value="admin">Quản trị viên</option>
                <option value="customer">Khách hàng</option>
              </select>
            </div>
            <div class="form-group">
              <label for="edit-created-at">Ngày tạo:</label>
              <input type="date" id="edit-created-at" name="created_at" readonly />
            </div>
            <div class="action-buttons">
              <button type="submit" class="save-button">Lưu</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/admin/admin_js/acAdmin.js?v=2.0" defer></script>
</body>
</html>
