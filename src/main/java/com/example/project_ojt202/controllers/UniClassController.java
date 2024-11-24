package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.StudentFeedback;

import com.example.project_ojt202.models.Feedback;
import com.example.project_ojt202.models.FeedbackChoice;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.services.StudentFeedbackService;
import com.example.project_ojt202.services.UniClassService;
import com.example.project_ojt202.services.FeedbackChoiceService;
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
    
    // Hiển thị danh sách lớp học
    @GetMapping("/uniClasses")
public String showUniClasses(Model model) {
    // Lấy danh sách tất cả lớp học
    List<UniClass> uniClasses = uniClassService.getAllUniClasses();

    // Thêm thông tin về khả năng hiển thị nút phản hồi
    Map<Long, Boolean> feedbackAvailabilityMap = uniClasses.stream().collect(Collectors.toMap(
        UniClass::getUniClassId, // Đảm bảo `getUniClassId` không trả về null
        uniClass -> {
            if (uniClass.getDateEndLearn() == null) {
                return false; // Không có ngày kết thúc
            }
            LocalDate now = LocalDate.now();
            LocalDate startFeedbackDate = uniClass.getDateEndLearn().minusDays(14);
            return (now.isAfter(startFeedbackDate) || now.isEqual(startFeedbackDate))
                    && (now.isBefore(uniClass.getDateEndLearn()) || now.isEqual(uniClass.getDateEndLearn()));
        }
    ));
    model.addAttribute("feedbackAvailabilityMap", feedbackAvailabilityMap);
    

    model.addAttribute("uniClasses", uniClasses);
    model.addAttribute("feedbackAvailabilityMap", feedbackAvailabilityMap);
    System.out.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + feedbackAvailabilityMap);
    return "uniClassList"; // Tên file HTML
}


    // Hiển thị form feedback cho một lớp
    @GetMapping("/uniClasses/feedback")
    public String showFeedbackForm(@RequestParam("id") Long uniClassId,
                                   @ModelAttribute("profileAccount") StudentProfile studentProfile,
                                   Model model) {
    
        // Kiểm tra lớp học có tồn tại hay không
        UniClass uniClass = uniClassService.getUniClassById(uniClassId);
        if (uniClass == null) {
            model.addAttribute("error", "Lớp học không tồn tại.");
            return "errorPage";
        }

        // Kiểm tra nếu học sinh đã gửi feedback
        boolean hasFeedback = studentFeedbackService.hasFeedbackForClass(studentProfile.getStudentID(), uniClassId);
        if (hasFeedback) {
            model.addAttribute("error", "Bạn đã gửi feedback cho lớp học này rồi.");
            List<UniClass> uniClasses = uniClassService.getAllUniClasses();
            model.addAttribute("uniClasses", uniClasses);
            return "uniClassList"; // Trả về danh sách lớp học cùng với thông báo lỗi
        }
    
        // Nếu đủ điều kiện, hiển thị form feedback
        List<FeedbackChoice> feedbackChoices = feedbackChoiceService.getAllFeedbackChoices();
        Map<Feedback, List<FeedbackChoice>> feedbackChoicesByQuestion = feedbackChoices.stream()
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
            Model model) {
    
        if (studentProfile == null) {
            return "redirect:/login";
        }
    
        // Kiểm tra lớp học có tồn tại hay không
        UniClass uniClass = uniClassService.getUniClassById(uniClassId);
        if (uniClass == null) {
            model.addAttribute("error", "UniClass không tồn tại.");
            return "uniClassList";
        }
        
        // Kiểm tra nếu học sinh đã feedback cho lớp học này
        boolean hasFeedback = studentFeedbackService.hasFeedbackForClass(studentProfile.getStudentID(), uniClassId);
        if (hasFeedback) {
            model.addAttribute("error", "Bạn đã gửi feedback cho lớp học này rồi.");
            return "uniClassList";
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
    
        return "redirect:/uniClasses";
    }
}


