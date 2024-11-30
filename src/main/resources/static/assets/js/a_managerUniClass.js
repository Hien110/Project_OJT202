document.addEventListener("DOMContentLoaded", function() {
    const check = document.querySelectorAll(".subjectOfUni");
    handleSelectChange();
    check.forEach((element) => {
        const classCheck = element.querySelectorAll(".uniClassBox");
        const classCountInput = element.querySelector(".classCountInput");
        classCountInput.value = classCheck.length;
    });

    const semesters = document.querySelectorAll(".semesters");
    const semesterValue = document.querySelectorAll(".semesterValue")
    let semesterValueString;
    semesters.forEach((semester) => {
            const buttonCreateClass = semester.querySelector(".createClass");
            const semesterTime = semester.querySelector(".semester");
            const season = semesterTime.innerText.slice(0,-2);
            const yearSeason = semesterTime.innerText.slice(-2);
            const year = "20" + yearSeason; // Năm (2023, 2024)
            const uniClassBox = semester.querySelectorAll(".uniClassBox");
            
            let startMonth, endMonth;
            if (season === "Summer") {
            startMonth = 4;
            endMonth = 5;
            } else if (season === "Fall") {
            startMonth = 11;
            endMonth = 12;
            } else if (season === "Spring") {
            startMonth = 12;
            endMonth = 1; 
            }
          
           
           
            const showButton = createButton (startMonth, endMonth, year);
            if(showButton){
                const test = semester.querySelectorAll(".uniClassBox");
                if (test.length === 0 ){
                buttonCreateClass.style.display = "block";
                }
                semesterValueString = season.slice(0,2) + yearSeason;
                semesterValue.forEach((semester1) => {
                    semester1.value = semesterValueString;
                })
                uniClassBox.forEach((uniClass) => {
                    const lectureOption = uniClass.querySelector(".optionLecture");
                    lectureOption.style.display = "block";
                })
            } else {
                buttonCreateClass.style.display = "none";
                uniClassBox.forEach((uniClass) => {
                    const lectureData = uniClass.querySelector(".lectureData");
                    lectureData.style.display = "block";
                })
            }
     })

     const formEditUniClass = document.querySelector("#editUniClass");
     const formCreateUniClass = document.querySelector("#createUniClass");
     formCreateUniClass.addEventListener('submit', function(event){
        document.getElementById("semesterValue").value = semesterValueString;
     })
     if (formEditUniClass) {
        formEditUniClass.addEventListener('submit', function(event) {
              // Chuyển Map thành JSON

            const mapDataInputLecture =   JSON.stringify(Object.fromEntries(mapLectureUniClass));
            
            document.getElementById('mapDataInputLecture').value = mapDataInputLecture;
          });
      } else {
          console.error("Không tìm thấy form.");
      }
});
    
    function createButton (startMonth, endMonth, year) {
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
        const endMonthFull = year + '-' + endMonth + '-01';
        let startMonthFull 
        if (startMonth === 12){
            startMonthFull = year-1 + '-' + startMonth + '-01';
        } else {
            startMonthFull = year + '-' + startMonth + '-01';
        }

        if(startMonthFull <= currentDate && currentDate <= endMonthFull){
            return true;
        } else {
            return false;
        }
    }


    // Hàm lắng nghe sự kiện thay đổi cho cả optionRoom và optionLecture
    // let mapChangeRoomUniClass = new Map();
    let mapLectureUniClass = new Map();
    function handleSelectChange() {
        // document.querySelectorAll('.optionRoom').forEach(selectElement => {
        //     const saveButton = document.querySelector("#saveButton");
        //     selectElement.addEventListener('change', (event) => {
        //         const newValue = event.target.value;
        //         // Tìm div chứa phần tử select đang thay đổi
        //         const parentDiv = event.target.closest('.uniClassBox');
        //         // Tìm input của tên lớp học, nếu không tìm thấy thì đặt là 'Không xác định'
        //         const UniClassID = parentDiv.querySelector('.UniClassID').value;
        //         mapChangeRoomUniClass.set(UniClassID, newValue)
        //         if (mapChangeRoomUniClass.size > 0) {
        //             saveButton.style.display = "block";  
        //           } else {
        //             saveButton.style.display = "none";  
        //           }
        //     });
        // });
        document.querySelectorAll('.optionLecture').forEach(selectElement => {
            const saveButton = document.querySelector("#saveButton");
            selectElement.addEventListener('change', (event) => {
                const newValue = event.target.value;
                // Tìm div chứa phần tử select đang thay đổi
                const parentDiv = event.target.closest('.uniClassBox');
                // Tìm input của tên lớp học, nếu không tìm thấy thì đặt là 'Không xác định'
                const UniClassID = parentDiv.querySelector('.UniClassID').value;
                mapLectureUniClass.set(UniClassID, newValue)
                if (mapLectureUniClass.size > 0) {
                    saveButton.style.display = "block";  
                  } else {
                    saveButton.style.display = "none";  
                  }
            });
        });
       
    }
    
    function openModal() {
        document.getElementById('modal').style.display = 'block';
        document.body.style.overflow = 'hidden'; // Ngăn cuộn trang khi modal mở
        }
        
    function closeModal() {
        document.getElementById('modal').style.display = 'none';

    }
        

    window.addEventListener("load", function() {
        // Kiểm tra nếu dấu hiệu tồn tại trong localStorage
        
        if (localStorage.getItem("showSnackbar") === "true") {
          var x = document.getElementById("createUniClassSuccess");
          x.className = "show";
          
          setTimeout(function() {
            x.className = x.className.replace("show", "");
          }, 3000);
          
          // Xóa dấu hiệu sau khi hiển thị snackbar
          localStorage.removeItem("showSnackbar");
        }
        if (localStorage.getItem("updateUniClassSucces") === "true") {
          var x = document.getElementById("updateUniClassSucces");
          x.className = "show";
          
          setTimeout(function() {
            x.className = x.className.replace("show", "");
          }, 3000);
          
          // Xóa dấu hiệu sau khi hiển thị snackbar
          localStorage.removeItem("updateUniClassSucces");
        }
      });


    function updateUniClassSucces() {
    // Lưu dấu hiệu vào localStorage trước khi tải lại
    localStorage.setItem("updateUniClassSucces", "true");
    window.location.reload();
    }