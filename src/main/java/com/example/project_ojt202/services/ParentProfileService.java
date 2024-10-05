package com.example.project_ojt202.services;

import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.ParentProfile;
import com.example.project_ojt202.repositories.ParentProfileRepository;

@Service
public class ParentProfileService {
    private final ParentProfileRepository parentProfileRepository;

    public ParentProfileService (ParentProfileRepository parentProfileRepository){
        this.parentProfileRepository = parentProfileRepository;
    }

    public ParentProfile getParentProfileByParentID(String parentID){
        ParentProfile parent = parentProfileRepository.findByParentID(parentID);
        return parent;
    }
}
