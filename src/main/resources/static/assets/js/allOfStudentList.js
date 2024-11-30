
function filterStudents() {
    const majorFilter = document.getElementById('majorFilter').value;
    const yearFilter = document.getElementById('yearFilter').value;
    const searchInput = document.getElementById('searchInput').value.toLowerCase(); // Lấy giá trị tìm kiếm và chuyển thành chữ thường

    const rows = document.querySelectorAll('.student-row');
    rows.forEach(row => {
        const major = row.getAttribute('data-major');
        const year = row.getAttribute('data-year');
        const name = row.querySelector('td:nth-child(2)').innerText.toLowerCase(); // Lấy tên sinh viên từ cột "Họ và Tên"

        let matchMajor = (majorFilter === '' || major === majorFilter);
        let matchYear = (yearFilter === '' || year === yearFilter);
        let matchName = (searchInput === '' || name.includes(searchInput)); // Kiểm tra nếu tên sinh viên khớp với từ khóa tìm kiếm

        if (matchMajor && matchYear && matchName) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

