package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project_ojt202.models.AvarageScore;
import com.example.project_ojt202.models.Learn;
import java.util.Optional;

public interface LearnRepository extends JpaRepository<Learn, Long> {
    List<Learn> findByUniClass_UniClassId(Long uniClassId);

    List<Learn> findByStudentProfile_StudentID(String studentID);

    @Query("SELECT l FROM Learn l WHERE l.uniClass.uniClassId = :uniClassId AND l.studentProfile.studentID = :studentID")
    Learn findByUniClassAndStudent(@Param("uniClassId") Long uniClassId, @Param("studentID") String studentID);

}