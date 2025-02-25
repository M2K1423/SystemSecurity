window.addEventListener("DOMContentLoaded", function () {
    const signUpForm = document.getElementById("signup-form");
    const username = document.getElementById("username");
    const email = document.getElementById("email");
    const password = document.getElementById("password");
    const rePassword = document.getElementById("rePassword");
  
    // Lấy phần tử thông báo từ DOM
    const notification = document.getElementById("notification");
    const notificationMessage = document.getElementById("notification-message");
  
    // Hàm hiển thị thông báo
    function showNotification(message, type = "success") {
      notificationMessage.textContent = message;
  
      // Đổi màu thông báo dựa trên loại thông báo
      if (type === "error") {
        notification.style.backgroundColor = "#dc3545"; // Màu đỏ cho lỗi
      } else {
        notification.style.backgroundColor = "#28a745"; // Màu xanh cho thành công
      }
  
      // Hiển thị thông báo
      notification.classList.add("show");
  
      // Tự động ẩn thông báo sau 3 giây
      setTimeout(() => {
        notification.classList.remove("show");
      }, 3000);
    }
  
    // Xử lý khi submit form đăng ký
    signUpForm.addEventListener("submit", function (event) {
      event.preventDefault();
  
      // Kiểm tra mật khẩu có khớp không
      if (password.value !== rePassword.value) {
        showNotification("Mật khẩu không khớp!", "error");
        return;
      }
  
      // Lấy danh sách người dùng từ sessionStorage
      let users = JSON.parse(sessionStorage.getItem("users")) || [];
  
      // Kiểm tra xem tài khoản đã tồn tại chưa
      const existingUser = users.find((user) => user.username === username.value);
      if (existingUser) {
        showNotification("Tài khoản đã tồn tại!", "error");
        return;
      }
  
      // Thêm người dùng mới vào mảng
      const newUser = {
        username: username.value,
        email: email.value,
        password: password.value,
      };
      users.push(newUser);
  
      // Lưu lại mảng người dùng vào sessionStorage
      sessionStorage.setItem("users", JSON.stringify(users));
  
      // Hiển thị thông báo đăng ký thành công
      showNotification("Đăng ký thành công!");
  
      // Chuyển hướng sang trang đăng nhập
      setTimeout(() => {
        window.location.href = "login.html";
      }, 2000); // Đợi 2 giây trước khi chuyển hướng
    });
  });
