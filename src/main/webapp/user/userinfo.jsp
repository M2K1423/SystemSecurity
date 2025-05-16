<%@ page import="com.example.webbongden.dao.model.Order" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/15/2024
  Time: 10:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180" />
    <title>Thông tin khách hàng</title>
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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/header-footer.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/user.css">
</head>
<style>
    .key-btn button,
    .info-btn button {
        width: 120px;
        padding: 5px 0;
        background-color: red;
        color: white;
        border-radius: 4px;
    }
</style>
<body>
<div class="wrapper">
    <jsp:include page="../reuse/header.jsp" />
    <div class="main">
        <div class="container container-cus">
            <div class="sidebar-info-customer">
                <div class="customer-info">
                    <img
                            src="./img/b79144e03dc4996ce319ff59118caf65.jpg"
                            alt="avatarUser"
                    />
                    <p class="customer-name">${userInfo.customerName}</p>
                </div>
                <div class="menu-user">
                    <nav>
                        <ul class="menu-user">
                            <li
                                    data-section="information_account"
                                    class="active"
                                    onclick="showContent('information_account')"
                            >
                                <i class="fa-solid fa-user"></i>
                                <a href="#">Thông tin tài khoản</a>
                            </li>
                            <li data-section="public_key" onclick="showContent('public_key')">
                                <i class="fa-solid fa-key" ></i>
                                <a href="#">Khoá công khai</a>
                            </li>
                            <li data-section="order" onclick="showContent('order')">
                                <i class="fa-solid fa-bars-progress"></i>
                                <a href="#">Quản lý đơn hàng</a>
                            </li>
                            <li
                                    data-section="change_password"
                                    onclick="showContent('change_password')"
                            >
                                <i class="fa-solid fa-lock"></i>
                                <a href="#">Đổi mật khẩu</a>
                            </li>
                            <li onclick="logout()">
                                <i class="fa-solid fa-right-from-bracket"></i>
                                <a href="/SystemSecurity_war/LogoutController" id="logoutLink">Đăng xuất</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
            <div class="content">
                <!-- Thông tin tài khoản -->
                <div
                        id="information_account"
                        class="content_section"
                >
                    <div class="information_header" id="userInfo" data-customer-id="${userInfo.customerId}">
                        <h2>THÔNG TIN TÀI KHOẢN</h2>
                    </div>
                    <form class="info-form">
                        <div class="user-name dlex">
                            <label for="username">Họ tên:</label>
                            <div>
                                <input type="text" id="username" name="username" required readonly value="${userInfo.customerName}"/>
                            </div>
                        </div>

                        <div class="email-cus dlex">
                            <label for="email">Email:</label>
                            <div>
                                <input type="text" id="email" name="email" value="${userInfo.email}" readonly/>
                            </div>
                        </div>

                        <div class="phone-cus dlex">
                            <label for="phone">Số điện thoại:</label>
                            <div>
                                <input type="tel" id="phone" name="phone" value="${userInfo.phone}" readonly/>
                            </div>
                        </div>

                        <div class="phone-cus dlex">
                            <label for="address">Địa chỉ:</label>
                            <div>
                                <input type="text" id="address" name="address" value="${userInfo.address}" readonly/>
                            </div>
                        </div>

                        <div class="create-date-cus dlex">
                            <label for="create-date">Ngày tạo:</label>
                            <div>
                                <input type="text" id="create-date" name="create-date" value="${userInfo.createdAt}" readonly/>
                            </div>
                        </div>


                        <div class="info-btn">

                                <button type="submit" id="save-info" style="display: none;">Lưu</button>



                                <button type="button" id="edit-info">Sửa thông tin</button>

                        </div>
                    </form>
                    <p id="saveMessage" style="display: none; color: green">
                        Hồ sơ của bạn đã được lưu!
                    </p>
                </div>
                <!-- Quản lý khoá -->
                <div id="public_key" class="content_section" style="display: none;">
                    <div class="key_header">
                        <h1>QUẢN LÝ KHOÁ CÔNG KHAI</h1>
                    </div>

                    <form class="key-form" action="manageKey" method="POST">
                        <div class="publicKey-cus dlex">
                            <label for="publicKey">Khoá công khai</label>
                            <div>
                                <textarea id="publicKey" name="publicKey" required readonly>${publicKey}</textarea>
                            </div>
                        </div>
                        <div class="auth-password-cus dlex">
                            <label for="auth-password">Xác nhận mật khẩu</label>
                            <div>
                                <input type="password" id="auth-password" name="password" required readonly/>
                            </div>
                        </div>

                        <div class="key-btn">
                            <button type="submit" id ="save-publicKey" style="display : none;">Xác nhận</button>
                            <button type="button" id ="edit-publicKey">Cập nhật khoá</button>
                        </div>
                    </form>
                </div>
                <!-- Quản lý đơn hàng -->
                <div id="order" class="content_section" style="display: none;">
                    <div class="order_header">
                        <h2>ĐƠN HÀNG ĐÃ ĐẶT</h2>
                    </div>

                    <div class="order-table-container" id="orderTableContainer">
                        <%
                            List<Order> orders = (List<Order>) session.getAttribute("orders");
                            if (orders != null && !orders.isEmpty()) {
                        %>
                        <table class="order-table">
                            <thead>
                            <tr>
                                <th>Id</th>
                                <th>Ngày đặt</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Tải hóa đơn</th> <!-- Cột mới -->
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                for (Order order : orders) {
                            %>
                            <tr>
                                <td><%= order.getId() %></td>
                                <td><%= order.getCreatedAt() %></td>
                                <td><%= order.getTotalPrice() %></td>
                                <td><%= order.getOrderStatus() %></td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/download-invoice?id=<%= order.getId() %>"
                                       class="btn btn-sm btn-outline-primary" target="_blank">
                                        Tải
                                    </a>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                        <%
                        } else {
                        %>
                        <p>Không có đơn hàng nào.</p>
                        <%
                            }
                        %>
                    </div>

                </div>

                <!-- đổi mật khẩu -->
                <div
                        id="change_password"
                        class="content_section"
                        style="display: none"
                >
                    <div class="change_password_header">
                        <h1>ĐỔI MẬT KHẨU</h1>
                        <div class="title">
                            Để bảo mật tài khoản, vui lòng không chia sẻ mật khẩu cho
                            người khác
                        </div>
                    </div>
                    <form class="change_password_form" action="changePassword" method="POST">
                        <div class="dlex">
                            <label for="oldPassword">Mật khẩu cũ:</label>
                            <div>
                                <input type="password" id="oldPassword" name="oldPassword" placeholder="Nhập mật khẩu hiện tại" required />
                            </div>
                        </div>

                        <div class="dlex">
                            <label for="newPassword">Mật khẩu mới:</label>
                            <div>
                                <input type="password" id="newPassword" name="newPassword" placeholder="Nhập mật khẩu mới" required />
                            </div>
                        </div>

                        <div class="dlex">
                            <label for="confirm_password">Nhập lại mật khẩu:</label>
                            <div>
                                <input type="password" id="confirm_password" name="confirmPassword" placeholder="Nhập lại mật khẩu mới" required />
                            </div>
                        </div>
                        <button class="submit" type="submit">Xác nhận</button>
                    </form>
                    <c:if test="${not empty successMessage}">
                        <p style="color: green;">${successMessage}</p>
                    </c:if>
                    <c:if test="${not empty errorMessage}">
                        <p style="color: red;">${errorMessage}</p>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../reuse/footer.jsp" />
</div>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/assets/Js/user.js?v=2.0" defer></script>
<script>
    document.getElementById('edit-info').addEventListener('click', function () {
        // Lấy tất cả các input cần chỉnh sửa
        const inputs = document.querySelectorAll('.info-form input:not([id="email"]):not([id="create-date"])');

        // Bật chế độ chỉnh sửa
        inputs.forEach(input => {
            input.readOnly = false; // Tắt chế độ readonly
            input.classList.add('editable'); // Thêm lớp để có thể thay đổi kiểu dáng (nếu cần)
        });

        // Hiển thị nút lưu và ẩn nút sửa
        document.getElementById('edit-info').style.display = 'none';
        document.getElementById('save-info').style.display = 'inline-block';
    });

    // Khi lưu thông tin
    document.getElementById('save-info').addEventListener('click', function (e) {
        e.preventDefault();

        // Lấy ID khách hàng từ thuộc tính data
        const customerId = document.getElementById('userInfo').getAttribute('data-customer-id');

        // Thu thập dữ liệu từ các input
        const formData = {
            customerId: customerId,
            cusName: document.getElementById('username').value,
            address: document.getElementById('address').value,
            phone: document.getElementById('phone').value,
        };

        console.log(formData); // Kiểm tra dữ liệu trước khi gửi

        // Gửi AJAX để cập nhật thông tin
        $.ajax({
            url: '/SystemSecurity_war/edit-cus-info',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                if (response.success) {
                    Swal.fire('Thành công!', 'Thông tin của bạn đã được cập nhật.', 'success');

                    // Đặt lại trạng thái readonly sau khi lưu
                    const inputs = document.querySelectorAll('.info-form input:not([id="email"]):not([id="create-date"])');
                    inputs.forEach(input => {
                        input.readOnly = true; // Bật lại chế độ readonly
                        input.classList.remove('editable'); // Xóa lớp editable
                    });

                    // Ẩn nút lưu và hiển thị nút sửa
                    document.getElementById('save-info').style.display = 'none';
                    document.getElementById('edit-info').style.display = 'inline-block';
                } else {
                    Swal.fire('Thất bại!', 'Không thể cập nhật thông tin. Vui lòng thử lại.', 'error');
                }
            },
            error: function () {
                Swal.fire('Lỗi!', 'Đã xảy ra lỗi khi cập nhật thông tin.', 'error');
            }
        });
    });

    // chỉnh sửa khoá
    document.getElementById('edit-publicKey').addEventListener('click', function () {
        const inputs = document.querySelectorAll('.key-form input, .key-form textarea');

        inputs.forEach(input => {
            input.readOnly = false;
            input.classList.add('editable');
        });

        document.getElementById('edit-publicKey').style.display = 'none';
        document.getElementById('save-publicKey').style.display = 'inline-block';
    });

    // lưu khoá công khai
    document.getElementById('save-publicKey').addEventListener('click', function (e) {
        e.preventDefault();

        // Lấy ID khách hàng từ thuộc tính data
        const customerId = document.getElementById('userInfo').getAttribute('data-customer-id');

        const formData = {
            customerId: customerId,
            publicKey: document.getElementById('publicKey').value,
            password: document.getElementById('auth-password').value,
        };

        $.ajax({
            url: '/SystemSecurity_war/edit-publicKey',
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: formData,
            success: function (response) {
                if(response.success){
                    Swal.fire('Thành công!', response.success, 'success');

                    const inputs = document.querySelectorAll('.key-form input, .key-form textarea');
                    inputs.forEach(input => {
                        input.readOnly = true; // Bật lại chế độ readonly
                        input.classList.remove('editable'); // Xóa lớp editable
                    });

                    // Ẩn nút lưu và hiển thị nút sửa
                    document.getElementById('save-publicKey').style.display = 'none';
                    document.getElementById('edit-publicKey').style.display = 'inline-block';
                } else {
                    Swal.fire('Lỗi!', 'Đã xảy ra lỗi khi cập nhật thông tin.', 'error');
                    }
            },
            error: function () {
                Swal.fire('Lỗi!', 'Đã xảy ra lỗi 1 khi cập nhật thông tin.', 'error');
            }
        });
    });

</script>
</html>