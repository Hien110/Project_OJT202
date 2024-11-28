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

document.addEventListener("dragstart", function(event) {
    var draggedElement = event.target;
    var itemDate = draggedElement.getAttribute("data-date");
      if(currentDate <  itemDate){
        event.dataTransfer.setData("text/plain", event.target.id);
      } else {
        event.preventDefault(); 
      }
});

document.addEventListener("dragend", function(event) {
});

document.addEventListener("dragover", function(event) {
    event.preventDefault();
});

document.addEventListener("dragleave", function(event) {
});
let mapChangeScheduce = new Map();
document.addEventListener("drop", function(event) {
event.preventDefault();
var draggedId = event.dataTransfer.getData("text/plain");
var draggedElement = document.getElementById(draggedId);

const semesterSelect = document.getElementById("semester");
const selectedSemester = semesterSelect.value;
const year = "20" + selectedSemester.slice(-2);  
var dropTarget = event.target.closest('.droptarget');

var day = dropTarget.getAttribute("data-day");
var slot = dropTarget.closest("tr").getAttribute("data-slot");
const date =  document.getElementById(day).innerText;
const valueDate = date.split('\n')
const dateChange = year +'-' + valueDate[1].split('/')[1] + '-' + valueDate[1].split('/')[0];
let valueChange = slot + '_' + dateChange;
const saveButton = document.querySelector("#saveButton");

const idSubjectRoot = draggedElement.getAttribute('id');

// Kiểm tra điều kiện đầu tiên (dateChange > currentDate)
if (dateChange > currentDate) {
  const calendarLiElements = dropTarget.querySelectorAll('.event');
  let errorOccurred = false; // Biến flag để kiểm tra lỗi

  // Kiểm tra điều kiện thứ hai trong vòng lặp
  for (let i = 0; i < calendarLiElements.length - 1; i++) {
      const calendarLi = calendarLiElements[i];
      const classId = calendarLi.getAttribute('style');
      const idTesst = calendarLi.getAttribute('id');
      
      if (classId === 'display: block;' && idTesst.split('-')[1] === idSubjectRoot.split('-')[1]) {
          updateScheduleError2();  // Gọi hàm hiển thị lỗi
          errorOccurred = true;
          break;  // Dừng vòng lặp khi phát hiện lỗi
      }
  }

  // Nếu không có lỗi trong vòng lặp
  if (!errorOccurred) {
      mapChangeScheduce.set(draggedId, valueChange);
      var existingValue = dropTarget.firstChild;
  dropTarget.appendChild(draggedElement);
  dropTarget.appendChild(existingValue);
  } else {
      return ''; // Ngừng thực thi nếu có lỗi
  }
} else {
  updateScheduleError(); // Gọi hàm hiển thị lỗi nếu điều kiện đầu tiên không thỏa
  return '';
}

const createScheduleButton = document.querySelector(".createScheduleButton");
// Hiển thị nút saveButton khi mapChangeScheduce có phần tử
if (mapChangeScheduce.size > 0) {
  saveButton.style.display = "block";  
  createScheduleButton.style.display = "none"
} else {
  saveButton.style.display = "none";  
  createScheduleButton.style.display = "block"

}
});


function editRoomUniClass() {
  const roomSelect = document.querySelectorAll(".optionRoom");
  
  roomSelect.forEach((roomOptions) => {
    roomOptions.addEventListener('change', (event) => {
        const parentDiv = event.target.closest('.droptarget');
        const parentDivChange = event.target.closest('.event');
        const parentDivChangeID = parentDivChange.getAttribute("id");
        const parentDivChangeDate = parentDivChange.getAttribute("data-date");
        const eventDroptarget = parentDiv.querySelectorAll(".event");
        const roomChange = event.target.value;
        let roomOld;
        eventDroptarget.forEach((event1) => {
          const eventID = event1.getAttribute("id");
          const eventRoom = event1.querySelector(".roomData").innerText;
          if(eventID === parentDivChangeID){
            roomOld = eventRoom.split(" ")[1] ;
          }
        })
        eventDroptarget.forEach((event1) => {
          const eventID = event1.getAttribute("id");
          const eventDate = event1.getAttribute("data-date");
          const eventRoom = event1.querySelector(".roomData").innerText;
          
          if (eventID !== parentDivChangeID) {
            if (eventDate === parentDivChangeDate && eventRoom.split(" ")[1] === roomChange) {
              updateScheduleError3();
              event.target.value = "Phòng " + roomOld;
            } else if(eventDate === parentDivChangeDate || eventRoom.split(" ")[1] !== roomChange){
              mapChangeScheduce.set(parentDivChangeID + "-changRoom", roomChange);
              console.log("1");
              const saveButton = document.querySelector("#saveButton");
              const createScheduleButton = document.querySelector(".createScheduleButton");
              if (mapChangeScheduce.size > 0) {
                saveButton.style.display = "block";  
                createScheduleButton.style.display = "none"
              } else {
                saveButton.style.display = "none";  
                createScheduleButton.style.display = "block"
              }
            }
          } 
        })
      
    })
  });

}

function openModal() {
document.getElementById('modal').style.display = 'block';
document.body.style.overflow = 'hidden'; // Ngăn cuộn trang khi modal mở
}

function closeModal() {
  window.location.reload();
}

function openModalCreateSchedule() {
document.getElementById('openModalCreateSchedule').style.display = 'block';
}

function closeModalCreateSchedule() {
  document.getElementById('openModalCreateSchedule').style.display = 'none';
}

function updateScheduleError() {
  var x = document.getElementById("updateScheduleError");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

function updateScheduleError2() {
  var x = document.getElementById("updateScheduleError2");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

function updateScheduleError3() {
  var x = document.getElementById("updateScheduleError3");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

function updateScheduleSucces() {
  // Lưu dấu hiệu vào localStorage trước khi tải lại
  localStorage.setItem("showSnackbar", "true");
  window.location.reload();
}

// Khi trang được tải lại
window.addEventListener("load", function() {
  // Kiểm tra nếu dấu hiệu tồn tại trong localStorage
  if (localStorage.getItem("showSnackbar") === "true") {
    var x = document.getElementById("updateScheduleSucces");
    x.className = "show";
    setTimeout(function() {
      x.className = x.className.replace("show", "");
    }, 3000);
    
    // Xóa dấu hiệu sau khi hiển thị snackbar
    localStorage.removeItem("showSnackbar");
  }
});

document.addEventListener("DOMContentLoaded", function() {
  const formOneWeek = document.querySelector('#editScheduleOneWeek');
  const formEachWeek = document.querySelector('#editScheduleEachWeek');
  if (formOneWeek) {
    formOneWeek.addEventListener('submit', function(event) {
          // Chuyển Map thành JSON
          const mapData = JSON.stringify(Object.fromEntries(mapChangeScheduce));

          // Gán chuỗi JSON vào input ẩn
          document.getElementById('mapDataInput').value = mapData;
      });
  } else {
      console.error("Không tìm thấy form.");
  }
  if (formEachWeek) {
    formEachWeek.addEventListener('submit', function(event) {
          // Chuyển Map thành JSON
          const mapData = JSON.stringify(Object.fromEntries(mapChangeScheduce));

          // Gán chuỗi JSON vào input ẩn
          document.getElementById('mapDataInput1').value = mapData;
      });
  } else {
      console.error("Không tìm thấy form.");
  }

  editRoomUniClass();
});

