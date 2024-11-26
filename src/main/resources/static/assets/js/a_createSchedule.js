document.addEventListener("DOMContentLoaded", function () {
  const semesterSelect = document.getElementById("semester");
  const today = new Date();
  const month = today.getMonth() + 1;
  const year = today.getFullYear();
  let yearSemmester = year % 2000;

  if (month >= 4 && month <= 5) {
    semesterSelect.value = "Summer " + yearSemmester;
  } else if (month >= 8 && month <= 9) {
    semesterSelect.value = "Fall " + yearSemmester;
  } else if (month >= 11) {
    let nextYear = yearSemmester + 1;
    semesterSelect.value = "Spring " + nextYear;
  }

  
  updateWeeks();
  createSchedule();
  getRoomUniClass();
  chooseWeekStart();
  chooseWeekEnd();
});
let mapWeekLearn = new Map();
mapWeekLearn.set("weekStart", null);
mapWeekLearn.set("weekEnd", null);
let mapSchedule = new Map();
function createSchedule(){
  const classDiv = document.querySelectorAll(
    ".ClassMap .classValue div[data-slot='checkSlot'][data-day='checkDay']"
  );
  let mapUniClass = new Map();

  classDiv.forEach((div) => {
    mapUniClass.set(div.textContent, div.textContent.split("-")[1]);
  });

  mapUniClass.forEach((value, key) => {
    let numberSlot = 1;
    let dataSlot = "Slot " + numberSlot;
    const daysOfWeek = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    let dayCheck1 = 0;
    let dayCheck2 = 3;
    let dataDay1 = daysOfWeek[dayCheck1];
    let dataDay2 = daysOfWeek[dayCheck2];
    mapUniClass.forEach((value1, key1) => {
      if (value === value1) {
        const uniClassName = key1.split("-")[0];
        const uniClasLecture = value1;
        const targetCell = document.querySelector(
          `.calendar tbody tr[data-slot='${dataSlot}'] td[data-day='${dataDay1}']`
        );
        const targetCell1 = document.querySelector(
          `.calendar tbody tr[data-slot='${dataSlot}'] td[data-day='${dataDay2}']`
        );

        let keySchedule;
        let valueSlot;
        if (targetCell) {
          keySchedule = key1.split("-")[2] + "-" + dataSlot + "-" + dayCheck1;
          valueSlot = null;
          mapSchedule.set(keySchedule, valueSlot);
          const containerDiv = document.createElement("div");
          containerDiv.className = "event"; // Thêm class nếu cần

          const uniClassNameDiv = document.createElement("div");
          const uniClasLectureDiv = document.createElement("div");
          const uniClassIdInput = document.createElement("input");
          uniClassIdInput.className = "uniClassIdInput";
          uniClassIdInput.type = "hidden";
          uniClassIdInput.value = keySchedule;
          uniClassNameDiv.textContent = uniClassName; // Gán nội dung lớp học vào
          uniClasLectureDiv.textContent = uniClasLecture; // Gán nội dung giáo viên vào

          const roomSelect = document.createElement("select");
          roomSelect.className = "optionRoom form-control";

          const roomOptions = ["Chọn phòng",
            "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", 
            "211", "212", "213", "214", "215", "301", "302", "303", "304", "305", 
            "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", 
            "401", "402", "403", "404", "405", "406", "407", "408", "409", "410", 
            "411", "412", "413", "414", "415", "501", "502", "503", "504", "505", 
            "506", "507", "508", "509", "510", "511", "512", "513", "514", "515"
          ];
          roomOptions.forEach((room) => {
            const option = document.createElement("option");
            if (room === "Chọn phòng") {
              option.value = null;
              option.textContent = room;
              roomSelect.appendChild(option);
            } else {
              option.value = room;
              option.textContent = "Phòng " + room;
              roomSelect.appendChild(option);
            }
          });

          containerDiv.appendChild(uniClassNameDiv);
          containerDiv.appendChild(uniClasLectureDiv);
          containerDiv.appendChild(roomSelect); // Thêm select vào containerDiv
          containerDiv.appendChild(uniClassIdInput);
          targetCell.appendChild(containerDiv);
        }
        if (targetCell1) {
          keySchedule = key1.split("-")[2] + "-" + dataSlot + "-" + dayCheck2;
          valueSlot = null;
          mapSchedule.set(keySchedule, valueSlot);
          const containerDiv = document.createElement("div");
          containerDiv.className = "event"; // Thêm class nếu cần

          const uniClassNameDiv = document.createElement("div");
          const uniClasLectureDiv = document.createElement("div");
          const uniClassIdInput = document.createElement("input");
          uniClassIdInput.className = "uniClassIdInput";
          uniClassIdInput.type = "hidden";
          uniClassIdInput.value = keySchedule;
          uniClassNameDiv.textContent = uniClassName; // Gán nội dung lớp học vào
          uniClasLectureDiv.textContent = uniClasLecture; // Gán nội dung giáo viên vào

          const roomSelect = document.createElement("select");
          roomSelect.className = "optionRoom form-control";

          const roomOptions = ["Chọn phòng",
            "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", 
            "211", "212", "213", "214", "215", "301", "302", "303", "304", "305", 
            "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", 
            "401", "402", "403", "404", "405", "406", "407", "408", "409", "410", 
            "411", "412", "413", "414", "415", "501", "502", "503", "504", "505", 
            "506", "507", "508", "509", "510", "511", "512", "513", "514", "515"
          ];
          roomOptions.forEach((room) => {
            const option = document.createElement("option");
            if (room === "Chọn phòng") {
              option.value = null;
              option.textContent = room;
              roomSelect.appendChild(option);
            } else {
              option.value = room;
              option.textContent = "Phòng " + room;
              roomSelect.appendChild(option);
            }
          });

          containerDiv.appendChild(uniClassNameDiv);
          containerDiv.appendChild(uniClasLectureDiv);
          containerDiv.appendChild(roomSelect); // Thêm select vào containerDiv
          containerDiv.appendChild(uniClassIdInput);
          targetCell1.appendChild(containerDiv);
        }
       
        numberSlot++;
        
        if (numberSlot === 5) {
          numberSlot = 1;
          dayCheck1++;
          dayCheck2++;
          dataDay1 = daysOfWeek[dayCheck1];
          dataDay2 = daysOfWeek[dayCheck2];
        }
        dataSlot = "Slot " + numberSlot;
       
        mapUniClass.delete(key1)
      }
    });
  });
}
function updateWeeks() {
  const semesterSelect = document.getElementById("semester");
  const weekSelectStart = document.getElementById("weekStart");
  const weekSelectEnd = document.getElementById("weekEnd");

  weekSelectStart.innerHTML = ""; // Xóa các option trước khi thêm mới
  weekSelectEnd.innerHTML = ""; // Xóa các option trước khi thêm mới

  // Thêm tùy chọn "Chọn tuần" với value là null
  const placeholderOptionStart = document.createElement("option");
  placeholderOptionStart.text = "Chọn tuần";
  placeholderOptionStart.value = null;
  placeholderOptionStart.disabled = true; // Không cho chọn mặc định
  placeholderOptionStart.selected = true; // Đặt làm tùy chọn mặc định
  weekSelectStart.add(placeholderOptionStart);

  const placeholderOptionEnd = document.createElement("option");
  placeholderOptionEnd.text = "Chọn tuần";
  placeholderOptionEnd.value = null;
  placeholderOptionEnd.disabled = true; // Không cho chọn mặc định
  placeholderOptionEnd.selected = true; // Đặt làm tùy chọn mặc định
  weekSelectEnd.add(placeholderOptionEnd);

  const selectedSemester = semesterSelect.value.trim(); // Loại bỏ khoảng trắng đầu/cuối
  const [season, yearSuffix] = selectedSemester.split(" "); // Tách chuỗi tại dấu cách
  const year = "20" + yearSuffix;

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

  const today = new Date();
  let day = today.getDate();
  if (day < 10) {
    day = "0" + day;
  }
  let month = today.getMonth() + 1;
  if (month < 10) {
    month = "0" + month;
  }
  const yearCurrent = today.getFullYear();
  const currentDate = `${yearCurrent}-${month}-${day}`;

  let indexWeek = 0;
  weeks.forEach((week) => {
    const startWeek =
      year +
      "-" +
      week.split("-")[0].split("/")[1].trim() +
      "-" +
      week.split("-")[0].split("/")[0];
    const endWeek =
      year +
      "-" +
      week.split("-")[1].split("/")[1].trim() +
      "-" +
      week.split("-")[1].split("/")[0].trim();
    indexWeek += 1;
    if (currentDate >= startWeek && currentDate <= endWeek) {
      checkIndex = indexWeek - 1;
    }

    const optionWeekStart = document.createElement("option");
    const optionWeekEnd = document.createElement("option");
    optionWeekStart.text = week;
    optionWeekEnd.text = week;
    weekSelectStart.add(optionWeekStart);
    weekSelectEnd.add(optionWeekEnd);
  });
}



