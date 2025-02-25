<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 12/14/2024
  Time: 7:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="./img/logo-fold.png" sizes="180x180">
    <title>Đăng nhập</title>
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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/reset.css">
</head>
<body>
<div class="wrapper">
    <!-- Hiển thị thông báo lỗi nếu có -->
    <c:if test="${not empty errorMessageReset}">
        <p id="responseMessage1" style="color: red;">
                ${errorMessageReset}
        </p>
    </c:if>

    <!-- Hiển thị thông báo thành công nếu có -->
    <c:if test="${not empty successMessageReset}">
        <p id="responseMessage2" style="color: green;">
                ${successMessageReset}
        </p>
    </c:if>
    <form id="login-form" action="login" method="post">
        <div class="form-header">
            <a href="index.html" class="logo"
            ><img src="./img/logo-login.png" alt=""
            /></a>
            <p>ĐĂNG NHẬP</p>
        </div>
        <div class="form-main">
            <div class="form-group">
                <p>TÀI KHOẢN</p>
                <div class="form-field">
                    <input
                            type="text"
                            name="username"
                            class="form-input"
                            id="cus-username"
                            placeholder=" "
                    />
                    <label for="cus-username" class="form-label">Tài khoản</label>
                </div>
            </div>

            <div class="form-group">
                <p>MẬT KHẨU</p>
                <div class="form-field">
                    <input
                            type="password"
                            name="password"
                            class="form-input"
                            id="cus-password"
                            placeholder=" "
                    />
                    <label for="cus-password" class="form-label">Mật khẩu</label>
                </div>
            </div>
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) {
            %>
            <div class="error-message" style="color: red; text-align: center; margin: 10px 0;">
                <%= errorMessage %>
            </div>
            <%
                }
            %>
            <div class="form-footer">
                <label>
                    <div class="option_field">
                        <a href="#" class="forgot-pw"
                        >Quên mật khẩu?</a
                        >
                    </div>
                </label>
                <button type="submit" class="btn">Đăng nhập</button>
            </div>
            <div class="login_signup">
                Bạn chưa có tài khoản?
                <a href="${pageContext.request.contextPath}/register" id="signup">Đăng ký ngay!</a>
            </div>
        </div>
    </form>
    <!-- form quên mật khẩu -->
    <div
            id="forgot-password-form"
            class="forgot-container"
            style="display: none"
    >
        <p>KHÔI PHỤC MẬT KHẨU</p>
        <form action="reset-password" id="reset-password-form" method="post">
            <div class="input-group">
                <label for="reset-email">Nhập email của bạn</label>
                <input
                        type="email"
                        id="reset-email"
                        name="reset-email"
                        required
                        placeholder="example@gmail.com"
                />
            </div>
            <div class="group-btn">
                <button type="submit">
                    Gửi Yêu Cầu Đặt Lại Mật Khẩu
                </button>
                <p>
                    Bạn đã nhớ mật khẩu?
                    <span
                            id="back-to-login"

                            style="color: #4288e5"
                    >
                Quay lại trang đăng nhập
              </span>
                </p>
            </div>
        </form>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/assets/Js/login.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const forgotPasswordLink = document.querySelector(".forgot-pw");
        const forgotPasswordForm = document.getElementById("forgot-password-form");
        const loginForm = document.getElementById("login-form");
        const backToLogin = document.getElementById("back-to-login");

        // Hiển thị form quên mật khẩu và ẩn form đăng nhập
        forgotPasswordLink.addEventListener("click", function (e) {
            e.preventDefault(); // Ngăn chặn hành vi mặc định (chuyển trang)
            loginForm.style.display = "none";
            forgotPasswordForm.style.display = "block";
        });

        // Quay lại form đăng nhập
        backToLogin.addEventListener("click", function () {
            forgotPasswordForm.style.display = "none";
            loginForm.style.display = "block";
        });
    });
</script>
</html>
