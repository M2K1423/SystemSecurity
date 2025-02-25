
// Hàm hiển thị/ẩn input cho thống kê theo tháng
function showDateInputs(value) {
    const monthInputGroup = document.getElementById("month-input-group");
    monthInputGroup.style.display = value === "monthly" ? "block" : "none";
}

// Thêm sự kiện khi thay đổi loại thống kê
const statisticTypeElement = document.getElementById("statistic-type");
statisticTypeElement.addEventListener("change", function () {
    const value = this.value;
    showDateInputs(value);

    // Reset placeholder và giá trị input khi thay đổi loại thống kê
    const monthInput = document.getElementById("month");
    if (value === "monthly") {
        monthInput.placeholder = "Nhập năm";
    } else {
        monthInput.placeholder = "";
        monthInput.value = "";
    }
});

// ============================== PHẦN CODE BIỂU ĐỒ ==================================================
let ctx;

// Khởi tạo ngữ cảnh biểu đồ
function initializeChartContext() {
    const canvas = document.getElementById("revenue-chart");
    if (canvas) {
        ctx = canvas.getContext("2d");
    } else {
        console.error("Canvas element with id 'revenue-chart' not found.");
    }
}

//======================= CODE BIỂU ĐỒ THÔỐNG KÊ THEO THÁNG=========================
// Gửi yêu cầu AJAX để lấy dữ liệu thống kê theo tháng
async function fetchMonthlyRevenueData(year) {
    const url = `/WebBongDen_war/revenue?statisticType=monthly&year=${year}`;
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error("Không thể lấy dữ liệu từ server.");
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Lỗi khi lấy dữ liệu thống kê.");
    }
}

// Gửi yêu cầu AJAX để lấy dữ liệu thống kê theo năm
async function fetchYearlyRevenueData() {
    const url = `/WebBongDen_war/revenue?statisticType=yearly`;
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error("Không thể lấy dữ liệu từ server.");
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Lỗi khi lấy dữ liệu thống kê.");
    }
}


// Xử lý khi nhấn nút "Xem Thống Kê"
async function processInput() {
    const statisticType = document.getElementById("statistic-type").value; // Loại thống kê
    const year = document.getElementById("month").value.trim(); // Lấy giá trị năm

    if (statisticType === "monthly") {
        // Kiểm tra điều kiện nhập cho thống kê theo tháng
        if (!year || isNaN(year) || year.length !== 4) {
            alert("Vui lòng nhập năm hợp lệ (4 chữ số).");
            return;
        }

        // Lấy dữ liệu thống kê theo tháng
        const revenueData = await fetchMonthlyRevenueData(year);

        // Chuyển đổi dữ liệu JSON thành định dạng cho Chart.js
        const chartData = transformMonthlyDataForChart(revenueData, year);

        // Tạo hoặc cập nhật biểu đồ
        generateChart(chartData, "monthly");
    } else if (statisticType === "yearly") {
        // Lấy dữ liệu thống kê theo năm
        const revenueData = await fetchYearlyRevenueData();

        // Chuyển đổi dữ liệu JSON thành định dạng cho Chart.js
        const chartData = transformYearlyDataForChart(revenueData);

        // Tạo hoặc cập nhật biểu đồ
        generateChart(chartData, "yearly");
    }
}

// Chuyển đổi dữ liệu từ server thành định dạng cho Chart.js
function transformMonthlyDataForChart(data, year) {
    const labels = [
        "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
        "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
    ];
    const dataset = Array(12).fill(0);
    data.forEach(row => {
        const month = row.month - 1; // Tháng trong SQL bắt đầu từ 1
        dataset[month] = row.revenue;
    });
    return {
        labels,
        datasets: [
            {
                label: `Doanh thu năm ${year}`,
                data: dataset,
                borderColor: "rgba(75, 192, 192, 1)",
                backgroundColor: "rgba(75, 192, 192, 0.2)",
                fill: true
            }
        ]
    };
}

// Chuyển đổi dữ liệu từ server thành định dạng cho Chart.js (thống kê theo năm)
function transformYearlyDataForChart(data) {
    const labels = data.map(row => row.year); // Các năm
    const dataset = data.map(row => row.revenue); // Doanh thu tương ứng

    return {
        labels,
        datasets: [
            {
                label: "Doanh thu theo năm",
                data: dataset,
                borderColor: "rgba(54, 162, 235, 1)",
                backgroundColor: "rgba(54, 162, 235, 0.2)",
                fill: true
            }
        ]
    };
}


// Tạo biểu đồ với Chart.js
function generateChart(data, statisticType) {
    if (!ctx) {
        console.error("Canvas context is not initialized.");
        return;
    }

    // Hủy biểu đồ cũ nếu đã tồn tại
    if (window.myChart) {
        window.myChart.destroy();
    }

    // Tạo biểu đồ mới
    window.myChart = new Chart(ctx, {
        type: "line",
        data: data,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true
                },
                tooltip: {
                    enabled: true
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: statisticType === "monthly" ? "Tháng" : "Năm"
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: "Doanh thu (VND)"
                    }
                }
            }
        }
    });
}

// Thêm sự kiện cho nút "Xem Thống Kê"
document.getElementById("submit-btn").addEventListener("click", processInput);

// Khởi tạo ngữ cảnh của biểu đồ khi tài liệu đã tải xong
initializeChartContext();



