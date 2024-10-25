document.addEventListener("DOMContentLoaded", function () {
  const semesterSelect = document.getElementById("semester");
  // Mặc định thiết lập kỳ là Fall23 khi trang tải
  semesterSelect.value = "Fall23";
  updateWeeks(); // Gọi để cập nhật các tuần của kỳ Fall23
  updateClassCounts(); // Cập nhật số lượng lớp khi thay đổi kỳ học

  // Lắng nghe sự kiện thay đổi kỳ học
  semesterSelect.addEventListener("change", function () {
    updateWeeks(); // Tự động cập nhật các tuần khi thay đổi kỳ học
    updateClassCounts(); // Cập nhật số lượng lớp khi thay đổi kỳ học
  });

  const weekSelect = document.getElementById("week");

  // Lắng nghe sự kiện thay đổi tuần
  weekSelect.addEventListener("change", updateCalendarDates);
});

// Khai báo biến toàn cục để giữ classCountMap
const classCountMap = new Map();

function updateWeeks() {
  const semesterSelect = document.getElementById("semester");
  const weekSelect = document.getElementById("week");

  weekSelect.innerHTML = ""; // Xóa các option trước khi thêm mới

  const selectedSemester = semesterSelect.value;
  const season = selectedSemester.slice(0, -2); // Mùa (Fall, Spring, Summer)
  const year = "20" + selectedSemester.slice(-2); // Năm (2023, 2024)

  let startMonth, endMonth;

  if (season === "Spring") {
    startMonth = 1;
    endMonth = 4;
  } else if (season === "Summer") {
    startMonth = 5;
    endMonth = 8;
  } else if (season === "Fall") {
    startMonth = 8;
    endMonth = 12; // Fall chỉ đến hết tháng 12
  }

  function getFirstMonday(year, month) {
    let date = new Date(year, month - 1, 1); // Ngày đầu tiên của tháng
    while (date.getDay() !== 1) {
      date.setDate(date.getDate() + 1); // Tìm thứ Hai đầu tiên
    }
    return date;
  }

  function getWeeks(startMonth, endMonth, year) {
    let weeks = [];
    let currentDate = getFirstMonday(year, startMonth);
    const lastDayOfYear = new Date(year, 11, 31);

    while (
      currentDate <= lastDayOfYear &&
      currentDate.getMonth() + 1 <= endMonth
    ) {
      let startWeek = new Date(currentDate);
      let endWeek = new Date(currentDate);
      endWeek.setDate(endWeek.getDate() + 6);

      if (endWeek > lastDayOfYear) {
        endWeek = lastDayOfYear;
      }

      const startWeekStr = startWeek.toLocaleDateString("en-GB", {
        day: "2-digit",
        month: "2-digit",
      });
      const endWeekStr = endWeek.toLocaleDateString("en-GB", {
        day: "2-digit",
        month: "2-digit",
      });
      weeks.push(`${startWeekStr} - ${endWeekStr}`);
      
      currentDate.setDate(currentDate.getDate() + 7);
    }
    return weeks;
  }

  const weeks = getWeeks(startMonth, endMonth, year);

  weeks.forEach((week) => {
    const option = document.createElement("option");
    option.text = week;
    weekSelect.add(option);
  });

  // Thiết lập tuần đầu tiên mặc định được chọn sau khi cập nhật
  weekSelect.value = weekSelect.options[0].value;

  // Gọi để cập nhật lịch dựa trên tuần đầu tiên
  updateCalendarDates();
}

