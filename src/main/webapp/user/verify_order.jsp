<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html><head><title>Kết quả xác minh</title></head><body>
<% Boolean valid = (Boolean) request.getAttribute("signatureValid"); %>
<% if (valid != null && valid) { %>
<h2 style="color:green">✔️ Chữ ký HỢP LỆ!</h2>
<% } else { %>
<h2 style="color:red">❌ Chữ ký KHÔNG hợp lệ!</h2>
<% } %>
<a href="/SystemSecurity_war/home">Quay lại trang chủ</a>
</body></html>
