function filterSubjects() {
    const majorFilter = document.getElementById("majorFilter").value
    const searchInput = document
        .getElementById("searchInput")
        .value.toLowerCase() // Lấy giá trị tìm kiếm và chuyển thành chữ thường

    console.log(majorFilter, searchInput)

    const rows = document.querySelectorAll(".student-row")
    rows.forEach((row) => {
        const major = row.getAttribute("data-major")
        const name = row.getAttribute("data-name").toLowerCase()
        const subjectId = row.getAttribute("data-subjectid").toLowerCase()

        let matchMajor = majorFilter === "" || major === majorFilter
        let matchName = searchInput === "" || name.includes(searchInput)
        let matchSubjectId =
            searchInput === "" || subjectId.includes(searchInput)

        if (matchMajor && (matchName || matchSubjectId)) {
            row.style.display = ""
        } else {
            row.style.display = "none"
        }
    })
}

// Hàm để sắp xếp bảng theo cột 'Kỳ'
function sortTableByTermNo() {
    var table = document.getElementById("studentTable");
    var rows = Array.from(table.getElementsByTagName("tr")).slice(1); // Lấy tất cả các hàng trừ tiêu đề
    rows.sort(function (rowA, rowB) {
      var termA = parseInt(rowA.cells[4].innerText); // Lấy giá trị của cột "Kỳ" (5th column)
      var termB = parseInt(rowB.cells[4].innerText); 
      return termA - termB; // So sánh để sắp xếp theo số tăng dần
    });

    // Sau khi sắp xếp, thêm lại các hàng vào bảng theo thứ tự mới
    rows.forEach(function (row) {
      table.appendChild(row);
    });
  }

  // Gọi hàm sortTableByTermNo khi trang được tải xong
  document.addEventListener("DOMContentLoaded", function () {
    sortTableByTermNo();
  });