function showContent(sectionId) {
  const menuAll = document.querySelectorAll(".menu-user li");
  menuAll.forEach((item) => {
    item.classList.remove('active');
  });
  const selectedItem = Array.from(menuAll).find(item => item.getAttribute("data-section") === sectionId);
  if (selectedItem) {
    selectedItem.classList.add("active");
  }

  // Ẩn tất cả các phần nội dung
  document.querySelectorAll(".content_section").forEach((section) => {
    section.style.display = "none";
  });
  // Hiển thị phần nội dung được chọn
  document.getElementById(sectionId).style.display = "block";
}



