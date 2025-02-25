document.addEventListener("DOMContentLoaded", function () {
    const editButton = document.getElementById("edit-image-btn");
    const cancelEditButton = document.getElementById("cancel-edit-btn");
    const imageSection = document.getElementById("image-section");
    const uploadFileSection = document.getElementById("upload-file-section");
    const imageUrlInput = document.getElementById("product-image-url");
    const fileInput = document.getElementById("upload-product-image");

    // Khi nhấn "Chỉnh sửa"
    // editButton.addEventListener("click", function () {
    //     // Ẩn phần URL và nút "Chỉnh sửa"
    //     imageSection.style.display = "none";
    //
    //     // Hiện phần upload file và nút "Hủy"
    //     uploadFileSection.style.display = "flex";
    // });
    //
    // // Khi nhấn "Hủy"
    // cancelEditButton.addEventListener("click", function () {
    //     // Hiện lại phần URL và nút "Chỉnh sửa"
    //     imageSection.style.display = "flex";
    //
    //     // Ẩn phần upload file
    //     uploadFileSection.style.display = "none";
    //
    //     // Xóa file đã chọn (nếu có)
    //     fileInput.value = "";
    // });



    $(document).ready(function () {
        const table = $("#product-table").DataTable({
            ajax: {
                url: "/WebBongDen_war/AdminLoadProductController", // URL Servlet
                type: "GET", // Phương thức HTTP
                data: function (d) {
                    d.searchValue = $("#product-search").val();
                },
                dataSrc: "", // DataTables sẽ lấy dữ liệu từ gốc JSON
            },
            error: function (xhr, error, thrown) {
                console.log("Error:", error);
                console.log("Response Text:", xhr.responseText);
            },
            destroy: true,
            autoWidth: false,
            paging: true,
            pageLength: 10,
            columns: [
                { data: "id" }, // Id
                {
                    data: "imageUrl", // Hình ảnh
                    render: function (data) {
                        // Kiểm tra nếu đường dẫn đã chứa 'assets/images/'

                        return `<img src="${data}" alt="Product Image" style="width: 50px; height: 50px;">`;
                    },
                },
                { data: "productName" }, // Tên sản phẩm
                {
                    data: "unitPrice", // Giá
                    render: function (data) {
                        return parseInt(data).toLocaleString("vi-VN", {
                            style: "currency",
                            currency: "VND",
                        });
                    },
                },
                { data: "categoryName" }, // Loại sản phẩm
                { data: "createdAt" }, // Ngày thêm
                {
                    data: null, // Thao tác: Xem chi tiết
                    render: function (data, type, row) {
                        return `
                <button class="view-details" data-id="${row.id}" data-page="product-management">Xem chi tiết</button>
                `;
                    },
                },
                {
                    data: null, // Thao tác: Xóa
                    render: function (data, type, row) {
                        return `<button class="delete-product" data-id="${row.id}">
                      <i class="fa-regular fa-trash-can"></i>
                  </button>`;
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

        $("#product-table").on("click", ".view-details", function () {
            $(".product-stats").hide();
            const productId = $(this).data("id"); // Lấy product ID từ data-id
            console.log("Product ID:", productId); // Debug: Kiểm tra giá trị ID

            // Gửi yêu cầu tới server để lấy chi tiết sản phẩm
            fetch(`/WebBongDen_war/getProductDetails?id=${productId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Lỗi khi tải chi tiết sản phẩm");
                    }
                    return response.json();
                })
                .then(data => {
                    // Cập nhật các trường trong modal
                    $("#product-id-details").text(data.id || "N/A");
                    $("#product-name-details").text(data.productName || "N/A");
                    $("#product-image-main").attr("src", data.mainImageUrl || "./img/default-product.png");
                    $("#product-image-view").text(data.mainImageUrl || "./img/default-product.png");
                    $("#product-name-view").text(data.productName || "N/A");
                    $("#product-id-view").text(data.id || "N/A");
                    $("#product-price-view").text(`${data.unitPrice || 0} VNĐ`);
                    $("#product-category-view").text(data.categoryName || "N/A");
                    $("#product-status-view").text(data.productStatus || "N/A");
                    $("#product-description-view").text(data.description || "N/A");
                    $("#product-date-view").text(data.createdAt || "N/A");
                    $("#product-discount-view").text(`${data.discountPercent || 0}%`);
                    $("#product-stock-view").text(data.stockQuantity || 0);
                    $("#product-rating-view").text(data.rating || 0);
                    $("#product-warranty-view").text(data.warrantyPeriod || "N/A");
                    $("#product-material-view").text(data.material || "N/A");
                    $("#product-color-view").text(data.lightColor || "N/A");
                    $("#product-lifespan-view").text(data.usageAge || "N/A");
                    $("#product-power-view").text(data.voltage || "N/A");

                    $("#edit-product-image").val(data.mainImageUrl || "./img/default-product.png");
                    $("#edit-product-name").val(data.productName || "N/A");
                    $("#edit-product-id").val(data.id || "N/A");
                    $("#edit-product-price").val(`${data.unitPrice || 0}`);
                    $("#edit-product-category").val(data.categoryName || "N/A");
                    $("#edit-product-status").val(data.productStatus || "N/A");
                    $("#edit-product-description").val(data.description || "N/A");
                    $("#edit-product-date").val(data.createdAt || "N/A");
                    $("#edit-product-discount").val(`${data.discountPercent || 0}`);
                    $("#edit-product-stock").val(data.stockQuantity || 0);
                    $("#edit-product-rating").val(data.rating || 0);
                    $("#edit-product-warranty").val(data.warrantyPeriod || "N/A");
                    $("#edit-product-material").val(data.material || "N/A");
                    $("#edit-product-color").val(data.lightColor || "N/A");
                    $("#edit-product-lifespan").val(data.usageAge || "N/A");
                    $("#edit-product-power").val(data.voltage || "N/A");

                    // Hiển thị modal
                    $("#product-details").css("display", "block");
                    $(".tab-content-container").css("display", "none"); // Ẩn danh sách sản phẩm
                })
                .catch(error => {
                    console.error(error); // Log lỗi
                    alert("Đã xảy ra lỗi khi tải chi tiết sản phẩm. Vui lòng thử lại!");
                });
        });


        $("#close-details-btn").on("click", function () {
            $(".product-stats").show();
            $("#product-details").hide();
            $(".tab-content-container").show();
        });

        $("#search-btn-product").on("click", function () {
            table.ajax.reload();
        });

        // Sự kiện click nút "Thêm danh mục"
        $(".add-category").on("click", function () {
            $("#overlay-add-category").addClass("active");
            loadCategories(); // Tải danh sách danh mục cha khi overlay được hiển thị
        });

        $("#close-overlay-add-category").on("click", function () {
            $("#overlay-add-category").removeClass("active");
        });

// Hàm tải danh sách danh mục cha
        function loadCategories() {
            fetch("/WebBongDen_war/categories")
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Không thể tải danh mục cha");
                    }
                    return response.json();
                })
                .then((data) => {
                    const tableBody = $("#categories-table-body");
                    tableBody.empty(); // Xóa nội dung bảng cũ

                    // Hiển thị từng danh mục cha
                    data.forEach((category) => {
                        const row = `
                    <tr>
                        <td>${category.id}</td>
                        <td>${category.categoryName}</td>
                        <td>
                            <button class="view-sub-categories-btn btn btn-info" data-id="${category.id}">DanhMucCon</button>
                        </td>
                        <td>
                            <button class="delete-category-btn btn btn-danger" data-id="${category.id}">Xóa</button>
                        </td>
                    </tr>
                `;
                        tableBody.append(row);
                    });

                    // Gán sự kiện cho các nút "DS Danh mục con" và "Xóa"
                    assignCategoryEvents();
                })
                .catch((error) => {
                    console.error("Lỗi khi tải danh mục:", error);
                    Swal.fire("Lỗi", "Không thể tải danh mục.", "error");
                });
        }

// Gán sự kiện cho các nút sau khi tải danh sách danh mục cha
        function assignCategoryEvents() {
            // Nút "DS Danh mục con"
            $(".view-sub-categories-btn").off("click").on("click", function () {
                const categoryId = $(this).data("id");
                loadSubCategories(categoryId);
                $("#overlay-add-category").removeClass("active");
            });

            // Nút "Xóa"
            $(".delete-category-btn").off("click").on("click", function () {
                const categoryId = $(this).data("id");
                deleteCategory(categoryId);
            });
        }

// Hàm tải danh mục con
        function loadSubCategories(categoryId) {
            fetch(`/WebBongDen_war/categories/subcategories?categoryId=${categoryId}`)
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Không thể tải danh mục con");
                    }
                    return response.json();
                })
                .then((subCategories) => {
                    // Tạo bảng danh mục con
                    let subCategoryTable = `
                <table style="width: 100%; border-collapse: collapse; text-align: left;">
                    <thead>
                        <tr>
                            <th style="border: 1px solid #ddd; padding: 8px;">ID</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Tên danh mục con</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

                    // Thêm các dòng danh mục con vào bảng
                    subCategories.forEach((subCategory) => {
                        subCategoryTable += `
                    <tr>
                        <td style="border: 1px solid #ddd; padding: 8px;">${subCategory.id}</td>
                        <td style="border: 1px solid #ddd; padding: 8px;">${subCategory.name}</td>
                        <td style="border: 1px solid #ddd; padding: 8px;">
                            <button class="delete-sub-category-btn btn btn-danger" data-id="${subCategory.id}">Xóa</button>
                        </td>
                    </tr>
                `;
                    });

                    subCategoryTable += `
                    </tbody>
                </table>
            `;

                    // Hiển thị bảng trong SweetAlert
                    Swal.fire({
                        title: "Danh sách danh mục con",
                        html: subCategoryTable,
                        width: "800px",
                        showCloseButton: true,
                        showConfirmButton: false,
                        didClose: () => {
                            // Mở lại Overlay khi SweetAlert bị đóng
                            $("#overlay-add-category").addClass("active");
                        },
                    });

                    // Gán sự kiện cho các nút "Xóa" danh mục con
                    assignSubCategoryEvents();
                })
                .catch((error) => {
                    console.error("Lỗi khi tải danh mục con:", error);
                    Swal.fire("Lỗi", "Không thể tải danh mục con.", "error");
                });
        }

// Gán sự kiện cho nút "Xóa" danh mục con
        function assignSubCategoryEvents() {
            $(".delete-sub-category-btn").off("click").on("click", function () {
                const subCategoryId = $(this).data("id");
                deleteSubCategory(subCategoryId);
            });
        }

// Hàm xóa danh mục cha
        function deleteSubCategory(subCategoryId, categoryId) {
            Swal.fire({
                title: "Bạn có chắc chắn?",
                text: "Bạn có muốn xóa danh mục con này không? Hành động này không thể hoàn tác!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Có",
                cancelButtonText: "Hủy",
            }).then((result) => {
                if(result.isConfirmed) {
                    fetch(`/WebBongDen_war/subcategories/delete/${subCategoryId}`, { method: "DELETE" })
                        .then((response) => {
                            if (!response.ok) throw new Error("Không thể xóa danh mục con");
                            Swal.fire({
                                icon: "success",
                                title: "Thành công",
                                text: "Danh mục con đã được xóa.",
                                showConfirmButton: false,
                                timer: 1000,
                            });
                        })
                        .catch(() => Swal.fire("Lỗi", "Không thể xóa danh mục con.", "error"));
                }
            });
        }


// Hàm xóa danh mục con
        function deleteCategory(categoryId) {
            Swal.fire({
                title: "Bạn có chắc chắn?",
                text: "Bạn có muốn xóa danh mục này không? Hành động này không thể hoàn tác!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Có",
                cancelButtonText: "Hủy",
            }).then((result) => {
                if(result.isConfirmed) {
                    fetch(`/WebBongDen_war/categories/delete/${categoryId}`, { method: "DELETE" })
                        .then((response) =>
                            response.ok
                                ? response.json()
                                : response.json().then((data) => Promise.reject(new Error(data.message || "Không thể xóa danh mục")))
                        )
                        .then((data) => {
                            Swal.fire("Thành công", data.message || "Danh mục đã được xóa.", "success");
                            loadCategories();
                        })
                        .catch((error) => {
                            console.error("Lỗi khi xóa danh mục:", error);
                            Swal.fire("Lỗi", error.message, "error");
                        })
                        .finally(() => $("#overlay-add-category").removeClass("active"));
                }
            });
        }

        $("#category-form").on("submit", function (event) {
            event.preventDefault(); // Ngăn chặn reload trang

            const categoryName = $("#category-name").val().trim();
            if (!categoryName) {
                Swal.fire("Lỗi", "Tên danh mục không được để trống.", "error");
                return;
            }

            // Gửi yêu cầu thêm danh mục cha
            $.ajax({
                url: "/WebBongDen_war/categories/add",
                method: "POST",
                contentType: "application/x-www-form-urlencoded",
                data: { name: categoryName },
                success: function (response) {
                    Swal.fire("Thành công", response.message, "success");
                    $("#category-name").val(""); // Xóa nội dung input
                    table.ajax.reload();
                },
                error: function (xhr) {
                    const errorMessage = xhr.responseJSON?.message || "Đã xảy ra lỗi khi thêm danh mục cha.";
                    Swal.fire("Lỗi", errorMessage, "error");
                },
            });
        });

        // Xử lý form thêm danh mục con
        $("#sub-category-form").on("submit", function (event) {
            event.preventDefault(); // Ngăn chặn reload trang

            const parentId = $("#parent-category").val();
            const subCategoryName = $("#sub-category-name").val().trim();

            if (!parentId || !subCategoryName) {
                Swal.fire("Lỗi", "Vui lòng chọn danh mục cha và nhập tên danh mục con.", "error");
                return;
            }

            // Gửi yêu cầu thêm danh mục con
            $.ajax({
                url: "/WebBongDen_war/subcategories/add",
                method: "POST",
                contentType: "application/x-www-form-urlencoded",
                data: { parentId: parentId, name: subCategoryName },
                success: function (response) {
                    Swal.fire("Thành công", response.message, "success");
                    $("#sub-category-name").val(""); // Xóa nội dung input
                },
                error: function (xhr) {
                    const errorMessage = xhr.responseJSON?.message || "Đã xảy ra lỗi khi thêm danh mục con.";
                    Swal.fire("Lỗi", errorMessage, "error");
                },
            });
        });

        $('#save-product-btn').on('click', function (e) {
            e.preventDefault(); // Ngăn chặn hành vi submit form mặc định

            // Lấy dữ liệu từ form HTML
            const formData = new FormData($('.details-form')[0]);

            // Thêm kiểm tra dữ liệu trước khi gửi (nếu cần)
            if (!formData.get('id') || !formData.get('productName')) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: 'ID sản phẩm và Tên sản phẩm không được để trống!',
                });
                return;
            }

            // Gửi AJAX
            $.ajax({
                url: '/WebBongDen_war/edit-product-detail', // URL của servlet
                type: 'POST',
                data: formData,
                processData: false, // Không xử lý dữ liệu
                contentType: false, // Để contentType mặc định
                success: function (response) {
                    // Xử lý khi server trả về thành công
                    if (response.success) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công',
                            text: response.message,
                        }).then(() => {
                            window.location.reload(); // Tải lại trang hoặc chuyển hướng
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi',
                            text: response.message,
                        });
                    }
                },
                error: function () {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: 'Không thể kết nối với máy chủ!',
                    });
                },
            });
        });
    });




    // Hiển thị overlay
    document.querySelector(".add-product").addEventListener("click", function () {
        const overlay = document.getElementById("overlay-add-product");
        overlay.classList.add("active");
    });

    // Ẩn overlay khi nhấn ra ngoài
    document.querySelector(".fa-xmark").addEventListener("click", function () {
        const overlay = document.getElementById("overlay-add-product");
        overlay.classList.remove("active");
    });

    document.querySelector("#product-table").addEventListener("click", function (event) {
        if (event.target.classList.contains("delete-product")) {
            const productId = event.target.dataset.id; // Lấy ID sản phẩm từ data-id

            // Hiển thị hộp thoại xác nhận với SweetAlert
            Swal.fire({
                title: "Bạn có chắc chắn?",
                text: "Bạn sẽ không thể khôi phục sản phẩm này!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Xóa",
                cancelButtonText: "Hủy"
            }).then((result) => {
                if (result.isConfirmed) {
                    // Gửi yêu cầu xóa qua AJAX
                    fetch("deleteProduct", {
                        method: "POST",
                        body: new URLSearchParams({
                            action: "delete-product",
                            id: productId
                        })
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error("Có lỗi xảy ra trong quá trình xóa!");
                            }
                            return response.json();
                        })
                        .then(data => {
                            if (data.status === "success") {
                                Swal.fire({
                                    title: "Đã xóa!",
                                    text: data.message,
                                    icon: "success",
                                    timer: 1500,
                                    showConfirmButton: false
                                });

                                // Tìm dòng cha của nút xóa
                                const row = event.target.closest("tr");

                                if (row) {
                                    // Sử dụng DataTables API để xóa dòng
                                    const table = $('#product-table').DataTable();
                                    table.row(row).remove().draw(false); // Giữ nguyên trang và cập nhật tổng số
                                }
                            } else {
                                Swal.fire({
                                    title: "Lỗi",
                                    text: data.message,
                                    icon: "error",
                                    confirmButtonText: "OK"
                                });
                            }
                        })
                        .catch(error => {
                            console.error("Error:", error);
                            Swal.fire({
                                title: "Lỗi",
                                text: "Có lỗi xảy ra khi xóa sản phẩm!",
                                icon: "error",
                                confirmButtonText: "OK"
                            });
                        });
                }
            });
        }
    });

    document
        .getElementById("edit-product-btn")
        .addEventListener("click", function () {
            // Lấy tất cả các phần tử `span` và `input` bên trong div .details-content
            const detailsContent = document.querySelector(".details-content");

            const spans = detailsContent.querySelectorAll("span");
            const inputs = detailsContent.querySelectorAll("input, textarea");

            // Ẩn tất cả các `span` (view) và hiển thị tất cả các `input` (edit)
            spans.forEach((span) => {
                span.style.display = "none";
            });

            inputs.forEach((input) => {
                input.style.display = "block";
            });

            document.getElementById("edit-product-category").style.display = "block";


            // Hiển thị nút Lưu, Ẩn nút Chỉnh sửa
            document.getElementById("save-product-btn").style.display = "block";
            document.getElementById("cancel-product-btn").style.display = "block";
            document.getElementById("edit-product-btn").style.display = "none";
        });

    // Hàm lưu thay đổi
    document
        .getElementById("save-product-btn")
        .addEventListener("click", function () {
            // Lấy tất cả các phần tử `span` và `input` bên trong div .details-content
            const detailsContent = document.querySelector(".details-content");

            const spans = detailsContent.querySelectorAll("span");
            const inputs = detailsContent.querySelectorAll("input, textarea");

            // Cập nhật giá trị từ `input` sang `span`
            inputs.forEach((input) => {
                const correspondingSpan = input.previousElementSibling; // Lấy span liền trước input
                if (correspondingSpan) {
                    correspondingSpan.textContent = input.value;
                }
            });

            // Hiển thị lại tất cả các `span` và ẩn tất cả các `input`
            spans.forEach((span) => {
                span.style.display = "inline";
            });

            inputs.forEach((input) => {
                input.style.display = "none";
            });

            document.getElementById("edit-product-category").style.display = "none";

            // Hiển thị nút Chỉnh sửa, Ẩn nút Lưu
            document.getElementById("save-product-btn").style.display = "none";
            document.getElementById("edit-product-btn").style.display = "block";
        });

    //Khi ấn hủy
    document.getElementById("cancel-product-btn").addEventListener("click", function () {
        const detailsContent = document.querySelector(".details-content");
        const spans = detailsContent.querySelectorAll("span");
        const inputs = detailsContent.querySelectorAll("input, textarea");

        // Phục hồi giá trị ban đầu từ thuộc tính `data-original`
        inputs.forEach((input) => {
            const originalValue = input.getAttribute("data-original");
            if (originalValue !== null) {
                input.value = originalValue;
            }
        });

        // Hiển thị lại các phần tử span (view) và ẩn các input (edit)
        spans.forEach((span) => {
            span.style.display = "inline";
        });
        inputs.forEach((input) => {
            input.style.display = "none";
        });

        document.getElementById("edit-product-category").style.display = "none";

        // Hiển thị nút Chỉnh sửa, Ẩn nút Lưu và Hủy
        document.getElementById("save-product-btn").style.display = "none";
        document.getElementById("cancel-product-btn").style.display = "none";
        document.getElementById("edit-product-btn").style.display = "block";
    });
});