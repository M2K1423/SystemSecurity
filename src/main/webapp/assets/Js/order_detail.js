
$(document).on("click", ".change-order-btn", function () {
    const orderId = $(this).data("id");

    // Nếu cần lấy thêm dữ liệu từ dòng (ví dụ tên khách, địa chỉ)
    const rowData = {
        createAt: $(this).closest("tr").find("td:nth-child(2)").text(),
    };

    // Gọi hàm hiển thị chi tiết đơn hàng
    viewOrderDetails(orderId, rowData);
});

function viewOrderDetails(orderId, rowData) {
    // Lấy thông tin ngày tạo đơn hàng
    $("#create_at").text(rowData.createAt || "N/A");
    // Gửi AJAX để lấy chi tiết đơn hàng
    $.ajax({
        url: "/SystemSecurity_war/order-info",
        type: "GET",
        data: {orderId: orderId},
        success: function (data) {
            // Nếu server trả về JSON kiểu map thì jQuery sẽ parse được luôn
            const orderDetails = data.orderDetails;
            const order = data.order;

            $("#order_id").text(order.id || "N/A")
            $("#form_of_delivery").text(order.shippingMethod || "N/A")
            $("#delivery_fee").text(order.shippingFee ?? "N/A");
            $("#note").text(order.note ?? "N/A")

            // Gán thông tin khách hàng
            $("#customer-name").text(order.customerName || "N/A");
            $("#customer-address").text(order.address || "N/A");
            $("#customer-phone").text(order.phone || "N/A");

            if (order.orderStatus && order.orderStatus.toLowerCase() === "pending") {
                const email = $("#email").val();
                const editUrl = `/SystemSecurity_war/edit-order?orderId=${order.id}&email=${email}`;

                $("#edit-order-btn")
                    .off("click") // tránh gán nhiều lần
                    .on("click", function () {
                        window.location.href = editUrl;
                    })
                    .show();
            } else {
                $("#edit-order-btn").hide();
            }

            // Gán chi tiết đơn hàng (giữ nguyên phần render bảng của bạn)
            const $orderItemsBody = $("#order-items-body");
            $orderItemsBody.empty();

            orderDetails.forEach((item) => {
                $orderItemsBody.append(`
                <tr>
                    <td>${item.productId}</td>
                    <td>${item.productName}</td>
                    <td>${item.quantity}</td>
                    <td>${parseFloat(item.unitPrice).toLocaleString("vi-VN", {style: "currency", currency: "VND"})}</td>
                    <td>${parseFloat(item.amount).toLocaleString("vi-VN", {style: "currency", currency: "VND"})}</td>
                </tr>
            `);
            });

            const totalAmount = orderDetails.reduce((total, item) => total + parseFloat(item.amount), 0);
            $("#total-amount").text(totalAmount.toLocaleString("vi-VN", {
                style: "currency",
                currency: "VND",
            }));

            // Hiển thị overlay
            $(".overlay").addClass("active");
        },
        error: function (xhr) {
            alert("Không thể lấy chi tiết đơn hàng.");
            console.error("Lỗi:", xhr.responseText);
        }
    });
}

// tắt overlay
$(document).ready(function () {
    $("#close-invoice-details").on("click", function () {
        $(".overlay").removeClass("active");
    });
});