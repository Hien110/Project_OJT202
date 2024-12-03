// Xử lý sự kiện khi người dùng nhấn Enter
document.addEventListener("keydown", function (event) {
  if (event.key === "Enter") {
    event.preventDefault(); // Ngăn việc gửi form mặc định

    // Kiểm tra nếu trường đang hoạt động là một trong các trường cần kiểm tra
    const activeElement = document.activeElement;

    if (activeElement.id === "questionCount") {
      handleQuestionCountChange();
    } else if (
      activeElement.id === "hard" ||
      activeElement.id === "medium" ||
      activeElement.id === "easy"
    ) {
      adjustQuestions(activeElement.id);
    } else if (
      activeElement.id === "startExamDate" ||
      activeElement.id === "startExamTime" ||
      activeElement.id === "endExamDate" ||
      activeElement.id === "endExamTime"
    ) {
      combineDateTime();
    }
  }
});
document.addEventListener("keydown", function (event) {
  if (event.key === "Enter") {
    event.preventDefault(); // Ngăn việc gửi form mặc định

    // Kiểm tra nếu trường đang hoạt động là một trong các trường cần kiểm tra
    const activeElement = document.activeElement;

    if (activeElement.id === "questionCount") {
      handleQuestionCountChange();
    } else if (
      activeElement.id === "hard" ||
      activeElement.id === "medium" ||
      activeElement.id === "easy"
    ) {
      adjustQuestions(activeElement.id);
    } else if (
      activeElement.id === "startExamDate" ||
      activeElement.id === "startExamTime" ||
      activeElement.id === "endExamDate" ||
      activeElement.id === "endExamTime"
    ) {
      combineDateTime();
    }
  }
});

// Hiển thị hoặc ẩn mật khẩu
function togglePassword() {
  const passwordInput = document.getElementById("passwordInput");
  passwordInput.type = passwordInput.type === "password" ? "text" : "password";
}

// Gộp thời gian bắt đầu và kết thúc, kiểm tra tính hợp lệ
function combineDateTime() {
  // Lấy giá trị của startExamDate và startExamTime
  const startDate = document.getElementById("startExamDate").value;
  const startTime = document.getElementById("startExamTime").value;

  if (startDate && startTime) {
    const testStartTime = `${startDate}T${startTime}`;
    document.getElementById("testStartTime").value = testStartTime; // Cập nhật giá trị cho trường ẩn
    console.log("Thời gian bắt đầu (testStartTime):", testStartTime);
  } else {
    alert("Vui lòng chọn đầy đủ thời gian bắt đầu.");
    return false;
  }

  // Lấy giá trị của endExamDate và endExamTime
  const endDate = document.getElementById("endExamDate").value;
  const endTime = document.getElementById("endExamTime").value;

  if (endDate && endTime) {
    const testEndTime = `${endDate}T${endTime}`;
    document.getElementById("testFinishTime").value = testEndTime; // Cập nhật giá trị cho trường ẩn
    console.log("Thời gian kết thúc (testFinishTime):", testEndTime);
  } else {
    alert("Vui lòng chọn đầy đủ thời gian kết thúc.");
    return false;
  }
  return true;
}

// Hiển thị form upload câu hỏi hoặc ngân hàng câu hỏi
function setStatusTest(element) {
  const statusValue = element.getAttribute("data-value");
  document.getElementById("statusTestInput").value = statusValue;

  const uploadFileForm = document.getElementById("uploadFileForm");
  if (statusValue === "1") {
    uploadFileForm.style.display = "block";
  } else {
    uploadFileForm.style.display = "none";
  }
}

// Xử lý số lượng câu hỏi tổng, giới hạn trong khoảng từ 10 đến 60
function handleQuestionCountChange() {
  const questionCountInput = document.getElementById("questionCount");
  let totalQuestions = parseInt(questionCountInput.value) || 0;

  if (totalQuestions > 60) {
    alert("Không được quá 60 câu hỏi. Đã đặt về 60.");
    totalQuestions = 60;
    questionCountInput.value = totalQuestions;
  } else if (totalQuestions < 10) {
    alert("Không được dưới 10 câu hỏi. Đã đặt về 10.");
    totalQuestions = 10;
    questionCountInput.value = totalQuestions;
  }

  updateQuestionDistribution(); // Gọi hàm phân phối câu hỏi
}

