package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.Major;

public interface MajorRepository extends JpaRepository<Major, String>{
    Major findByMajorID(String majorID);
}
