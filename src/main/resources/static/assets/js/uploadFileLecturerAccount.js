      document.addEventListener("DOMContentLoaded", () => {
        const errorToast = document.getElementById("toastMessageError");
        if (errorToast) {
          errorToast.style.display = "block";
          setTimeout(() => {
            errorToast.style.display = "none";
          }, 5000); // Ẩn toast sau 5 giây
        }
      });
      function cancelUpload() {
        // Clear the file input
        document.getElementById("fileInput").value = "";
        // Optionally, redirect to a different page or hide the upload area
        window.location.href = "/home"; // Example: Redirect to the homepage
      }

      const uploadArea = document.getElementById("uploadArea");
      const fileInput = document.getElementById("fileInput");
      const form = document.getElementById("uploadForm");

      // Highlight the drop area when a file is being dragged over it
      ["dragenter", "dragover"].forEach((eventName) => {
        uploadArea.addEventListener(eventName, (e) => {
          e.preventDefault();
          e.stopPropagation();
          uploadArea.classList.add("dragging");
        });
      });

      ["dragleave", "drop"].forEach((eventName) => {
        uploadArea.addEventListener(eventName, (e) => {
          e.preventDefault();
          e.stopPropagation();
          uploadArea.classList.remove("dragging");
        });
      });

      // Handle the dropped file
      uploadArea.addEventListener("drop", (e) => {
        e.preventDefault();
        e.stopPropagation();
        const files = e.dataTransfer.files;
        if (files.length) {
          fileInput.files = files; // Set the file to the input
          form.submit(); // Automatically submit the form after dropping the file
        }
      });

      // Allow clicking to open the file input
      uploadArea.addEventListener("click", () => fileInput.click());

      // Trigger form submit when a file is selected through the input
      fileInput.addEventListener("change", () => form.submit());

      function submitData() {
        fetch("/submitUploadLecturerAccount", {
          method: "POST",
        })
          .then((response) => response.text())
          .then((data) => {
            // Hiển thị modal khi nhận được phản hồi thành công
            const modal = document.getElementById("successModal");
            modal.style.display = "flex";
          })
          .then(() => {
            // Gửi email sau khi lưu thông tin giảng viên
            fetch("/sendEmailsLecturer", {
              method: "POST",
            }).then(() => {
              // Hiển thị toast sau khi gửi email thành công
              const toast = document.getElementById("toastMessage");
              toast.style.display = "block";

              // Đóng modal
              const modal = document.getElementById("successModal");
              modal.style.display = "none";

              // Ẩn toast và chuyển hướng về trang chủ sau 3 giây
              setTimeout(() => {
                toast.style.display = "none";
                window.location.href = "/home"; // Chuyển hướng về trang chủ
              }, 3000);
            });
          });
      }

      let currentPage = 1;
      const rowsPerPage = 20;

      function displayPage(page) {
        const tableBody = document.getElementById("studentTableBody");
        const rows = tableBody.querySelectorAll("tr");

        // Tính toán trang hiện tại
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        // Ẩn tất cả các hàng
        rows.forEach((row, index) => {
          row.style.display = index >= start && index < end ? "" : "none";
        });

        // Cập nhật chỉ số trang
        document.getElementById("pageIndicator").textContent = page;
      }

      function nextPage() {
        const tableBody = document.getElementById("studentTableBody");
        const rows = tableBody.querySelectorAll("tr");

        if (currentPage * rowsPerPage < rows.length) {
          currentPage++;
          displayPage(currentPage);
        }
      }

      function prevPage() {
        if (currentPage > 1) {
          currentPage--;
          displayPage(currentPage);
        }
      }

      // Khởi tạo hiển thị trang đầu tiên
      document.addEventListener("DOMContentLoaded", () => {
        displayPage(currentPage);
      });
