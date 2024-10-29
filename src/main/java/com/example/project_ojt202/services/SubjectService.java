package com.example.project_ojt202.services;

import java.util.List;

import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.repositories.SubjectRepository;

import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService (SubjectRepository subjectRepository){
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getSubjectByMajorIDAndTernNo(String majorID, int ternNo){
        List<Subject> subjects = subjectRepository.findByMajor_MajorIDAndTernNo(majorID, ternNo);
        return subjects;
    }
}