// Phân phối câu hỏi dựa trên mức độ được chọn
function updateQuestionDistribution() {
  const questionCount =
    parseInt(document.getElementById("questionCount").value) || 0;
  const level = document.getElementById("levelTest").value;

  let hard = 0,
    medium = 0,
    easy = 0;

  if (level === "Dễ") {
    hard = Math.floor(questionCount * 0.1);
    medium = Math.floor(questionCount * 0.2);
    easy = questionCount - hard - medium;
  } else if (level === "Trung Bình") {
    hard = Math.floor(questionCount * 0.1);
    medium = Math.floor(questionCount * 0.7);
    easy = questionCount - hard - medium;
  } else if (level === "Khó") {
    hard = Math.floor(questionCount * 0.7);
    medium = Math.floor(questionCount * 0.2);
    easy = questionCount - hard - medium;
  }

  if (hard + medium + easy !== questionCount) {
    alert("Đã cân bằng lại số câu hỏi.");
  }

  document.getElementById("hard").value = hard;
  document.getElementById("medium").value = medium;
  document.getElementById("easy").value = easy;
}

// Điều chỉnh câu hỏi khi thay đổi một trong các trường
function adjustQuestions(changedField) {
  const questionCount =
    parseInt(document.getElementById("questionCount").value) || 0;
  const hardInput = document.getElementById("hard");
  const mediumInput = document.getElementById("medium");
  const easyInput = document.getElementById("easy");

  let hard = parseInt(hardInput.value) || 0;
  let medium = parseInt(mediumInput.value) || 0;
  let easy = parseInt(easyInput.value) || 0;

  // Tổng số câu hiện tại
  let total = hard + medium + easy;

  // Nếu tổng vượt quá questionCount, giảm các trường khác để khớp tổng
  if (total > questionCount) {
    const excess = total - questionCount;

    if (changedField === "hard") {
      if (medium > 0) {
        medium = Math.max(0, medium - excess);
      } else if (easy > 0) {
        easy = Math.max(0, easy - excess);
      } else {
        alert("Không thể tăng câu khó vì tổng đã đạt giới hạn.");
        hard = questionCount - medium - easy; // Hoàn tác tăng
      }
    } else if (changedField === "medium") {
      if (hard > 0) {
        hard = Math.max(0, hard - excess);
      } else if (easy > 0) {
        easy = Math.max(0, easy - excess);
      } else {
        alert("Không thể tăng câu trung bình vì tổng đã đạt giới hạn.");
        medium = questionCount - hard - easy; // Hoàn tác tăng
      }
    } else if (changedField === "easy") {
      if (hard > 0) {
        hard = Math.max(0, hard - excess);
      } else if (medium > 0) {
        medium = Math.max(0, medium - excess);
      } else {
        alert("Không thể tăng câu dễ vì tổng đã đạt giới hạn.");
        easy = questionCount - hard - medium; // Hoàn tác tăng
      }
    }
  }

  // Không cho giảm dưới 0
  hard = Math.max(0, hard);
  medium = Math.max(0, medium);
  easy = Math.max(0, easy);

  // Đảm bảo tổng không bị lệch
  total = hard + medium + easy;
  if (total < questionCount) {
    const deficit = questionCount - total;

    if (changedField === "hard") {
      if (medium === easy) {
        Math.random() < 0.5 ? medium++ : easy++;
      } else if (medium > easy) {
        medium = Math.min(medium + deficit, questionCount - hard);
      } else {
        easy = Math.min(easy + deficit, questionCount - hard);
      }
    } else if (changedField === "medium") {
      if (hard === easy) {
        Math.random() < 0.5 ? hard++ : easy++;
      } else if (hard > easy) {
        hard = Math.min(hard + deficit, questionCount - medium);
      } else {
        easy = Math.min(easy + deficit, questionCount - medium);
      }
    } else if (changedField === "easy") {
      if (hard === medium) {
        Math.random() < 0.5 ? hard++ : medium++;
      } else if (hard > medium) {
        hard = Math.min(hard + deficit, questionCount - easy);
      } else {
        medium = Math.min(medium + deficit, questionCount - easy);
      }
    }
  }

  // Cập nhật lại giá trị vào các trường
  hardInput.value = hard;
  mediumInput.value = medium;
  easyInput.value = easy;
}

