package com.example.project_ojt202.services;

import com.example.project_ojt202.models.PrerequisiteSubject;
import com.example.project_ojt202.repositories.PrerequisiteSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrerequisiteSubjectService {
    @Autowired
    private PrerequisiteSubjectRepository prerequisiteSubjectRepository;

    public List<PrerequisiteSubject> getPrerequisiteSubjectsBySubjectID(String subjectID) {
        return prerequisiteSubjectRepository.findBySubjectSubjectID(subjectID);
    }
}
