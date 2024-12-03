document.addEventListener("DOMContentLoaded", function() {
    // Lấy nút "Tạo lớp học tự động"
    createClassAuto();
    checkLecture();
    createClass();
});

let mapLectureUniClass = new Map();
    function handleSelectChange() {
        document.querySelectorAll('.optionLecture').forEach(selectElement => {
            const semesterValue = document.querySelector(".semesterValue").value;
            let semesterFull;
            
            switch(semesterValue.slice(0,2)){
                case 'Fa': 
                    semesterFull = "Fall" + semesterValue.slice(-2);
                    break;
                case 'Su': 
                    semesterFull = "Summer" + semesterValue.slice(-2);
                    break;
                case 'Sp': 
                    semesterFull = "Spring" + semesterValue.slice(-2);
                    break;
            }
            selectElement.addEventListener('change', (event) => {
                const lectureID = event.target.value;
                // Tìm div chứa phần tử select đang thay đổi
                const parentDiv = event.target.closest('.uniClassBox');
                const uniClassName = parentDiv.querySelector('.uniClassName').value;
                const maxStudent = parentDiv.querySelector('.maxStudent').value;
                // Tìm input của tên lớp học, nếu không tìm thấy thì đặt là 'Không xác định'
                let  newValue ;
                console.log(lectureID);
                if (lectureID !== "null"){
                    newValue = lectureID + '_' + uniClassName.split('_')[0] + '_' + maxStudent + '_' + semesterFull;
                } else {
                    newValue = null;
                }
                console.log(newValue);
                
                mapLectureUniClass.set(uniClassName, newValue)
                
            });
        });
    }

    function createClassAuto(){
        const autoCreateButton = document.querySelector(".autoCreate");
    // Thêm sự kiện click vào nút
    autoCreateButton.addEventListener("click", function(event) {
        event.preventDefault(); // Ngăn chặn form submit
        // Lấy tất cả các phần tử chứa thông tin môn học
        mapLectureUniClass.clear();

        const subjects = document.querySelectorAll(".subjectOfUni");
        const lectures = document.querySelectorAll(".lectureID");
        let lectureOptionsHtml = "";
        lectures.forEach((lecture) => {
            const lectureName = lecture.value;
            const lectureID = lecture.getAttribute("data-lecture");
            lectureOptionsHtml += `<option value="${lectureID}">${lectureName} - ${lectureID}</option>`;
        });

        let checkInforClass = false;
        subjects.forEach((subject) => {
            const totalStudentsInput = subject.querySelector(".numberStudent");
            const totalNumberClassInput = subject.querySelector(".numberClass");
            // Lấy giá trị từ các trường nhập liệu
            const totalStudents = parseInt(totalStudentsInput.value);
            const totalNumberClass = parseInt(totalNumberClassInput.value);
            
            if (Number.isNaN(totalStudents) || Number.isNaN(totalNumberClass)) {
               checkInforClass = true;
            } 
          
    });

    if(checkInforClass){
        createUniClassError2();  
    } else {
        const saveButton = document.querySelector("#saveButton");
        saveButton.style.display = "block";  
        subjects.forEach((subject) => {
            const totalStudentsInput = subject.querySelector(".numberStudent");
            const totalNumberClassInput = subject.querySelector(".numberClass");
            const displayContainer = subject.querySelector(".displayClasses");
                       
            // Lấy giá trị từ các trường nhập liệu
            const totalStudents = parseInt(totalStudentsInput.value);
            const totalNumberClass = parseInt(totalNumberClassInput.value);
            const subjectName = subject.querySelector(".subjectID").value;
            const semester = document.querySelector(".semesterValue").value;
            const semesterUpperCase = semester.toUpperCase();
          
            // Kiểm tra nếu giá trị hợp lệ
                const numberStudentMax = Math.ceil(totalStudents / totalNumberClass);

                // Xóa các lớp học đã hiển thị trước đó
                displayContainer.innerHTML = "";

                // Tạo các lớp học và hiển thị trong displayContainer
                for (let i = 1; i <= totalNumberClass; i++) {
                    const className = `${subjectName}_${semesterUpperCase}_${String(i).padStart(2, '0')}`;
            
                    // Tạo phần tử HTML để hiển thị lớp học
                    const classHtml = `
                        <div class="uniClassBox filter-box" style="margin-top: 15px;">
                            <h3 style="background-color: black;" >Lớp học</h3>
                            <label class="labelSelect">Tên lớp học</label>
                            <input type="text" value="${className}" class="form-control uniClassName" readonly />
                            <label class="labelSelect">Giáo viên</label>
                            <select class="optionLecture form-control" style="width: 160px;">
                                <option value="null">Chọn giáo viên</option>
                                ${lectureOptionsHtml}
                            </select>
                            <label class="labelSelect">Số học sinh tối đa</label>
                            <input type="text" value="${numberStudentMax}" class="form-control maxStudent" readonly />
                        </div>
                    `;
                    
                    // Thêm lớp học vào displayContainer
                    displayContainer.insertAdjacentHTML("beforeend", classHtml);
                    mapLectureUniClass.set(className, null)

                }
        })}
    handleSelectChange();
    });
    }
    

    function createClass() {
        const createUniClass = document.querySelector('#createUniClass');
            if (createUniClass) {
                createUniClass.addEventListener('submit', function(event) {
                    // Chuyển Map thành JSON
                    const mapData = JSON.stringify(Object.fromEntries(mapLectureUniClass));
          
                    // Gán chuỗi JSON vào input ẩn
                    document.getElementById('mapDataUniClass').value = mapData;
                });
            } else {
                console.error("Không tìm thấy form.");
        }
    }

    function checkLecture(){
        const saveButton = document.querySelector('#saveButton');  
        saveButton.addEventListener("click", function(){
            let countError = 0;
            mapLectureUniClass.forEach((value, key) => {
                if (value === null) {
                    countError += 1;
                } 
        })
        if (countError !== 0){
            createUniClassError();
        } else {
            openModal();
        }
    })
}

    function createUniClassError() {
        var x = document.getElementById("createUniClassError");
        x.className = "show";
        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
      }

    function createUniClassError2() {
        var x = document.getElementById("createUniClassError2");
        x.className = "show";
        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
      }


      function openModal() {
        document.getElementById('modal').style.display = 'block';
        document.body.style.overflow = 'hidden'; // Ngăn cuộn trang khi modal mở
        }
        
    function closeModal() {
        document.getElementById('modal').style.display = 'none';

    }

    function createUniClass() {
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
      