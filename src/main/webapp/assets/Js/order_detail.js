
$(document).on("click", ".change-order-btn", function () {
    const orderId = $(this).data("id");

    // Lấy dữ ngày tạo đơn hàng
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

            // Hiển thị overlay
            $("#order-popup").addClass("active");
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
        $("#order-popup").removeClass("active");
    });
});


// Gắn sự kiện click cho nút Cập nhât chữ kí
document.getElementById("update-signature-btn").addEventListener("click", function () {
    // Hiện popup cập nhật chữ kí
    document.getElementById("popupSignatureOverlay").classList.remove("hidden");

    // Ẩn Chi tiết đơn hàng
    document.getElementById("order-popup").classList.remove("active");

    // Cập nhật orderId từ popup đơn hàng
    const orderId = document.getElementById("order_id").textContent;
    document.getElementById("signatureOrderId").value = orderId;
});

// Gắn sự kiện submit cho form cập nhật chữ kí
document.getElementById("signatureForm").addEventListener("submit", async (event) => {
    event.preventDefault();          // tránh reload trang

    const formData = new FormData(event.target); // lấy dữ liệu form

    try {
        // gửi yêu cầu POST tới Servlet để cập nhật chư kí
        const resp = await fetch('/SystemSecurity_war/update-digital-signature', {
            method: 'POST',
            body: formData
        });

        // Nếu phản hồi lỗi thì ném ngoại lệ
        if (!resp.ok) throw new Error("Network response was not ok");

        // Parse Json từ phản hồi
        const data = await resp.json();   // { success: true/false, message: "..." }

        if (data.success) {
            // Nếu cập nhật thành công, hiện thông báo thành công bằng SweetAlert
            Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: data.message,       // thông điệp từ servlet
                confirmButtonColor: '#28a745'
            }).then(() => {
                // Đóng popup chữ ký và mở lại popup chi tiết đơn hàng
                closeSignaturePopup();
            });
        } else {
            // Nếu cập nhật thất bại (do logic từ backend), hiển thị lỗi
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: data.message,
                confirmButtonColor: '#dc3545'
            });
        }
    } catch (err) {
        // Hiện thông báo trong Lỗi trong quá trình gửi
        console.error("Lỗi khi gửi AJAX:", err);
        Swal.fire({
            icon: 'error',
            title: 'Lỗi kết nối',
            text: 'Không thể gửi dữ liệu tới máy chủ.',
            confirmButtonColor: '#dc3545'
        });
    }
});

// Hàm đóng popup chữ ký và hiển thị lại popup đơn hàng cữ
function closeSignaturePopup() {
    document.getElementById("popupSignatureOverlay").classList.add("hidden");
    // Hiện lại popup 1
    document.getElementById("order-popup").classList.add("active");
}