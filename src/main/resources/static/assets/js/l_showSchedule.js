document.addEventListener("DOMContentLoaded", function () {
    const semesterSelect = document.getElementById("semester");
    const today = new Date(); 
    const day = today.getDate(); 
    const month = today.getMonth() + 1; 
    const year = today.getFullYear(); 
    const yearSemmester = year%2000;
  
    if(month  >= 1 && month <= 4) {
      semesterSelect.value = "Spring" + yearSemmester;
    } else  if(month >= 5 && month <= 8) {
      semesterSelect.value = "Summer" + yearSemmester;
    }  else {
      semesterSelect.value = "Fall" + yearSemmester;
    }
  
    updateWeeks(); // Gọi để cập nhật các tuần của kỳ Fall23
    // Lắng nghe sự kiện thay đổi kỳ học
    semesterSelect.addEventListener("change", function () {
      updateWeeks(); // Tự động cập nhật các tuần khi thay đổi kỳ học
    });
  
    const weekSelect = document.getElementById("week");
  
    // Lắng nghe sự kiện thay đổi tuần
    weekSelect.addEventListener("change", updateCalendarDates);
  });
  
  // Khai báo biến toàn cục để giữ classCountMap
  
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
      startMonth = 9;
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
    
    const dateSelect = document.getElementById("week");
    const today = new Date(); 
    let day = today.getDate(); 
    if (day < 10){
      day = '0' + day;
    }
    let month = today.getMonth() + 1; 
    if (month < 10){
      month = '0' + month;
    }
    const yearCurrent = today.getFullYear(); 
    const currentDate = `${yearCurrent}-${month}-${day}`
  
    let indexWeek = 0;
    let checkIndex = 0;
    weeks.forEach((week) => {
    const startWeek = year + '-' + week.split("-")[0].split("/")[1].trim() + '-' +  week.split("-")[0].split("/")[0];
    const endWeek = year + '-' + week.split("-")[1].split("/")[1].trim() + '-' +  week.split("-")[1].split("/")[0].trim() ;
    indexWeek += 1;
    if (currentDate >= startWeek && currentDate <= endWeek){
        checkIndex = indexWeek-1;
    };
  
      const option = document.createElement("option");
      option.text = week;
      weekSelect.add(option);
    });
    // Thiết lập tuần đầu tiên mặc định được chọn sau khi cập nhật
    weekSelect.value = weekSelect.options[checkIndex].value;
  
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
   
    updateSchedule(calendarDates);
  }
  
  function updateSchedule() {
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
            const scheduleItems = cell.querySelectorAll("ul ul li");
            
            // Lặp qua từng mục lịch trình trong ô
            scheduleItems.forEach((item) => {
              const timeScheduceElement = item.querySelector(".scheduceTime");
              const classOfSemester = item.querySelector("#className");
              const value1 = classOfSemester.innerText;
              const key = value1.split("_")[0].trim() + "-" + value1.split("_")[1].trim();
                
              if (timeScheduceElement) {
                const timeScheduceText = timeScheduceElement.innerText
                  .split(" - ")[0]
                  .trim(); // Lấy thời gian (Slot) từ lịch trình
                const dateScheduceText = timeScheduceElement.innerText
                  .split(" - ")[1]
                  .trim(); // Lấy ngày từ lịch trình
                const dateScheduceText1 = '20' + dateScheduceText.split("/")[2] + '-' + dateScheduceText.split("/")[1] + '-' + dateScheduceText.split("/")[0]
                
                const isSlotMatched = timeScheduceText === slotName;
                const isDateMatched = dateScheduceText1 === headerFormatted;
                
                const today = new Date(); 
                let day = today.getDate(); 
                if  (day < 10) {
                  day = '0' + day; 
                }
                let month = today.getMonth() + 1; 
                if (month < 10) {
                  month = '0' + month;
                  }
                const year = today.getFullYear(); 
                const currentDate = `${year}-${month}-${day}`;
                // Hiển thị nếu cả slot và ngày đều khớp
                if (isSlotMatched && isDateMatched) {
                  item.style.display = "block"; // Hiển thị mục lịch trình
                  if (dateScheduceText1 < currentDate){
                    item.style.color = "black";
                    item.style.backgroundColor = "#c4c4c4";
                  } else if (dateScheduceText1 == currentDate){
                    item.style.color = "#ffae00";
                    item.style.backgroundColor = "#fff58d";
                  } else {
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
  
  
  