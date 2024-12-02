package com.example.project_ojt202.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project_ojt202.models.Feedback;
import com.example.project_ojt202.models.FeedbackChoice;
import com.example.project_ojt202.services.FeedbackService;
import com.example.project_ojt202.services.FeedbackChoiceService;

@Controller
@RequestMapping("/afeedBack")  // Cập nhật đường dẫn ở đây
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private FeedbackChoiceService feedbackChoiceService;


    @GetMapping
public String getAllFeedbacks(Model model) {
    List<Feedback> feedbacks = feedbackService.getAllFeedbacks(); // lấy tất cả câu hỏi từ service
    model.addAttribute("feedbacks", feedbacks); // gửi danh sách câu hỏi đến view
    return "afeedBack"; // tên view của bạn
}

    // Lưu câu hỏi mới
    @PostMapping("/save")
    public String saveFeedback(@ModelAttribute Feedback feedback) {
        // Đặt status mặc định là "show" khi lưu câu hỏi mới
        feedback.setStatus("show");
        feedbackService.saveFeedback(feedback);
        return "redirect:/afeedBack";
    }

    // Xóa câu hỏi
    @GetMapping("/delete/{id}")
public String deleteFeedback(@PathVariable Long id) {
    // Lấy câu hỏi theo ID
    Feedback feedback = feedbackService.getFeedbackById(id);
    if (feedback != null) {
        // Cập nhật status thành "hidden"
        feedback.setStatus("hidden");
        feedbackService.saveFeedback(feedback);  // Lưu lại thay đổi
    }
    return "redirect:/afeedBack";
}


    // Lấy danh sách các lựa chọn cho câu hỏi
    @GetMapping("/choices/{feedbackID}")
    @ResponseBody
    public List<FeedbackChoice> getChoicesByFeedbackId(@PathVariable Long feedbackID) {
        return feedbackChoiceService.getFeedbackChoicesByFeedbackId(feedbackID);
    }

    // Lưu lựa chọn cho câu hỏi
    @PostMapping("/choices/save")
    @ResponseBody
    public void saveChoice(@RequestParam Long feedbackID,
                       @RequestParam String feedback_choice_content,
                       @RequestParam String feedback_choice_note,
                       @RequestParam Integer feedback_choice_score
                       ) {
    FeedbackChoice choice = new FeedbackChoice();
    choice.setFeedback(new Feedback(feedbackID, feedback_choice_note));  
    choice.setFeedbackChoiceContent(feedback_choice_content);
    choice.setFeedbackChoiceNote(feedback_choice_note);
    choice.setScore(feedback_choice_score);
    feedbackChoiceService.saveFeedbackChoice(choice);
}


  @PostMapping("/choices/updateAll")
@ResponseBody
public String updateAllChoices(@RequestBody List<FeedbackChoice> choices) {
    feedbackChoiceService.updateDChoices(choices);
    return "success";
}

    // Xóa lựa chọn
    @DeleteMapping("/choices/delete/{choiceID}")
@ResponseBody
public String deleteChoice(@PathVariable Long choiceID) {
    feedbackChoiceService.deleteFeedbackChoice(choiceID);
    return "success";
}

    // Lưu tất cả các lựa chọn
@PostMapping("/saveAll")
public String saveAllChoices(@RequestParam Long feedbackID,
                              @RequestParam List<String> choiceContent,
                              @RequestParam List<String> choiceNote,@RequestParam List<Integer> choiceScore
                              ) {
    // Duyệt qua tất cả các lựa chọn và lưu
    for (int i = 0; i < choiceContent.size(); i++) {
        FeedbackChoice choice = new FeedbackChoice();
        choice.setFeedback(new Feedback (feedbackID, null));
        choice.setFeedbackChoiceContent(choiceContent.get(i));
        choice.setFeedbackChoiceNote(choiceNote.get(i));
        choice.setScore(choiceScore.get(i));
        feedbackChoiceService.saveFeedbackChoice(choice);
    }
    return "redirect:/afeedBack";  
}

}
