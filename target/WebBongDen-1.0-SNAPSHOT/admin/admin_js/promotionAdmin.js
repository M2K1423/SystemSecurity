document.getElementById("promotion-form").addEventListener("submit", async function (e) {
    e.preventDefault(); // Ngăn form gửi dữ liệu mặc định

    const formData = {
        promotionName: document.getElementById("promotionName").value,
        endDay: document.getElementById("promotionEndDate").value, // JSON key 'endDay' phải khớp với model
        discountPercent: parseFloat(document.getElementById("promotionDiscount").value), // JSON key 'discountPercent' phải khớp
        promotionType: document.getElementById("promotionType").value,
    };

    console.log("Form Data:", formData);

    try {
        // Nếu ngày hợp lệ, tiếp tục gửi dữ liệu
        const response = await fetch("/WebBongDen_war/add-promotion", {
            method: "POST",
            headers: {
                "Content-Type": "application/json", // Gửi dữ liệu JSON
            },
            body: JSON.stringify(formData), // Chuyển đổi dữ liệu sang JSON
        });

        const result = await response.json();
        console.log("Response:", result);

        if (response.ok) {
            Swal.fire({
                icon: "success",
                title: "Thành công!",
                text: result.message || "Thêm chương trình khuyến mãi thành công!",
                confirmButtonText: "OK",
            }).then(() => {
                document.getElementById("promotion-form").reset(); // Reset form sau khi thành công
            });
        } else {
            Swal.fire({
                icon: "error",
                title: "Lỗi!",
                text: result.message || "Đã xảy ra lỗi.",
                confirmButtonText: "Thử lại",
            });
        }
    } catch (error) {
        console.error("Error:", error);
        Swal.fire({
            icon: "error",
            title: "Lỗi hệ thống!",
            text: "Đã xảy ra lỗi trong quá trình gửi dữ liệu. Vui lòng thử lại sau.",
            confirmButtonText: "OK",
        });
    }
});

$(document).ready(function () {
    // Lấy danh sách promotion cho dropdown
    $.ajax({
        url: "/WebBongDen_war/list-promotion",
        method: "GET",
        dataType: "json",
        success: function (promotions) {
            const promotionSelect = $("#promotion-select");

            promotions.forEach(promotion => {
                const option = $("<option></option>")
                    .val(promotion.id) // Giá trị là ID của chương trình
                    .text(promotion.promotionName); // Nội dung là tên chương trình

                promotionSelect.append(option);
            });
        },
        error: function (xhr, status, error) {
            console.error("Failed to fetch promotions for dropdown:", error);
            alert("Không thể tải danh sách chương trình giảm giá.");
        }
    });

    // Lấy danh sách promotion cho bảng
    $("#promotion-table").DataTable({
        ajax: {
            url: "/WebBongDen_war/list-promotion", // Dùng lại URL
            type: "GET",
            dataSrc: "", // Lấy từ gốc của JSON
        },
        columns: [
            { data: "promotionName" },
            { data: "startDay" },
            { data: "endDay" },
            {
                data: null,
                render: function (data, type, row) {
                    return `${row.promotionType} - ${row.discountPercent}%`;
                },
            },
            {
                data: null,
                render: function (data, type, row) {
                    return `
                        <button class="view-details" data-id="${row.id}">Xem DSSP</button>`;
                },
            },
            {
                data: null,
                render: function (data, type, row) {
                    return `
                        <button class="edit-promotion" data-id="${row.id}">Chỉnh sửa</button>
                        <button class="delete-promotion" data-id="${row.id}">Xóa</button>`;
                },
            },
        ],
        lengthChange: false,
        searching: false,
        ordering: true,
        info: true,
        language: {
            paginate: {
                previous: "Trước",
                next: "Tiếp",
            },
            info: "Hiển thị _START_ đến _END_ của _TOTAL_ chương trình",
        },
    });
});

document.getElementById("product-to-promotion-form").addEventListener("submit", async function (e) {
    e.preventDefault();

    const formData = {
        promotionId: document.getElementById("promotion-select").value,
        productId: document.getElementById("product-id").value,
    };

    try {
        const response = await fetch("/WebBongDen_war/add-product-to-promotion", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        });

        const result = await response.json();
        if (response.ok) {
            Swal.fire({
                icon: "success",
                title: "Thành công!",
                text: result.message,
            });
        } else {
            Swal.fire({
                icon: "error",
                title: "Lỗi!",
                text: result.message,
            });
        }
    } catch (error) {
        Swal.fire({
            icon: "error",
            title: "Lỗi!",
            text: "Đã xảy ra lỗi khi gửi dữ liệu.",
        });
        console.error("Error:", error);
    }
});

