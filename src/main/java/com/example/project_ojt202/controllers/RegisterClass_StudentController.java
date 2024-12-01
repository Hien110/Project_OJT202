package com.example.project_ojt202.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.PrerequisiteSubject;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.LearnService;
import com.example.project_ojt202.services.PrerequisiteSubjectService;
import com.example.project_ojt202.services.UniClassService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RegisterClass_StudentController {
    @Autowired
    private UniClassService uniClassService;

    @Autowired
    private LearnService learnService;

    @Autowired
    private PrerequisiteSubjectService prerequisiteSubjectService;

    @GetMapping("/selectMajorClass_Student")
    public String index() {
        return "selectMajorClass_Student";
    }

    @GetMapping("/registerClassDetail/{id}")
    public String index1(@PathVariable("id") Long uniClassId, Model model, HttpSession session) {
        UniClass uniClass = uniClassService.getUniClassById(uniClassId);
        List<Learn> learns = learnService.getAllLearns();
        Account account = (Account) session.getAttribute("account");
        String studentID = account.getStudentProfile().getStudentID();

        // check hs đã ở trong lớp của môn này chưa
        boolean isInSubjectClass = isInSubjectClass(learns, uniClass, studentID);

        // check số lượng hs
        int numberOfStudent = 0;
        boolean errMaxStudent = false;
        for (Learn learn : learns) { // Duyệt qua tất cả các đối tượng Learn
            if (learn.getUniClass() != null && learn.getUniClass().getUniClassId().equals(uniClassId)) {
                numberOfStudent++; // Nếu uniClassId khớp, tăng biến đếm
            }
        }

        if (numberOfStudent >= Integer.parseInt(uniClass.getNumberStudent())) {
            errMaxStudent = true;
        }

        // check đã passed môn hay chưa
        boolean isPassedSubject = hasPassedLearnWithUniClassId(learns, uniClass, studentID);

        // check môn tiên quyết
        List<PrerequisiteSubject> prerequisiteSubjects = prerequisiteSubjectService
                .getPrerequisiteSubjectsBySubjectID(uniClass.getSubject().getSubjectID());
        boolean isPassedPreSubject = isPassedPreSubject(prerequisiteSubjects, learns, uniClassId, studentID);

        // check hs đã có trong lớp học chưa
        boolean isInClass = isInClass(learns, studentID, uniClassId);

        model.addAttribute("isInSubjectClass", isInSubjectClass);
        model.addAttribute("isInClass", isInClass);
        model.addAttribute("isPassedPreSubject", isPassedPreSubject);
        model.addAttribute("isPassedSubject", isPassedSubject);
        model.addAttribute("errMaxStudent", errMaxStudent);
        model.addAttribute("uniClass", uniClass);
        return "/registerClassDetail";
    }

    public boolean hasPassedLearnWithUniClassId(List<Learn> learns, UniClass uniClass, String studentID) {
        // Duyệt qua tất cả các đối tượng Learn trong danh sách
        for (Learn learn : learns) {
            // Kiểm tra điều kiện: uniClassId và learnResult = "passed"
            if (learn.getUniClass() != null
                    && learn.getStudentProfile().getStudentID().equals(studentID)
                    && learn.getUniClass().getSubject().getSubjectID().equals(uniClass.getSubject().getSubjectID())
                    && "passed".equals(learn.getLearnResult())) {
                return true; // Nếu tìm thấy, trả về true
            }
        }
        return false; // Nếu không tìm thấy đối tượng nào thỏa mãn, trả về false
    }

    public boolean isInClass(List<Learn> learns, String studentID, Long uniClassId) {
        for (Learn learn : learns) {
            if (learn.getUniClass().getUniClassId().equals(uniClassId)
                    && learn.getStudentProfile().getStudentID().equals(studentID)) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/registerClass")
    public String submitData(@RequestParam("uniClassId") Long uniClassId, Model model, HttpSession session) {
        // Lấy thông tin lớp học
        UniClass uniClass = uniClassService.getUniClassById(uniClassId);

        // Lấy thông tin tài khoản và studentProfile từ session
        Account account = (Account) session.getAttribute("account");
        StudentProfile studentProfile = account.getStudentProfile();

        // Tạo đối tượng Learn để lưu vào database
        Learn learn = new Learn(null, "inProgress", studentProfile, uniClass);
        learnService.saveDataToDatabase(learn);

        // Thêm thông báo thành công vào model
        model.addAttribute("successMessage", "Đăng ký lớp học thành công!");

        List<Learn> learns = learnService.getAllLearns();
        String studentID = account.getStudentProfile().getStudentID();

        // check hs đã ở trong lớp của môn này chưa
        boolean isInSubjectClass = isInSubjectClass(learns, uniClass, studentID);

        // check số lượng hs
        int numberOfStudent = 0;
        boolean errMaxStudent = false;
        for (Learn learnn : learns) { // Duyệt qua tất cả các đối tượng Learn
            if (learnn.getUniClass() != null && learn.getUniClass().getUniClassId().equals(uniClassId)) {
                numberOfStudent++; // Nếu uniClassId khớp, tăng biến đếm
            }
        }

        if (numberOfStudent >= Integer.parseInt(uniClass.getNumberStudent())) {
            errMaxStudent = true;
        }

        // check đã passed môn hay chưa
        boolean isPassedSubject = hasPassedLearnWithUniClassId(learns, uniClass, studentID);

        // check môn tiên quyết
        List<PrerequisiteSubject> prerequisiteSubjects = prerequisiteSubjectService
                .getPrerequisiteSubjectsBySubjectID(uniClass.getSubject().getSubjectID());
        boolean isPassedPreSubject = isPassedPreSubject(prerequisiteSubjects, learns, uniClassId, studentID);

        // check hs đã có trong lớp học chưa
        boolean isInClass = isInClass(learns, studentID, uniClassId);

        model.addAttribute("isInSubjectClass", isInSubjectClass);
        model.addAttribute("isInClass", isInClass);
        model.addAttribute("isPassedPreSubject", isPassedPreSubject);
        model.addAttribute("isPassedSubject", isPassedSubject);
        model.addAttribute("errMaxStudent", errMaxStudent);
        model.addAttribute("uniClass", uniClass);
        // Trả về lại trang chi tiết lớp học
        return "registerClassDetail"; // Đây là view template để hiển thị lại thông tin chi tiết lớp học
    }

    public boolean isPassedPreSubject(List<PrerequisiteSubject> prerequisiteSubjects, List<Learn> learns,
            Long uniClassId, String studentID) {

        // Duyệt qua tất cả các đối tượng PrerequisiteSubject trong danh sách
        for (PrerequisiteSubject prerequisiteSubject : prerequisiteSubjects) {
            if (prerequisiteSubject.getPrerequisiteSubjectID().equals("null")) {
                return true;
            } else if (!prerequisiteSubject.getPrerequisiteSubjectID().equals("null")) {

                int foundStudent = 0; // ktra có học sinh trong bảng learn hay không
                // Duyệt qua tất cả các đối tượng Learn trong danh sách
                for (Learn learn : learns) {
                    // Kiểm tra nếu StudentID và SubjectID của Learn khớp với PrerequisiteSubject
                    if (learn.getStudentProfile().getStudentID().equals(studentID)
                            && learn.getUniClass().getSubject().getSubjectID()
                                    .equals(prerequisiteSubject.getPrerequisiteSubjectID())) {

                        // Kiểm tra nếu kết quả học không phải là "notPassed"
                        if (learn.getLearnResult().equals("notPassed")
                                || learn.getLearnResult().equals("inProgress")) {
                            return false; // Nếu kết quả học là "notPassed", trả về false
                        }
                        foundStudent++;
                    }
                }
                if (foundStudent == 0) {
                    return false;
                }
            }
        }

        // Nếu tất cả các prerequisiteSubject đều có đối tượng Learn phù hợp và không có
        // "notPassed", trả về true
        return true;
    }

    public boolean isInSubjectClass(List<Learn> learns, UniClass uniClass, String studentID) {
        for (Learn learn : learns) {
            if (learn.getUniClass().getSubject().getSubjectID().equals(uniClass.getSubject().getSubjectID())
                    && learn.getStudentProfile().getStudentID().equals(studentID)) {
                return true;
            }
        }
        return false;
    }
}
