<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Kết quả xác minh chữ ký</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 30px;
            background-color: #f4f6f8;
        }

        .card {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            padding: 25px;
        }

        h2 {
            color: #333;
            margin-bottom: 15px;
        }

        .status.success {
            color: green;
            font-weight: bold;
        }

        .status.fail {
            color: red;
            font-weight: bold;
        }

        .info {
            margin: 15px 0;
            padding: 10px;
            background-color: #eef2f5;
            border-radius: 5px;
            font-family: monospace;
        }

        .btn-back {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            border-radius: 5px;
        }

        .btn-back:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="card">
    <h2>Kết quả xác minh chữ ký đơn hàng</h2>

    <c:if test="${not empty error}">
        <p class="status fail">❌ ${error}</p>
    </c:if>

    <c:if test="${not empty order}">
        <div class="info">
            <p><strong>ID đơn hàng:</strong> ${order.id}</p>
            <p><strong>Tên khách hàng:</strong> ${order.customerName}</p>
            <p><strong>Ngày đặt hàng:</strong> ${order.createdAt}</p>
            <p><strong>Tổng tiền:</strong> ${order.totalPrice}</p>
        </div>

        <c:choose>
            <c:when test="${valid == true }">
                <p class="status success">✅ Chữ ký HỢP LỆ - Dữ liệu đơn hàng KHÔNG bị thay đổi.</p>
            </c:when>
            <c:when test="${valid == false && signatureValid == false}">
                <p class="status fail">❌ Chữ ký KHÔNG HỢP LỆ - Dữ liệu đã bị thay đổi hoặc chữ ký không đúng.</p>
            </c:when>
        </c:choose>
    </c:if>

    <a href="${pageContext.request.contextPath}/user/userinfo.jsp" class="btn-back">← Quay lại danh sách đơn hàng</a>

</div>
</body>
</html>
