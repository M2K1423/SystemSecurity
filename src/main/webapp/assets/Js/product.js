document.addEventListener('DOMContentLoaded', function() {
    $(document).ready(function(){
        $('.product-image-list').slick({
          infinite: true,
          slidesToShow: 1,
          prevArrow: `<button type='button' class='slick-prev slick-arrow'><i class="fa-solid fa-angle-left"></i></button>`,
          nextArrow: `<button type='button' class='slick-next slick-arrow'><i class="fa-solid fa-angle-right"></i></button>`,
        });
    });

    // Code phần rating
  const ratingItems = document.querySelectorAll(".rating-item");
  const selectedRatingDisplay = document.querySelector(".selected-rating");

  // Xử lý sự kiện hover
  ratingItems.forEach((item, index) => {
    item.addEventListener("mouseover", function() {
      // Đổi màu các sao trước sao được hover
      for (let i = 0; i <= index; i++) {
        ratingItems[i].classList.add("selected");
      }
      for (let i = index + 1; i < ratingItems.length; i++) {
        ratingItems[i].classList.remove("selected");
      }

    });

    // Xử lý sự kiện click
    item.addEventListener("click", function() {
      const ratingValue = index + 1;
      console.log("Điểm đánh giá: " + ratingValue);
      for (let i = 0; i <= index; i++) {
        ratingItems[i].classList.add("selected");
      }
    });
  });

  function toggleCategoryMenu() {
    // Lấy tất cả các danh mục chính
    const categoryItems = document.querySelectorAll(".cate-item");

    categoryItems.forEach((category) => {
      category.addEventListener("click", function (event) {
        event.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>

        // Tìm danh mục con liên quan
        const subcategoryList = this.parentElement.querySelector(".subcategory-list");
        console.log(subcategoryList)
        if (subcategoryList) {
          // Kiểm tra trạng thái hiển thị
          const isVisible = subcategoryList.style.height === "auto";

          // Ẩn/hiện danh mục con
          subcategoryList.style.height = isVisible ? "0" : "auto";

          // Thay đổi icon trạng thái
          const icon = this.querySelector("i");
          if (icon) {
            icon.classList.toggle("active");
          }
        }
      });
    });
  }

// Gọi hàm sau khi trang đã được load
  toggleCategoryMenu()



    const buyBtn = document.getElementById("addToCart");

    if (buyBtn) { // Kiểm tra nếu nút tồn tại
      buyBtn.addEventListener('click', function () {
        // Lấy productId từ thuộc tính data-id
        const productId = parseInt(buyBtn.dataset.id, 10); // Sử dụng parseInt (cơ số 10)

        // Gửi request đến server
        fetch('/WebBongDen_war/add-to-cart', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: `productId=${productId}`,
        })
            .then(response => {
              if (response.ok) {
                return response.json(); // Parse JSON response
              } else {
                console.log(response)
                throw new Error('Failed to add product to cart');
              }
            })
            .then(data => {
              if (data.status === 'success') {
                // Hiển thị thông báo thành công bằng SweetAlert
                Swal.fire({
                  title: 'Thành công!',
                  text: 'Sản phẩm đã được thêm vào giỏ hàng!',
                  icon: 'success',
                  timer: 1500,
                  showConfirmButton: false
                });

                // Cập nhật số lượng sản phẩm trong giỏ hàng
                const quantityElement = document.querySelector(".quantity-product");
                if (quantityElement && data.cartQuantity) {
                  quantityElement.textContent = data.cartQuantity;
                }
              } else {
                // Hiển thị lỗi từ server
                Swal.fire({
                  title: 'Lỗi!',
                  text: data.message || 'Không thể thêm sản phẩm vào giỏ hàng.',
                  icon: 'error',
                  confirmButtonText: 'OK'
                });
              }
            })
            .catch(error => {
              console.error('Error:', error);
              Swal.fire({
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng.',
                icon: 'error',
                confirmButtonText: 'OK'
              });
            });
      });
    } else {
      console.error("Nút 'addToCart' không tồn tại trong DOM.");
    }

  document.querySelector(".submit-review-btn").addEventListener("click", function (event) {
    event.preventDefault(); // Ngăn chặn form submit mặc định

    // Lấy dữ liệu từ form
    const productId = document.getElementById("product-id").value;
    const accountId = document.getElementById("account-id").value;
    const content = document.getElementById("comment-content").value;
    const rating = document.getElementById("rating").value;

    // Kiểm tra dữ liệu
    if (!content.trim()) {
      Swal.fire("Lỗi", "Vui lòng nhập nội dung bình luận.", "error");
      return;
    }

    // Tạo object để gửi dưới dạng JSON
    const reviewData = {
      productId: parseInt(productId),
      accountId: parseInt(accountId),
      content: content,
      rating: parseInt(rating),
    };

    // Gửi dữ liệu qua Fetch
    fetch("/WebBongDen_war/review", {
      method: "POST",
      headers: {
        "Content-Type": "application/json", // Đặt header là JSON
      },
      body: JSON.stringify(reviewData), // Chuyển dữ liệu sang JSON
    })
        .then((response) => response.json())
        .then((data) => {
          if (data.success) {
            Swal.fire("Thành công", "Bình luận của bạn đã được gửi!", "success").then(() => {
              location.reload(); // Reload lại trang
            });
          } else {
            Swal.fire("Lỗi", data.message || "Không thể gửi bình luận.", "error");
          }
        })
        .catch(() => {
          Swal.fire("Lỗi", "Không thể gửi bình luận.", "error");
        });
  });
});