// Cập nhật mức độ tự động dựa trên tỷ lệ câu hỏi
function updateLevelBasedOnDistribution(hard, medium, easy, questionCount) {
  const levelSelect = document.getElementById("levelTest");

  if (hard >= questionCount * 0.7) {
    levelSelect.value = "Khó";
  } else if (medium >= questionCount * 0.7) {
    levelSelect.value = "Trung Bình";
  } else {
    levelSelect.value = "Dễ";
  }
}

document.addEventListener("DOMContentLoaded", function () {
  document
    .getElementById("questionCount")
    .addEventListener("blur", handleQuestionCountChange);
  document
    .getElementById("hard")
    .addEventListener("blur", () => adjustQuestions("hard"));
  document
    .getElementById("medium")
    .addEventListener("blur", () => adjustQuestions("medium"));
  document
    .getElementById("easy")
    .addEventListener("blur", () => adjustQuestions("easy"));
  document
    .getElementById("startExamDate")
    .addEventListener("blur", validateEndExamDate);

  document
    .getElementById("endExamDate")
    .addEventListener("blur", validateEndExamDate);

  document
    .getElementById("startExamTime")
    .addEventListener("blur", validateEndExamTime);

  document
    .getElementById("endExamTime")
    .addEventListener("blur", validateEndExamTime);
});

function validateEndExamDate() {
  const startDate = document.getElementById("startExamDate").value;
  const endDate = document.getElementById("endExamDate").value;

  if (startDate && endDate) {
    if (new Date(endDate) < new Date(startDate)) {
      alert("Ngày kết thúc không được nhỏ hơn ngày bắt đầu.");
      document.getElementById("endExamDate").value = ""; // Xóa giá trị không hợp lệ
      return false;
    }
  }
  return true;
}

// Kiểm tra endExamTime nếu endExamDate <= startExamDate
function validateEndExamTime() {
  const startDate = document.getElementById("startExamDate").value;
  const endDate = document.getElementById("endExamDate").value;
  const startTime = document.getElementById("startExamTime").value;
  const endTime = document.getElementById("endExamTime").value;

  if (startDate && endDate && startTime && endTime) {
    if (new Date(endDate) <= new Date(startDate)) {
      const startDateTime = new Date(`${startDate}T${startTime}`);
      const endDateTime = new Date(`${endDate}T${endTime}`);

      // Kiểm tra nếu thời gian kết thúc nhỏ hơn hoặc bằng thời gian bắt đầu
      if (endDateTime <= startDateTime) {
        alert("Thời gian kết thúc phải lớn hơn thời gian bắt đầu.");
        document.getElementById("endExamTime").value = ""; // Xóa giá trị không hợp lệ
        return false;
      }
    }
  }
  return true;
}
function filterTests() {
  // Lấy giá trị của classSelect
  var selectedClassId = document.getElementById("classSelect").value;

  // Lấy tất cả các phần tử option trong testSelect
  var testSelect = document.getElementById("testSelect");
  var options = testSelect.getElementsByTagName("option");

  // Lặp qua tất cả các option và ẩn hoặc hiển thị chúng dựa trên điều kiện
  for (var i = 1; i < options.length; i++) {
    // Bắt đầu từ i = 1 để bỏ qua option "Chọn bài kiểm tra"
    var option = options[i];
    var subjectId = option.getAttribute("data-subject");

    if (selectedClassId === subjectId || selectedClassId === "") {
      option.style.display = "block"; // Hiển thị nếu điều kiện đúng
    } else {
      option.style.display = "none"; // Ẩn nếu điều kiện không đúng
    }
  }
}

function validateNumberInput(input) {
  // Kiểm tra xem giá trị nhập vào có phải là số và có lớn hơn hoặc bằng 1 không
  if (isNaN(input.value) || input.value < 1) {
    input.setCustomValidity("Vui lòng nhập số hợp lệ và lớn hơn hoặc bằng 1.");
    input.value = ""; // Xóa giá trị nếu không hợp lệ
  } else {
    input.setCustomValidity(""); // Đặt lại nếu giá trị hợp lệ
  }
}
