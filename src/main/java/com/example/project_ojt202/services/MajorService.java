package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.Major;
import com.example.project_ojt202.repositories.MajorRepository;


@Service
public class MajorService {
    @Autowired
    private MajorRepository majorRepository;

    public  List<Major> findAllMajor() {
        List<Major> majors = majorRepository.findAll();
        return majors;
    };
    
}
