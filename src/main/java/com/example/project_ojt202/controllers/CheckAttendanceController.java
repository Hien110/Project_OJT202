package com.example.project_ojt202.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Scheduce;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.CheckAttendanceService;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.ScheduceService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/lecture")
public class CheckAttendanceController {

    private final LectureProfileService lecturerService;
    private final CheckAttendanceService attendanceService;
    private final ScheduceService scheduceService;

    public CheckAttendanceController(LectureProfileService lecturerService, 
    CheckAttendanceService attendanceService, ScheduceService scheduceService) {
        this.lecturerService = lecturerService;
        this.attendanceService = attendanceService;
        this.scheduceService = scheduceService;
    }

    @GetMapping("/l_listClassTakeAttendance")
    public String getClasses(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        String lectureID = null;
        List<UniClass> classes = null;
    
        if (account != null && account.getLectureProfile() != null) {
            lectureID = account.getLectureProfile().getLectureID();
        }
    
        // Lấy danh sách lớp học của giảng viên
        classes = lecturerService.getClassesForLecturer(lectureID);
    
        // Lấy ngày hôm nay
        LocalDate currentDate = LocalDate.now();
    
        // Tạo Map để lưu danh sách lịch học cho từng lớp, chỉ bao gồm những lớp có lịch học trong ngày hôm nay
        Map<Long, List<Scheduce>> schedulesByClass = classes.stream()
            .collect(Collectors.toMap(
                UniClass::getUniClassId,
                uniClass -> scheduceService.findScheduceOfUniClass(uniClass.getUniClassId()).stream()
                    .filter(scheduce -> scheduce.getDateScheduce().equals(currentDate)) // So sánh với ngày hôm nay
                    .collect(Collectors.toList())
            ));
    
        // Lọc những lớp có lịch học trong ngày hôm nay
        classes = classes.stream()
            .filter(uniClass -> schedulesByClass.get(uniClass.getUniClassId()).size() > 0)
            .collect(Collectors.toList());
    
        model.addAttribute("classes", classes);
        model.addAttribute("schedulesByClass", schedulesByClass);
    
        return "l_listClassTakeAttendance";
    }    
    @GetMapping("/l_listClassTakeAttendance/{uniClassId}/l_listStudentTakeAttendance")
    public String getStudentsInClass(@PathVariable String uniClassId, Model model) {
        Long uniClassIdLong;
        try {
            uniClassIdLong = Long.valueOf(uniClassId);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid class ID");
            return "error";
        }

        List<StudentProfile> students = lecturerService.getStudentsForClass(uniClassIdLong);
        model.addAttribute("students", students);
        model.addAttribute("uniClassId", uniClassIdLong);
        return "l_listStudentTakeAttendance";
    }

    @GetMapping("/l_listClassTakeAttendance/{uniClassId}/submit-attendance")
    public String submitAttendance(@PathVariable Long uniClassId,
            @RequestParam Map<String, String> attendanceData, Model model) {
        // Ghi nhận điểm danh
        attendanceData.forEach((studentId, status) -> {
            boolean isPresent = "present".equals(status);
            attendanceService.recordAttendance(studentId, uniClassId, isPresent);
        });

        // Thêm thông báo xác nhận điểm danh
        model.addAttribute("message", "Điểm danh thành công!");

        // Lấy danh sách sinh viên để hiển thị lại trang
        List<StudentProfile> students = lecturerService.getStudentsForClass(uniClassId);
        model.addAttribute("students", students);
        model.addAttribute("uniClassId", uniClassId);

        return "redirect:/lecture/l_listClassTakeAttendance";
    }
}
