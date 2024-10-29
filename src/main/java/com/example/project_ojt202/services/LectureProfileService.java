package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.repositories.LectureProfileRepository;

@Service
public class LectureProfileService {
    private final LectureProfileRepository lectureProfileRepository;

    public LectureProfileService (LectureProfileRepository lectureProfileRepository){
        this.lectureProfileRepository = lectureProfileRepository;
    }

    public LectureProfile getLecProfileByLectureID(String lectureID){
        LectureProfile lecture = lectureProfileRepository.findByLectureID(lectureID);
        return lecture;
    }

    public List<LectureProfile> getLecProfileByMajorID(String majorID){
        List<LectureProfile> majLectureProfile = lectureProfileRepository.findByMajor_majorID(majorID);
        return majLectureProfile;
    }
}
