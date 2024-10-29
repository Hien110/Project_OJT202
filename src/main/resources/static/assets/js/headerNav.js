document.addEventListener("DOMContentLoaded", function () {
  const menuItems = document.querySelectorAll(".menu-item");

  // Lấy đường dẫn hiện tại
  const currentPath = window.location.pathname;

  menuItems.forEach((item) => {
    // Lấy giá trị href của từng mục
    const href = item.getAttribute("href");

    // Kiểm tra nếu đường dẫn hiện tại khớp với href của mục đó
    if (href === currentPath) {
      item.classList.add("active");
    } else {
      item.classList.remove("active");
    }
  });
});