function updateCalendarDates() {
  const weekSelect = document.getElementById("week");
  const selectedWeek = weekSelect.value;

  if (!selectedWeek || selectedWeek === "Week") return;

  const [startDate, endDate] = selectedWeek.split(" - ").map((date) => {
    const [day, month] = date.split("/");
    // Lấy năm từ kỳ học đã chọn
    const semesterSelect = document.getElementById("semester");
    const selectedSemester = semesterSelect.value;
    const year = "20" + selectedSemester.slice(-2); // Năm (2023, 2024)

    return new Date(year, parseInt(month) - 1, parseInt(day)); // Sử dụng năm đúng
  });

  const calendarHeaders = document.querySelectorAll(".calendar thead th");
  let currentDate = new Date(startDate); // Đảm bảo khởi tạo currentDate đúng
  const dateScheduceText = ["2023-10-18", "2023-10-19", "2023-10-20"]; // Ví dụ dữ liệu từ DB

  // Tạo một mảng để lưu trữ các ngày từ lịch
  const calendarDates = [];

  calendarHeaders.forEach((header, index) => {
    if (index > 0 && currentDate <= endDate) {
      const day = currentDate.getDate().toString().padStart(2, "0");
      const month = (currentDate.getMonth() + 1).toString().padStart(2, "0");
      const year = currentDate.getFullYear();
      header.innerHTML = `Thứ ${index +1}<br />${day}/${month}`;

      // Lưu giá trị vào mảng calendarDates
      calendarDates.push(`${year}-${month}-${day}`);

      // Cập nhật currentDate cho lần lặp tiếp theo
      currentDate.setDate(currentDate.getDate() + 1);
    }
  });
  const today = new Date(); 
  const day = today.getDate(); 
  const month = today.getMonth() + 1; 
  const year = today.getFullYear(); 
  const todayObj = `${year}-${month}-${day}`;
  // In ra console để kiểm tra

  const startDateOjt = new Date(startDate);
  const endDateOjt = new Date(endDate);

// Lấy các giá trị năm, tháng, ngày
const yearStart = startDateOjt.getFullYear();
const monthStart = String(startDateOjt.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
const dayStart = String(startDateOjt.getDate()).padStart(2, '0');
const formattedDateStart = `${yearStart}-${monthStart}-${dayStart}`;  


const yearEnd = endDateOjt.getFullYear();
const monthEnd = String(endDateOjt.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
const dayEnd = String(endDateOjt.getDate()).padStart(2, '0');
const formattedDateEnd = `${yearEnd}-${monthEnd}-${dayEnd-2}`;

const saveButton = document.querySelector("#saveButton");
console.log(formattedDateStart <=  todayObj && formattedDateEnd >= todayObj);

if ( formattedDateEnd >= todayObj) {
  saveButton.style.display = "block"
} else {
  saveButton.style.display = "none"
}

  updateSchedule(calendarDates);
}

function updateSchedule(calendarDates) {
  const slotRows = document.querySelectorAll("tbody tr");
  const semesterSelect = document.getElementById("semester");
  const selectedSemester = semesterSelect.value;
  const year = "20" + selectedSemester.slice(-2); // Năm (2023, 2024)

  slotRows.forEach((row) => {
    const slotName = row.querySelector(".tableSlot h5").innerText.trim(); // Lấy tên slot (ví dụ: "Slot 1", "Slot 2", ...)

    const scheduleCells = row.querySelectorAll("td");
    const headerCells = document.querySelectorAll(".calendar thead th");
    headerCells.forEach((header, colIndex) => {
      if (colIndex > 0) {
        // Bỏ qua cột đầu (thường là cột không chứa ngày)
        const headerText = header.innerText.split("\n")[1].trim(); // Lấy giá trị ngày từ tiêu đề cột (bỏ qua "Thứ")
        const [day, month] = headerText.split("/").map(Number); // Tách ngày/tháng
        const headerDate = new Date(year, month - 1, day);
        const headerFormatted = `${headerDate.getFullYear()}-${(
          headerDate.getMonth() + 1
        )
          .toString()
          .padStart(2, "0")}-${headerDate
          .getDate()
          .toString()
          .padStart(2, "0")}`;

        const cell = scheduleCells[colIndex];

        if (cell) {
          const scheduleItems = cell.querySelectorAll("ul li ul li");

          // Lặp qua từng mục lịch trình trong ô
          scheduleItems.forEach((item) => {
            const timeScheduceElement = item.querySelector(".scheduceTime");
            const classOfSemester = item.querySelector("#className");
            const value1 = classOfSemester.innerText;
            const key =
              value1.split("_")[0].trim() + "-" + value1.split("_")[1].trim();

            // Cập nhật classCountMap
            if (!classCountMap.has(key)) {
              classCountMap.set(key, []);
            }
            if (!classCountMap.get(key).includes(value1)) {
              classCountMap.get(key).push(value1);
            }

            if (timeScheduceElement) {
              const timeScheduceText = timeScheduceElement.innerText
                .split(" - ")[0]
                .trim(); // Lấy thời gian (Slot) từ lịch trình
              const dateScheduceText = timeScheduceElement.innerText
                .split(" - ")[1]
                .trim(); // Lấy ngày từ lịch trình

              const isSlotMatched = timeScheduceText === slotName;
              const isDateMatched = dateScheduceText === headerFormatted;

              const dateScheduce = item.querySelector(".scheduceTime").innerText.split(" - ")[1].trim()
              const today = new Date(); 
              const day = today.getDate(); 
              const month = today.getMonth() + 1; 
              const year = today.getFullYear(); 
              const currentDate = `${year}-${month}-${day}`;
              // Hiển thị nếu cả slot và ngày đều khớp
              if (isSlotMatched && isDateMatched) {
                item.style.display = "block"; // Hiển thị mục lịch trình
                const optionLecture = item.querySelectorAll("#optionLecture");
                const optionRoom = item.querySelectorAll("#optionRoom");
                const lectureData = item.querySelectorAll("#lectureData");
                const roomData = item.querySelectorAll("#roomData");
                
                if (dateScheduce <= currentDate){
                  optionLecture.forEach((option) => {
                    option.style.display = "none";
                  })
                  optionRoom.forEach((option) => {
                    option.style.display = "none";
                  })
                  lectureData.forEach((option) => {
                    option.style.display = "block";
                  })
                  roomData.forEach((option) => {
                    option.style.display = "block";
                  })
                }  else {
                  optionLecture.forEach((option) => {
                    option.style.display = "block";
                  })
                  optionRoom.forEach((option) => {
                    option.style.display = "block";
                  })
                  lectureData.forEach((option) => {
                    option.style.display = "none";
                  })
                  roomData.forEach((option) => {
                    option.style.display = "none";
                  })
                }
              } else {
                item.style.display = "none"; // Ẩn mục lịch trình
              }
            }
          });
        }
      }
    });
  });
}

// Hàm cập nhật số lượng lớp
function updateClassCounts() {
  const selectedSemester_S = document.getElementById("semester").value; // Lấy giá trị kỳ học đã chọn
  const subjects = document.querySelectorAll(".filter-box");
  subjects.forEach((subject) => {
    const classCountInput = subject.querySelector(".classCountInput");
    classCountInput.value = 0;
  });
  classCountMap.forEach((value, key) => {
    const [className, semester] = key.split("-"); // Tách key thành tên lớp và kỳ học
    const subjects = document.querySelectorAll(".filter-box");
    let selectedSemester;

    switch (selectedSemester_S.slice(0, -2)) {
      case "Fall":
        selectedSemester = "FA" + selectedSemester_S.slice(-2); // FA23 -> Fa23
        break;
      case "Summer":
        selectedSemester = "Su" + selectedSemester_S.slice(-2); // Summer -> Su
        break;
      case "Spring":
        selectedSemester = "SP" + selectedSemester_S.slice(-2); // Spring -> Sp
        break;
    }

    if (semester === selectedSemester) {
      subjects.forEach((subject) => {
        const subjectID = subject.querySelector(".subjectID").value.trim(); // Lấy mã môn học

        if (subjectID === className) {
          const classCountInput = subject.querySelector(".classCountInput");
          classCountInput.value = value.length; // Cập nhật số lượng lớp nếu có
        }
      });
    }
  });

  // Đặt giá trị bằng 0 cho những môn không có lớp
  subjects.forEach((subject) => {
    const classCountInput = subject.querySelector(".classCountInput");
    if (!classCountInput.value || classCountInput.value === "") {
      classCountInput.value = 0; // Nếu không có giá trị, đặt bằng 0
    }
  });
}
