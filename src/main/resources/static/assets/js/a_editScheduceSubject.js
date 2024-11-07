document.addEventListener("dragstart", function(event) {
    var draggedElement = event.target;
    var itemDate = draggedElement.getAttribute("data-date");
    
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
const year = "20" + selectedSemester.slice(-2); // Năm  
// Xác định ô thả (dropTarget)

// Lấy giá trị ngày và slot
var dropTarget = event.target.closest('.droptarget');
var day = dropTarget.getAttribute("data-day");
var slot = dropTarget.closest("tr").getAttribute("data-slot");
const date =  document.getElementById(day).innerText;
const valueDate = date.split('\n')
const dateChange = year +'-' + valueDate[1].split('/')[1] + '-' + valueDate[1].split('/')[0];
let valueChange = slot + '_' + dateChange;
mapChangeScheduce.set(draggedId, valueChange);
const saveButton = document.querySelector("#saveButton");
if (mapChangeScheduce.size > 0){
    saveButton.style.display = "block";  
} else {
    saveButton.style.display = "none";  
}
const dateRoot = draggedElement.getAttribute('data-date');
console.log(draggedElement.getAttribute('id'));
console.log(slot);

if (dateChange < dateRoot){
  updateScheduleError();
  return '';
}
if (dropTarget) {
// Thêm phần tử được kéo vào dropTarget
if (dropTarget.innerHTML !== '') {
    var existingValue = dropTarget.firstChild;
    dropTarget.appendChild(draggedElement);
    dropTarget.appendChild(existingValue);
} else {
    dropTarget.appendChild(draggedElement);
}


}
});

function openModal() {
document.getElementById('modal').style.display = 'block';
document.body.style.overflow = 'hidden'; // Ngăn cuộn trang khi modal mở
}

function closeModal() {
  window.location.reload();
}

function updateScheduleError() {
  var x = document.getElementById("updateScheduleError");
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
});

