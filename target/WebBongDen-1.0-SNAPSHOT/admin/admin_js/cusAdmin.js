document.addEventListener("DOMContentLoaded", function () {
    $(document).ready(function () {
        const table = $(".customer-table").DataTable({
            ajax: {
                url: "/SystemSecurity_war/list-customer", // URL Servlet trả về JSON
                type: "GET", // Phương thức HTTP
                dataSrc: "", // DataTables sẽ lấy dữ liệu từ gốc JSON
                data: function (d) {
                    d.searchValue = $("#customer-search").val(); // Truyền giá trị tìm kiếm
                },
            },
            error: function (xhr, error, thrown) {
                console.log("Error:", error);
                console.log("Response Text:", xhr.responseText);
            },
            destroy: true, // Đảm bảo bảng được làm mới khi reload
            autoWidth: false,
            paging: true,
            pageLength: 10,
            columns: [
                { data: "customerId" }, // ID khách hàng
                { data: "customerName" }, // Tên khách hàng
                { data: "email" }, // Email
                { data: "phone" }, // Số điện thoại
                { data: "address" }, // Địa chỉ
                {
                    data: "createdAt", // Ngày đăng ký
                    render: function (data) {
                        const date = new Date(data);
                        return `${date.getDate().toString().padStart(2, "0")}/${(date.getMonth() + 1)
                            .toString()
                            .padStart(2, "0")}/${date.getFullYear()}`;
                    },
                },
                {
                    data: null, // Lịch sử mua hàng: Nút xem chi tiết
                    render: function (data, type, row) {
                        return `<button class="view-details" data-index="${row.customerId}">Xem chi tiết</button>`;
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
                info: "Hiển thị _START_ đến _END_ của _TOTAL_ sản phẩm",
            },
        });
        $("#search-btn-cus").on("click", function () {
            table.ajax.reload(); // Reload lại bảng với dữ liệu lọc mới
        });

        $(".customer-table").on("click", ".view-details", function () {
            const customerId = $(this).data("index");

            // Gửi yêu cầu đến servlet để lấy dữ liệu đơn hàng của khách hàng
            $.ajax({
                url: `/SystemSecurity_war/customer-orders`, // URL Servlet trả về JSON
                type: "GET",
                data: { customerId: customerId }, // Truyền ID khách hàng
                success: function (orders) {
                    // Kiểm tra nếu có dữ liệu trả về
                    if (orders && orders.length > 0) {
                        // Tạo HTML cho bảng hiển thị trong SweetAlert
                        let tableHtml = `
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>ID Đơn Hàng</th>
                                        <th>Ngày Tạo</th>
                                        <th>Tổng Tiền</th>
                                    </tr>
                                </thead>
                                <tbody>`;
                        orders.forEach((order, index) => {
                            const formattedDate = order.formattedCreateAt || "N/A"; // Sử dụng ngày đã định dạng hoặc hiển thị "N/A" nếu không có

                            tableHtml += `
                            <tr>
                                <td>${index + 1}</td>
                                <td>${order.id}</td>
                                <td>${formattedDate}</td>
                                <td>${order.totalPrice.toLocaleString()} VND</td>
                            </tr>`;
                        });

                        tableHtml += `</tbody></table>`;

                        // Hiển thị SweetAlert với bảng chi tiết
                        Swal.fire({
                            title: "Lịch sử mua hàng",
                            html: tableHtml,
                            width: "800px", // Tăng độ rộng của modal
                            showCloseButton: true,
                        });
                    } else {
                        Swal.fire({
                            title: "Thông báo",
                            text: "Không có đơn hàng nào cho khách hàng này.",
                            icon: "warning",
                        });
                    }
                },
                error: function (xhr, status, error) {
                    Swal.fire({
                        title: "Lỗi",
                        text: "Không thể tải dữ liệu đơn hàng.",
                        icon: "error",
                    });
                    console.error("Error:", error);
                },
            });
        });
    })
})