$(document).on("click", ".change-order-btn", function () {
    const orderId = $(this).data("id");

    // Lấy dữ liệu ngày tạo đơn hàng
    const rowData = {
        createAt: $(this).closest("tr").find("td:nth-child(2)").text(),
    };

    // Gọi hàm hiển thị chi tiết đơn hàng
    viewOrderDetails(orderId, rowData);
});

function viewOrderDetails(orderId, rowData) {
    // Lấy thông tin ngày tạo đơn hàng
    $("#create_at").text(rowData.createAt || "N/A");
    $("#verification-status").text("Đang kiểm tra...");

    // Gửi AJAX để lấy chi tiết đơn hàng
    $.ajax({
        url: "/SystemSecurity_war/order-info",
        type: "GET",
        data: {orderId: orderId},
        success: function (data) {
            const orderDetails = data.orderDetails;
            const order = data.order;

            if (!order) {
                console.warn("Không tìm thấy đơn hàng trong dữ liệu trả về:", data);
                return;
            }

            $("#order_id").text(order.id || "N/A");
            $("#form_of_delivery").text(order.shippingMethod || "N/A");
            $("#delivery_fee").text(order.shippingFee ?? "N/A");
            $("#note").text(order.note ?? "N/A");

            // Gán thông tin khách hàng
            $("#customer-name").text(order.customerName || "N/A");
            $("#customer-address").text(order.address || "N/A");
            $("#customer-phone").text(order.phone || "N/A");

            if (order.orderStatus && order.orderStatus.toLowerCase() === "pending") {
                const email = $("#email").val();
                const editUrl = `/SystemSecurity_war/edit-order?orderId=${order.id}&email=${email}`;

                $("#edit-order-btn")
                    .off("click")
                    .on("click", function () {
                        window.location.href = editUrl;
                    })
                    .show();
            } else {
                $("#edit-order-btn").hide();
                $("#update-signature-btn").hide()
            }

            // Gán chi tiết đơn hàng
            const $orderItemsBody = $("#order-items-body");
            $orderItemsBody.empty();

            orderDetails.forEach((item) => {
                $orderItemsBody.append(`
                <tr>
                    <td>${item.productId}</td>
                    <td>${item.productName}</td>
                    <td>${item.quantity}</td>
                    <td>${parseFloat(item.unitPrice).toLocaleString("vi-VN", {style: "currency", currency: "VND"})}</td>
                    <td>${parseFloat(item.itemDiscount).toLocaleString("vi-VN", {style: "currency", currency: "VND"})}</td>
                    <td>${parseFloat(item.amount).toLocaleString("vi-VN", {style: "currency", currency: "VND"})}</td>
                </tr>
                `);
            });

            const totalAmount = orderDetails.reduce((total, item) => total + parseFloat(item.amount), 0);
            $("#total-amount").text(totalAmount.toLocaleString("vi-VN", {
                style: "currency",
                currency: "VND",
            }));

            // Gửi AJAX để kiểm tra chữ ký
            $.ajax({
                url: "/SystemSecurity_war/verify-order-signature",
                type: "POST",
                data: {orderId: orderId},
                success: function (response) {
                    if (response.success) {
                        $("#verification-status").text(response.message).css("color", "green");
                    } else {
                        $("#verification-status").text(response.message).css("color", "red");
                    }
                },
                error: function (xhr) {
                    $("#verification-status").text("Lỗi khi kiểm tra chữ ký.").css("color", "red");
                    console.error("Lỗi:", xhr.responseText);
                }
            });

            // Hiển thị overlay
            $("#order-popup").addClass("active");
        },
        error: function (xhr) {
            alert("Không thể lấy chi tiết đơn hàng.");
            console.error("Lỗi:", xhr.responseText);
        }
    });
}

// Tắt overlay
$(document).ready(function () {
    $("#close-invoice-details").on("click", function () {
        $("#order-popup").removeClass("active");
    });
});

// Gắn sự kiện click cho nút Cập nhật chữ ký
document.getElementById("update-signature-btn").addEventListener("click", function () {
    document.getElementById("popupSignatureOverlay").classList.remove("hidden");
    document.getElementById("order-popup").classList.remove("active");
    const orderId = document.getElementById("order_id").textContent;
    document.getElementById("signatureOrderId").value = orderId;
});

// Gắn sự kiện submit cho form cập nhật chữ ký
document.getElementById("signatureForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);

    try {
        const resp = await fetch('/SystemSecurity_war/update-digital-signature', {
            method: 'POST',
            body: formData
        });

        if (!resp.ok) throw new Error("Network response was not ok");
        const data = await resp.json();

        if (data.success) {
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: data.message,
                confirmButtonColor: '#28a745'
            }).then(() => {
                closeSignaturePopup();
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: data.message,
                confirmButtonColor: '#dc3545'
            });
        }
    } catch (err) {
        console.error("Lỗi khi gửi AJAX:", err);
        Swal.fire({
            icon: 'error',
            title: 'Lỗi kết nối',
            text: 'Không thể gửi dữ liệu tới máy chủ.',
            confirmButtonColor: '#dc3545'
        });
    }
});

// Hàm đóng popup chữ ký và hiển thị lại popup đơn hàng
function closeSignaturePopup() {
    document.getElementById("popupSignatureOverlay").classList.add("hidden");
    document.getElementById("order-popup").classList.add("active");
}

// Gắn sự kiện click cho nút Tải đơn hàng
$(document).on("click", "#download-order-btn", function () {
    const orderId = $("#order_id").text();
    window.location.href = `/SystemSecurity_war/download-order?orderId=${orderId}`;
});