$(document).on('click', '.view-details', function () {
    const promotionId = $(this).data('id'); // Lấy ID chương trình khuyến mãi từ data-id

    // Gửi yêu cầu GET tới API để lấy danh sách sản phẩm
    $.ajax({
        url: '/WebBongDen_war/get-products-by-promotion', // API trả về danh sách sản phẩm
        type: 'GET',
        data: { promotionId: promotionId },
        success: function (response) {
            if (response.success) {
                // Nếu thành công, hiển thị danh sách sản phẩm
                let productTable = `
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>ID SP</th>
                                <th>Tên Sản Phẩm</th>
                                <th>Giá</th>
                                <th>Hành Động</th>
                            </tr>
                        </thead>
                        <tbody>
                `;

                response.data.forEach(product => {
                    productTable += `
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.productName}</td>
                            <td>${product.unitPrice}</td>
                            <td>
                                <button class="btn btn-danger delete-product-inpromotion" data-id="${product.id}" data-promotion-id="${promotionId}">
                                    Xóa
                                </button>
                            </td>
                        </tr>
                    `;
                });

                productTable += `
                        </tbody>
                    </table>
                `;

                // Hiển thị danh sách sản phẩm trong SweetAlert modal
                Swal.fire({
                    title: 'Danh Sách Sản Phẩm',
                    html: productTable, // Hiển thị bảng sản phẩm
                    showCloseButton: true,
                    width: '1000px'
                });
            } else {
                // Nếu không có sản phẩm nào
                Swal.fire({
                    icon: 'info',
                    title: 'Thông báo',
                    text: response.message || 'Không có sản phẩm nào trong chương trình khuyến mãi.',
                });
            }
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: 'Không thể tải danh sách sản phẩm.',
            });
        }
    });
});

// Xóa sản phẩm khỏi chương trình khuyến mãi
$(document).on('click', '.delete-product-inpromotion', function () {
    const productId = $(this).data('id');
    const promotionId = $(this).data('promotion-id');

    Swal.fire({
        title: 'Bạn có chắc chắn?',
        text: 'Hành động này không thể hoàn tác!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            // Gửi yêu cầu DELETE tới API để xóa sản phẩm khỏi chương trình khuyến mãi
            $.ajax({
                url: '/WebBongDen_war/remove-product-from-promotion',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ promotionId: promotionId, productId: productId }),
                success: function (response) {
                    if (response.success) {
                        Swal.fire(
                            'Đã xóa!',
                            response.message || 'Sản phẩm đã được xóa khỏi chương trình khuyến mãi.',
                            'success'
                        ).then(() => {
                            // Tải lại danh sách sản phẩm
                            $('.view-details[data-id="' + promotionId + '"]').trigger('click');
                        });
                    } else {
                        Swal.fire('Lỗi!', response.message || 'Không thể xóa sản phẩm.', 'error');
                    }
                },
                error: function () {
                    Swal.fire('Lỗi!', 'Bạn phaỉ xóa sản phẩm trong chương trình trước.', 'error');
                }
            });
        }
    });
});

$(document).on("click", ".delete-promotion", function () {
    const promotionId = $(this).data("id"); // Lấy ID chương trình khuyến mãi từ data-id

    Swal.fire({
        title: "Bạn có chắc chắn?",
        text: "Hành động này không thể hoàn tác!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
    }).then((result) => {
        if (result.isConfirmed) {
            // Gửi yêu cầu DELETE tới API để xóa chương trình khuyến mãi
            $.ajax({
                url: "/WebBongDen_war/delete-promotion",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ promotionId: promotionId }), // Dữ liệu JSON chứa promotionId
                success: function (response) {
                    if (response.success) {
                        Swal.fire(
                            "Đã xóa!",
                            response.message || "Chương trình khuyến mãi đã được xóa.",
                            "success"
                        ).then(() => {
                            // Reload lại DataTable để cập nhật danh sách chương trình khuyến mãi
                            $("#promotion-table").DataTable().ajax.reload();
                        });
                    } else {
                        Swal.fire(
                            "Lỗi!",
                            response.message || "Không thể xóa chương trình khuyến mãi.",
                            "error"
                        );
                    }
                },
                error: function () {
                    Swal.fire(
                        "Lỗi!",
                        "Không thể kết nối với máy chủ.",
                        "error"
                    );
                },
            });
        }
    });
});




