document.addEventListener("DOMContentLoaded", function () {
    console.log('Page loaded');

    function toggleCategoryMenu() {
        const urlParams = new URLSearchParams(window.location.search);
        const currentSubCategoryId = urlParams.get('subCategoryId'); // Lấy subcategoryId từ URL
        console.log(currentSubCategoryId)

        // Lấy tất cả các danh mục chính
        const categoryItems = document.querySelectorAll(".cate-item");

        categoryItems.forEach((category) => {
            // Thêm sự kiện click cho từng danh mục
            category.addEventListener("click", function (event) {
                event.preventDefault(); // Ngăn hành vi mặc định

                const subcategoryList = this.parentElement.querySelector(".subcategory-list");
                if (subcategoryList) {
                    const isVisible = subcategoryList.style.height === "auto";
                    subcategoryList.style.height = isVisible ? "0" : "auto";

                    const icon = this.querySelector("i");
                    if (icon) {
                        icon.classList.toggle("active");
                    }
                }
            });

            // Tự động mở rộng danh mục cha và tô đậm danh mục con nếu subcategoryId khớp
            const subcategoryList = category.parentElement.querySelector(".subcategory-list");
            if (subcategoryList) {
                const subCategoryItems = subcategoryList.querySelectorAll("a");
                subCategoryItems.forEach((subCategory) => {
                    if (subCategory.dataset.id === currentSubCategoryId) {
                        // Mở rộng danh mục cha
                        subcategoryList.style.height = "auto";

                        // Tô đậm danh mục con
                        subCategory.classList.add("active");

                        // Thêm icon trạng thái
                        const icon = category.querySelector("i");
                        if (icon) {
                            icon.classList.add("active");
                        }
                    }
                });
            }
        });
    }

    toggleCategoryMenu();

    // Kiểm tra sự kiện 'prev'
    const prevButton = document.getElementById('prev');
    if (prevButton) {
        prevButton.addEventListener("click", function () {
            console.log('Previous button clicked');
        });
    } else {
        console.log("Prev button not found");
    }
});
