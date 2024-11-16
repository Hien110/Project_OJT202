package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.repositories.LearnRepository;

@Service
public class LearnService {

    @Autowired
    private LearnRepository learnRepository;

    public List<Learn> getLearnByUniClass(Long uniClassID) {
        List<Learn> learn = learnRepository.findByUniClass_UniClassId(uniClassID);
        return learn;
    }
}