function getRoomUniClass() {
  const roomSelect = document.querySelectorAll(".optionRoom");
  
  roomSelect.forEach((roomOptions) => {
    roomOptions.addEventListener('change', (event) => {
        const parentDiv = event.target.closest('.event');
        const valueKey = parentDiv.querySelector(".uniClassIdInput").value;
        const room = event.target.value;
        mapSchedule.set(valueKey, room);  
        let mapCheckRoom = new Map();
        let checkRoom = 0;
        mapCheckRoom.set(valueKey, room)
        mapSchedule.forEach((value, key) => {
          mapCheckRoom.forEach((valueCheck, keyCheck) => {
            if(keyCheck.split('-')[1] === key.split('-')[1] && value === valueCheck && value !== null && valueCheck !== null && keyCheck.split('-')[2] === key.split('-')[2] ){
              checkRoom ++;
            }
          })
        })
        if (checkRoom > 1){
          createScheduleError();
          event.target.value = null;
        }
        
    })
  });
 
}

function createScheduleError() {
  var x = document.getElementById("createScheduleError");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}


function createScheduleError2() {
  var x = document.getElementById("createScheduleError2");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

function createScheduleError3() {
  var x = document.getElementById("createScheduleError3");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}
function createScheduleError4() {
  var x = document.getElementById("createScheduleError4");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}
function createScheduleError5() {
  var x = document.getElementById("createScheduleError5");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

function openModalCreateSchedule(){
  let checkSizeRoom = 0;
  let checkWeek = 0;
  mapSchedule.forEach((value, key) => {
    if (value === null){
      checkSizeRoom++
    }
  })
  mapWeekLearn.forEach((value, key) => {
    if (value === null){
      checkWeek++
    }
  })
  if (checkSizeRoom !== 0){
    createScheduleError2();
  } else
  if (checkWeek !== 0){
    createScheduleError3();
  } else {
    openModal()
  }
}


function openModal() {
  document.getElementById('modal').style.display = 'block';
  document.body.style.overflow = 'hidden'; // Ngăn cuộn trang khi modal mở
  }
  
function closeModal() {
  document.getElementById('modal').style.display = 'none';

}


function chooseWeekStart(){
  const weekStart = document.getElementById('weekStart');
  weekStart.addEventListener('change', (event) => {
    const weekStartValue = event.target.value;
    const weekStartValue1 = weekStartValue.split('-')[0];
    const weekStartValue3 = weekStartValue.split('-')[1];
    const year = '20' + document.querySelector(".createInput").value.slice(-2);
    const weekStartValue2 =year + '-' + weekStartValue1.split('/')[1].trim() + '-' + weekStartValue1.split('/')[0];
    const valueWeekStart = mapWeekLearn.get("weekEnd");
    
    const previousValueWeekEnd = mapWeekLearn.get("weekStart"); // Lưu giá trị cũ
    const dayStartWeek = new Date(previousValueWeekEnd);
    dayStartWeek.setDate(dayStartWeek.getDate() + 6);
    const dayEndWeekString = dayStartWeek.toISOString().split('T')[0];
    console.log(dayEndWeekString);
    
    if (valueWeekStart !== null && weekStartValue2 > valueWeekStart) {
      createScheduleError4();
      if (previousValueWeekEnd !== null) {
        mapWeekLearn.set("weekStart", previousValueWeekEnd); // Đặt lại giá trị cũ
        event.target.value =  previousValueWeekEnd.split('-')[2] + '/' + previousValueWeekEnd.split('-')[1].trim() + ' - ' + dayEndWeekString.split('-')[2] + '/' + dayEndWeekString.split('-')[1].trim();
      } else {
         event.target.value = null
      }
    } else {
      // Cập nhật giá trị mới nếu hợp lệ
      mapWeekLearn.set("weekStart", weekStartValue2);
    }
    
    
  })
}

function chooseWeekEnd(){
  const weekEnd = document.getElementById('weekEnd');
  weekEnd.addEventListener('change', (event) => {
    const weekEndValue = event.target.value;
    const weekEndValue1 = weekEndValue.split('-')[1];
    const year = '20' + document.querySelector(".createInput").value.slice(-2);
    const weekEndValue2 =year + '-' + weekEndValue1.split('/')[1] + '-' + weekEndValue1.split('/')[0].trim();
    const valueWeekStart = mapWeekLearn.get("weekStart");
    
    const previousValueWeekEnd = mapWeekLearn.get("weekEnd"); // Lưu giá trị cũ
    const dayStartWeek = new Date(previousValueWeekEnd);
    dayStartWeek.setDate(dayStartWeek.getDate() - 6);
    const dayEndWeekString = dayStartWeek.toISOString().split('T')[0];
    
    if (valueWeekStart !== null && weekEndValue2 < valueWeekStart) {
      createScheduleError5();
      if (previousValueWeekEnd !== null) {
        mapWeekLearn.set("weekEnd", previousValueWeekEnd); // Đặt lại giá trị cũ
        event.target.value = dayEndWeekString.split('-')[2] + '/' + dayEndWeekString.split('-')[1].trim() + ' - ' +  previousValueWeekEnd.split('-')[2] + '/' + previousValueWeekEnd.split('-')[1].trim() ;
      } else {
         event.target.value = null
      }
    } else {
      // Cập nhật giá trị mới nếu hợp lệ
      mapWeekLearn.set("weekEnd", weekEndValue2);
    }
  })
}

function handleCreateSchedule() {
  const createSchedule = document.querySelector('#createSchedule');
      if (createSchedule) {
          createSchedule.addEventListener('submit', function(event) {
              // Chuyển Map thành JSON
              const mapDataSchedule = JSON.stringify(Object.fromEntries(mapSchedule));
              const mapDataWeekLearn = JSON.stringify(Object.fromEntries(mapWeekLearn));
    
              // Gán chuỗi JSON vào input ẩn
              document.getElementById('mapDataSchedule').value = mapDataSchedule;
              document.getElementById('mapDataWeekLearn').value = mapDataWeekLearn;
          });
      } else {
          console.error("Không tìm thấy form.");
  }
}