document.addEventListener("DOMContentLoaded", function () {
  const menuItems = document.querySelectorAll(".menu-item");
  const currentPath = window.location.pathname;

  // Lấy URL đã lưu trong localStorage (nếu có)
  let lastActivePath = localStorage.getItem("lastActivePath");
  let matchedItem = false;

  // Đặt active theo URL hiện tại hoặc URL đã lưu
  menuItems.forEach((item) => {
    const itemPath = item.getAttribute("href");
    
    if (itemPath === currentPath) {
      item.classList.add("active");
      matchedItem = true; // Đánh dấu đã tìm thấy mục khớp với URL hiện tại
      // Lưu URL hiện tại vào localStorage
      localStorage.setItem("lastActivePath", itemPath);
    } else {
      item.classList.remove("active");
    }
  });

  // Nếu không có mục nào khớp với URL hiện tại, dùng URL đã lưu trong localStorage để giữ trạng thái active
  if (!matchedItem && lastActivePath) {
    menuItems.forEach((item) => {
      const itemPath = item.getAttribute("href");
      if (itemPath === lastActivePath) {
        item.classList.add("active");
      }
    });
  }

  // Lưu lại trạng thái khi người dùng nhấp vào bất kỳ mục navbar nào
  menuItems.forEach((item) => {
    item.addEventListener("click", function () {
      menuItems.forEach((i) => i.classList.remove("active"));
      this.classList.add("active");
      localStorage.setItem("lastActivePath", this.getAttribute("href"));
    });
  });
});
