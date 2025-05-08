document.addEventListener("DOMContentLoaded", function () {
    // Lấy dữ liệu JSON từ thuộc tính data-chart
    const customerChartElement = document.querySelector('.customer-chart');
    const userChartData = JSON.parse(customerChartElement.dataset.chart);

    console.log(userChartData); // Kiểm tra dữ liệu trong Console

    // Tách nhãn (labels) và giá trị (data)
    const labels1 = Object.keys(userChartData);
    const data1 = Object.values(userChartData);

    // Cấu hình và tạo biểu đồ
    const ctx1 = document.getElementById('customerPieChart').getContext('2d');
    new Chart(ctx1, {
        type: 'pie',
        data: {
            labels: labels1,
            datasets: [{
                label: 'Phân loại khách hàng',
                data: data1,
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                }
            }
        }
    });

    // Lấy dữ liệu từ thuộc tính data-revenue
    const revenueChartElement = document.querySelector('.card-container2');
    const revenueChartData = JSON.parse(revenueChartElement.dataset.revenue); // Chuyển JSON string thành đối tượng

    console.log(revenueChartData); // Kiểm tra dữ liệu trong Console

    // Tách nhãn (labels) và giá trị (data)
    const labels = Object.keys(revenueChartData); // Các khoảng thời gian
    const data = Object.values(revenueChartData); // Doanh thu tương ứng

    // Tạo biểu đồ đường
    const ctx = document.getElementById('revenueChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu (VND)',
                data: data,
                borderColor: '#36A2EB',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                tension: 0.4, // Làm mượt các góc của đường
                fill: true // Đổ màu dưới đường
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Khoảng thời gian'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Doanh thu (VND)'
                    }
                }
            }
        }
    });
});
