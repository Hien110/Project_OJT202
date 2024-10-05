package com.example.project_ojt202.services;

import com.example.project_ojt202.models.ParentProfile;
import com.example.project_ojt202.repositories.ParentProfileRepository;

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
