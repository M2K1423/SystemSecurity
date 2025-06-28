document.addEventListener("DOMContentLoaded", async function () {
  const fetchData = async (url) => {
    const response = await fetch(url);
    return response.json();
  };

// Lấy danh sách tỉnh/thành phố
  const provinces = await fetchData("https://provinces.open-api.vn/api/p/");
  const provinceSelect = document.getElementById("province");

// Hiển thị tên tỉnh/thành phố trong value
  provinces.forEach((province) => {
    const option = new Option(province.name, province.name); // Sử dụng name thay vì code
    provinceSelect.appendChild(option);
  });

// Khi chọn tỉnh/thành phố, lấy danh sách quận/huyện
  provinceSelect.addEventListener("change", async function () {
    const provinceName = this.value; // Lấy tên của tỉnh/thành phố
    const districtSelect = document.getElementById("district");
    const wardSelect = document.getElementById("ward");

    districtSelect.innerHTML = '<option value="">Chọn Quận, Huyện</option>';
    wardSelect.innerHTML = '<option value="">Chọn Phường, Xã</option>';

    // Tìm mã tỉnh/thành phố từ tên
    const selectedProvince = provinces.find(
        (province) => province.name === provinceName
    );

    if (selectedProvince) {
      const province = await fetchData(
          `https://provinces.open-api.vn/api/p/${selectedProvince.code}?depth=2`
      );

      province.districts.forEach((district) => {
        const option = new Option(district.name, district.name); // Sử dụng name thay vì code
        districtSelect.appendChild(option);
      });
    }
  });

// Khi chọn quận/huyện, lấy danh sách phường/xã
  document
      .getElementById("district")
      .addEventListener("change", async function () {
        const districtName = this.value; // Lấy tên của quận/huyện
        const wardSelect = document.getElementById("ward");
        wardSelect.innerHTML = '<option value="">Chọn Phường, Xã</option>';

        // Tìm mã quận/huyện từ tên
        const provinceName = provinceSelect.value;
        const selectedProvince = provinces.find(
            (province) => province.name === provinceName
        );

        if (selectedProvince) {
          const province = await fetchData(
              `https://provinces.open-api.vn/api/p/${selectedProvince.code}?depth=2`
          );

          const selectedDistrict = province.districts.find(
              (district) => district.name === districtName
          );

          if (selectedDistrict) {
            const district = await fetchData(
                `https://provinces.open-api.vn/api/d/${selectedDistrict.code}?depth=2`
            );

            district.wards.forEach((ward) => {
              const option = new Option(ward.name, ward.name); // Sử dụng name thay vì code
              wardSelect.appendChild(option);
            });
          }
        }
      });


  //Phần code chính

  // Tìm tất cả các tab và nav-item
  const tabs = document.querySelectorAll('.tab-content');
  const navItems = document.querySelectorAll('.nav-item');

// Hàm cập nhật trạng thái active cho các tab trước và tab hiện tại
  function updateTabProgress(currentHash) {
    let isActive = true; // Điều khiển trạng thái active

    navItems.forEach(item => {
      const tabHash = `#${item.getAttribute('data-tab')}`;

      // Nếu tab hiện tại trùng với tab trong nav-item
      if (tabHash === currentHash) {
        isActive = false; // Dừng kích hoạt các tab sau tab hiện tại
        item.classList.add('active');
        item.classList.add('completed');
      } else if (isActive) {
        // Các tab trước tab hiện tại được kích hoạt
        item.classList.add('active');
        item.classList.add('completed');
      } else {
        // Các tab sau tab hiện tại bị gỡ bỏ active
        item.classList.remove('active');
        item.classList.remove('completed');
      }
    });
  }

// Hàm chuyển tab
  function switchTab(hash) {
    // Ẩn tất cả các tab
    tabs.forEach(tab => {
      tab.style.display = 'none';
    });

    // Hiển thị tab tương ứng nếu tồn tại
    const targetTab = document.querySelector(hash);
    console.log(hash);
    if (targetTab) {
      targetTab.style.display = 'block';
      updateTabProgress(hash); // Cập nhật trạng thái nav-item
    } else {
      console.error(`Tab không tồn tại: ${hash}`);
      // Mặc định quay về tab đầu tiên nếu tab không tồn tại
      const defaultTab = document.querySelector('#cart');
      if (defaultTab) {
        defaultTab.style.display = 'block';
        updateTabProgress('#cart');
      }
    }
  }

// Kiểm tra hash hiện tại khi tải trang
  const currentHash = window.location.hash || '#cart'; // Mặc định là #cart
  switchTab(currentHash);

// Lắng nghe sự kiện hashchange khi người dùng điều hướng
  window.addEventListener('hashchange', () => {
    const newHash = window.location.hash || '#cart';
    switchTab(newHash);
  });


  // Lắng nghe sự kiện submit trên form
  document.getElementById("customer-info-form").addEventListener("submit", function (e) {
    e.preventDefault(); // Ngăn gửi form ngay lập tức

    // Hiển thị loader bằng SweetAlert2
    Swal.fire({
      title: 'Đang xử lý...',
      text: 'Vui lòng chờ trong giây lát.',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    // Gửi form sau khi hiển thị loader
    setTimeout(() => {
      this.submit(); // Gửi form
    }, 500); // Đợi một chút để loader hiển thị
  });

  // Lấy form cần xử lý
  document.getElementById('payment-form').addEventListener('submit', function (event) {
    event.preventDefault(); // Ngăn form gửi đi ngay lập tức

    // Hiển thị loader bằng SweetAlert2
    Swal.fire({
      title: 'Đang xử lý...',
      text: 'Vui lòng chờ trong giây lát.',
      allowOutsideClick: false, // Không cho phép tắt popup khi click ra ngoài
      showConfirmButton: false, // Ẩn nút xác nhận
      didOpen: () => {
        Swal.showLoading(); // Hiển thị hiệu ứng loader
      }
    });

    // Gửi form sau một khoảng thời gian ngắn để loader hiển thị
    setTimeout(() => {
      this.submit(); // Thực sự gửi form
    }, 1000); // Thời gian chờ loader hiển thị (có thể chỉnh sửa)
  });
})

function updateQuantity(button, change) {
  const form = button.closest('form');
  const quantityInput = form.querySelector('input[name="quantity"]');
  const productId = form.querySelector('input[name="productId"]').value;

  // Cập nhật số lượng mới
  const newValue = Math.max(1, parseInt(quantityInput.value, 10) + change);
  quantityInput.value = newValue;

  // Hiển thị loader
  Notiflix.Loading.standard('Đang cập nhật...');

  // Gửi yêu cầu AJAX
  fetch(form.action, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `productId=${productId}&quantity=${newValue}`,
  })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          // Cập nhật tổng tiền trên giao diện
          document.querySelector('.total-price').textContent = `${data.totalPrice.toLocaleString()} VND`;
          const quantityElement = document.querySelector(".quantity-product");
          if (quantityElement && data.totalQuantity) {
            quantityElement.textContent = data.totalQuantity;
          }
        } else {
          Notiflix.Notify.failure('Cập nhật thất bại. Vui lòng thử lại.');
        }
      })
      .catch(() => {
        Notiflix.Notify.failure('Có lỗi xảy ra. Vui lòng thử lại.');
      })
      .finally(() => {
        // Ẩn loader
        Notiflix.Loading.remove();
      });
}

// Tính khoảng cách giữa các nav-item
function updateAfterWidth() {
  const items = document.querySelectorAll('.nav-item');
  //lấy 2 nav-item đầu tiên nếu có
  if (items.length >= 2) {
    const rect1 = items[0].getBoundingClientRect();
    const rect2 = items[1].getBoundingClientRect();
    // tính khoảng cách từ cạnh trái nav-item2 đến cạnh phải nav-item1
    const width = rect2.left - rect1.right;

    // set lại css
    const style = document.createElement('style');
    style.innerHTML = `
      .nav-item:not(:first-child)::after {
        width: ${width}px !important;
      }
    `;
    document.head.appendChild(style);
  }
}

window.addEventListener('DOMContentLoaded', () => {
  // Delay 1 animation frame để đảm bảo layout ổn định
  requestAnimationFrame(() => {
    updateAfterWidth();
  });
});

// tính khoảng cách giữa các nav-item sau khi load
window.addEventListener('load', () => {
  setTimeout(() => {
    updateAfterWidth();
  }, 50); // chờ thêm 50ms sau khi load xong
});

// tính khoảng cách giữa các nav-item sau khi resize
window.addEventListener('resize', updateAfterWidth);




