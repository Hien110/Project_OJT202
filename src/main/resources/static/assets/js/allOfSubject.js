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