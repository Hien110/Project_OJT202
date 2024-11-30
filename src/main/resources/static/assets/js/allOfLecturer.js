function filterLecturers() {
    const majorFilter = document.getElementById('majorFilter').value;
    const yearFilter = document.getElementById('yearFilter').value;
    const searchInput = document.getElementById('searchInput').value.toLowerCase(); // Lấy giá trị tìm kiếm và chuyển thành chữ thường

    console.log(majorFilter, yearFilter, searchInput);

    const rows = document.querySelectorAll('.student-row');
    rows.forEach(row => {
        const major = row.getAttribute('data-major');
        const year = row.getAttribute('data-year');
        const name = row.querySelector('td:nth-child(2)').innerText.toLowerCase(); // Lấy tên từ cột "Họ và Tên"

        let matchMajor = (majorFilter === '' || major === majorFilter);
        let matchYear = (yearFilter === '' || year === yearFilter);
        let matchName = (searchInput === '' || name.includes(searchInput));

        if (matchMajor && matchYear && matchName) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}