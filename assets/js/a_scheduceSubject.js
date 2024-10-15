document.addEventListener('DOMContentLoaded', function() {
    const semesterSelect = document.getElementById('semesterSelect');
    const subjectTableBody = document.getElementById('subjectTableBody');
    const allClasses = subjectTableBody.querySelectorAll('li'); // Lấy tất cả các lớp học
    const createScheduleButton = document.getElementById('createScheduleButton');
    const noClassesMessage = document.getElementById('noClassesMessage'); // Thông báo không có lớp học

    // Đối tượng ánh xạ các ký hiệu học kỳ sang tháng
    const semesterDates = {
        'Fall': { start: { month: 8, day: 1 }, end: { month: 8, day: 23 } }, // Tháng 8
        'Spring': { start: { month: 12, day: 1 }, end: { month: 12, day: 23 } }, // Tháng 12
        'Summer': { start: { month: 10, day: 1 }, end: { month: 10, day: 23 } } // Tháng 4
    };

    // Hàm lọc các lớp học theo kỳ
    function filterClassesBySemester() {
        const selectedSemester = semesterSelect.value;
        const subjectClassCounts = {}; // Biến để lưu số lượng lớp theo subjectID cho từng học kỳ

        // Đặt lại số lượng lớp về 0 cho tất cả các môn học
        allClasses.forEach(classItem => {
            const subjectID = classItem.getAttribute('data-subject');
            if (!subjectClassCounts[subjectID]) {
                subjectClassCounts[subjectID] = 0; // Đặt lại số lượng lớp cho mỗi subjectID
            }
        });

        // Lọc lớp học theo học kỳ đã chọn
        allClasses.forEach(classItem => {
            const classSemester = classItem.getAttribute('data-semester'); // Lấy học kỳ của lớp
            const subjectID = classItem.getAttribute('data-subject'); // Lấy subjectID của lớp

            // Nếu học kỳ hiện tại khớp với học kỳ được chọn
            if (selectedSemester === 'All' || classSemester === selectedSemester) {
                classItem.style.display = ''; // Hiển thị lớp học nếu khớp học kỳ
                subjectClassCounts[subjectID]++; // Tăng số lượng lớp cho môn học này
            } else {
                classItem.style.display = 'none'; // Ẩn lớp học nếu không khớp
            }
        });

        // Cập nhật số lượng lớp học hiển thị cho từng subjectID
        for (const subjectID in subjectClassCounts) {
            const countElement = document.getElementById(`classCount_${subjectID}`);
            if (countElement) {
                countElement.textContent = subjectClassCounts[subjectID]; // Hiển thị số lượng lớp cho học kỳ
            }
        }

        // Kiểm tra nếu không có lớp học nào hiển thị
        const totalClasses = Object.values(subjectClassCounts).reduce((acc, count) => acc + count, 0);
        if (totalClasses === 0) {
            noClassesMessage.style.display = 'block'; // Hiển thị thông báo nếu không có lớp
        } else {
            noClassesMessage.style.display = 'none'; // Ẩn thông báo nếu có lớp
        }

        checkScheduleButtonVisibility(selectedSemester);
    }

    // Hàm để kiểm tra thời gian hiện tại và hiển thị nút "Tạo thời khoá biểu"
    function checkScheduleButtonVisibility(selectedSemester) {
        const currentDate = new Date();
        let startDate, endDate;
        let semesterType = '';
        // Tách ký hiệu học kỳ và năm từ giá trị đã chọn
        if (selectedSemester !== 'All') {
            switch (selectedSemester.slice(0, 2)) {
                case "Fa":
                    semesterType = "Fall";
                    break;
                case "Sp":
                    semesterType = "Spring";
                    break;
                case "Su":
                    semesterType = "Summer";
                    break;
            }

            const year = `20${selectedSemester.slice(-2)}`; // Lấy năm

            // Lấy thông tin tháng và ngày từ đối tượng
            if (semesterDates[semesterType]) {
                startDate = new Date(year, semesterDates[semesterType].start.month - 1, semesterDates[semesterType].start.day);
                endDate = new Date(year, semesterDates[semesterType].end.month - 1, semesterDates[semesterType].end.day);
            }
        }

        // Kiểm tra xem thời gian hiện tại có nằm trong khoảng thời gian quy định không
        if (startDate && endDate && currentDate >= startDate && currentDate <= endDate) {
            createScheduleButton.style.display = 'inline'; // Hiển thị nút
        } else {
            createScheduleButton.style.display = 'none'; // Ẩn nút
        }
    }

    // Lắng nghe sự kiện khi người dùng thay đổi kỳ học
    semesterSelect.addEventListener('change', filterClassesBySemester);

    // Gọi hàm lần đầu tiên để hiển thị chính xác ngay khi trang tải xong
    filterClassesBySemester();
});