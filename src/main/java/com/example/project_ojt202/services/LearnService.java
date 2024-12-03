package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.AvarageScore;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.repositories.LearnRepository;
import java.util.Optional;

@Service
public class LearnService {

    @Autowired
    private LearnRepository learnRepository;

    public List<Learn> getLearnByUniClass(Long uniClassID) {
        List<Learn> learn = learnRepository.findByUniClass_UniClassId(uniClassID);
        return learn;
    }

    public List<Learn> getLearnByStudentID(String studentID) {
        List<Learn> learns = learnRepository.findByStudentProfile_StudentID(studentID);
        return learns;
    }

    public List<Learn> getAllLearns() {
        return learnRepository.findAll();
    }

    // Hàm tìm phần tử có ternNo lớn nhất
    public Learn findLearnWithMaxTernNo(List<Learn> learns) {
        Learn maxLearn = null;
        int maxTernNo = Integer.MIN_VALUE; // Khởi tạo giá trị ternNo lớn nhất ban đầu

        for (Learn learn : learns) {
            // Kiểm tra nếu learn.uniClass và learn.uniClass.subject khác null
            if (learn.getUniClass() != null && learn.getUniClass().getSubject() != null) {
                int ternNo = learn.getUniClass().getSubject().getTernNo();
                // Nếu ternNo của phần tử hiện tại lớn hơn maxTernNo, cập nhật
                if (ternNo > maxTernNo) {
                    maxTernNo = ternNo;
                    maxLearn = learn;
                }
            }
        }

        return maxLearn; // Trả về phần tử có ternNo lớn nhất
    }

    public void saveDataToDatabase(Learn learn) {
        learnRepository.save(learn);
    }

    public Learn getLearnByUniClassAndStudent(Long uniClassId, String studentID) {
        return learnRepository.findByUniClassAndStudent(uniClassId, studentID);
    }
    
    
}
