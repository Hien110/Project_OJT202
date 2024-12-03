package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.StudentFeedback;
import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Feedback;
import com.example.project_ojt202.models.FeedbackChoice;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.StudentFeedbackService;
import com.example.project_ojt202.services.UniClassService;
import jakarta.servlet.http.HttpSession;
import com.example.project_ojt202.services.FeedbackChoiceService;
import com.example.project_ojt202.services.LearnService;
import com.example.project_ojt202.models.StudentProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("profileAccount")
public class UniClassController {

    @Autowired
    private UniClassService uniClassService;

    @Autowired
    private FeedbackChoiceService feedbackChoiceService;

    @Autowired
    private StudentFeedbackService studentFeedbackService;

    @Autowired
    private LearnService learnService;

    // Hiển thị form feedback cho một lớp
    @GetMapping("/uniClasses/feedback")
public String showFeedbackForm(@RequestParam("id") Long uniClassId,
                               @ModelAttribute("profileAccount") StudentProfile studentProfile,
                               Model model, HttpSession session) {
                                //hhhhh
 Account account = (Account) session.getAttribute("account");
        String studentID = null;

        // Kiểm tra xem account có tồn tại và có thông tin studentProfile không
        if (account != null && account.getStudentProfile() != null) {
            studentID = account.getStudentProfile().getStudentID(); // Lấy studentID từ session
        }

        // Lấy danh sách lớp học của sinh viên
        List<Learn> classes = learnService.getLearnByStudentID(studentID);

        // Thêm thông tin về khả năng hiển thị nút phản hồi
        Map<Long, Boolean> feedbackAvailabilityMap = classes.stream()
                .map(Learn::getUniClass)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        UniClass::getUniClassId,
                        uniClass -> {
                            if (uniClass.getDateEndLearn() == null) {
                                return false;
                            }
                            LocalDate now = LocalDate.now();
                            LocalDate startFeedbackDate = uniClass.getDateEndLearn().minusDays(14);
                            return (now.isAfter(startFeedbackDate) || now.isEqual(startFeedbackDate)) &&
                                   (now.isBefore(uniClass.getDateEndLearn()) || now.isEqual(uniClass.getDateEndLearn()));
                        }
                ));

        // Gắn dữ liệu vào model
        model.addAttribute("classes", classes);
        model.addAttribute("feedbackAvailabilityMap", feedbackAvailabilityMap);
                                //hhhhhhh
    // Kiểm tra lớp học có tồn tại hay không
    UniClass uniClass = uniClassService.getUniClassById(uniClassId);
    if (uniClass == null) {
        model.addAttribute("error", "Lớp học không tồn tại.");
        return "s_list-classforstudent";
    }

    // Kiểm tra nếu học sinh đã gửi feedback
    boolean hasFeedback = studentFeedbackService.hasFeedbackForClass(studentProfile.getStudentID(), uniClassId);
    if (hasFeedback) {
        model.addAttribute("error", "Bạn đã gửi feedback cho lớp học này rồi.");
        List<UniClass> uniClasses = uniClassService.getAllUniClasses();
        model.addAttribute("uniClasses", uniClasses);
        return "s_list-classforstudent"; // Trả về danh sách lớp học cùng với thông báo lỗi
    }

    // Lấy danh sách FeedbackChoice nhưng chỉ giữ lại các Feedback có trạng thái "show"
    List<FeedbackChoice> feedbackChoices = feedbackChoiceService.getAllFeedbackChoices();
    Map<Feedback, List<FeedbackChoice>> feedbackChoicesByQuestion = feedbackChoices.stream()
            .filter(choice -> "show".equalsIgnoreCase(choice.getFeedback().getStatus())) // Lọc Feedback có trạng thái "show"
            .collect(Collectors.groupingBy(FeedbackChoice::getFeedback));

    model.addAttribute("uniClass", uniClass);
    model.addAttribute("feedbackChoicesByQuestion", feedbackChoicesByQuestion);
    return "uniClassFeedback"; // Trang dành riêng để gửi feedback
}

    

    // Xử lý gửi feedback cho lớp học
    @PostMapping("/submitFeedback")
    public String submitFeedback(
            @RequestParam("uniClassId") Long uniClassId,
            @RequestParam Map<String, String> feedbackChoices,
            @RequestParam(value = "feedbackText", required = false) String feedbackText,
            @ModelAttribute("profileAccount") StudentProfile studentProfile,
            Model model, HttpSession session) {

                Account account = (Account) session.getAttribute("account");
                String studentID = null;
        
                // Kiểm tra xem account có tồn tại và có thông tin studentProfile không
                if (account != null && account.getStudentProfile() != null) {
                    studentID = account.getStudentProfile().getStudentID(); // Lấy studentID từ session
                }
        
                // Lấy danh sách lớp học của sinh viên
                List<Learn> classes = learnService.getLearnByStudentID(studentID);
        
                // Thêm thông tin về khả năng hiển thị nút phản hồi
                Map<Long, Boolean> feedbackAvailabilityMap = classes.stream()
                        .map(Learn::getUniClass)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(
                                UniClass::getUniClassId,
                                uniClass -> {
                                    if (uniClass.getDateEndLearn() == null) {
                                        return false;
                                    }
                                    LocalDate now = LocalDate.now();
                                    LocalDate startFeedbackDate = uniClass.getDateEndLearn().minusDays(14);
                                    return (now.isAfter(startFeedbackDate) || now.isEqual(startFeedbackDate)) &&
                                           (now.isBefore(uniClass.getDateEndLearn()) || now.isEqual(uniClass.getDateEndLearn()));
                                }
                        ));
        
                // Gắn dữ liệu vào model
                model.addAttribute("classes", classes);
                model.addAttribute("feedbackAvailabilityMap", feedbackAvailabilityMap);
    
        if (studentProfile == null) {
            return "redirect:/login";
        }
    
        // Kiểm tra lớp học có tồn tại hay không
        UniClass uniClass = uniClassService.getUniClassById(uniClassId);
        if (uniClass == null) {
            model.addAttribute("error", "UniClass không tồn tại.");
            return "s_list-classforstudent";
        }
        
        // Kiểm tra nếu học sinh đã feedback cho lớp học này
        boolean hasFeedback = studentFeedbackService.hasFeedbackForClass(studentProfile.getStudentID(), uniClassId);
        if (hasFeedback) {
            model.addAttribute("error", "Bạn đã gửi feedback cho lớp học này rồi.");
            return "s_list-classforstudent";
        }
    
        // Xử lý feedback choices
        Map<Long, Long> parsedFeedbackChoices = feedbackChoices.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("feedbackChoices[")) // Lọc các input liên quan đến feedbackChoices
                .collect(Collectors.toMap(
                        entry -> Long.parseLong(entry.getKey().replaceAll("feedbackChoices\\[|\\]", "")),
                        entry -> Long.parseLong(entry.getValue())
                ));
    
        // Lưu feedback vào cơ sở dữ liệu
        parsedFeedbackChoices.forEach((questionId, choiceId) -> {
            FeedbackChoice feedbackChoice = feedbackChoiceService.getFeedbackChoiceById(choiceId);
            if (feedbackChoice != null) {
                StudentFeedback studentFeedback = new StudentFeedback();
                studentFeedback.setStudentProfile(studentProfile);
                studentFeedback.setFeedbackChoice(feedbackChoice);
                studentFeedback.setUniClass(uniClass);
                studentFeedback.setFeedbackText(feedbackText); // Lưu văn bản feedback
                studentFeedbackService.saveFeedback(studentFeedback);
            }
        });
    
        return "s_list-classforstudent";
    }
    @GetMapping("/luniClasses")
    public String showUniClassesForLecturer(HttpSession session, 
                                             @RequestParam(required = false) String semester, 
                                             Model model) {
        // Lấy LectureProfile của giảng viên từ session
        LectureProfile lectureProfile = (LectureProfile) session.getAttribute("profileAccount");
    
        // Kiểm tra giảng viên đã đăng nhập hay chưa
        if (lectureProfile == null) {
            return "redirect:/login"; // Chuyển hướng về trang đăng nhập nếu chưa đăng nhập
        }
    
        // Lọc lớp theo học kỳ nếu có, nếu không sẽ lấy tất cả các lớp
        List<UniClass> uniClasses;
        if (semester != null && !semester.isEmpty()) {
            // Lọc lớp theo học kỳ và giảng viên
            uniClasses = uniClassService.getClassesByLecturerAndSemester(lectureProfile, semester);
        } else {
            // Lấy tất cả lớp của giảng viên nếu không có học kỳ
            uniClasses = uniClassService.getClassesByLecturer(lectureProfile);
        }
    
        // Thêm danh sách lớp vào model để hiển thị trên giao diện
        model.addAttribute("uniClasses", uniClasses);
    
        // Thêm danh sách học kỳ (nếu cần) để hiển thị trên giao diện lọc
        List<String> semesters = uniClassService.getAllSemesters(); // Phương thức lấy tất cả các học kỳ
        model.addAttribute("semesters", semesters);
    
        return "uniClassList"; // Trả về trang hiển thị danh sách lớp
    }
    
}


