package com.example.project_ojt202.services;
import com.example.project_ojt202.models.StudentFeedback;
import com.example.project_ojt202.repositories.StudentFeedbackRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentFeedbackService {

    @Autowired
    private StudentFeedbackRepository studentFeedbackRepository;
    public StudentFeedbackService (StudentFeedbackRepository studentFeedbackRepository){
        this.studentFeedbackRepository = studentFeedbackRepository;
    }

    public StudentFeedback saveFeedback(StudentFeedback feedback) {
        return studentFeedbackRepository.save(feedback);
    }
    public boolean hasFeedbackForClass(String studentID, Long uniClassId) {
        return studentFeedbackRepository.existsByStudentProfile_StudentIDAndUniClass_UniClassId(studentID, uniClassId);
    }
    public List<StudentFeedback> getFeedbackForClassAndStudent(String studentID, Long uniClassId) {
        return studentFeedbackRepository.findByStudentProfile_StudentIDAndUniClass_UniClassId(studentID, uniClassId);
    }
    public List<StudentFeedback> getFeedbackByUniClassId(Long uniClassId) {
        return studentFeedbackRepository.findByUniClass_UniClassId(uniClassId);
    }
    public List<StudentFeedback> getFeedbackByClassId(Long classId) {
        return studentFeedbackRepository.findFeedbackByClassId(classId);
    }
}
