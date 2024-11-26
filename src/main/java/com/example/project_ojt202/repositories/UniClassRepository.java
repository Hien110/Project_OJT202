package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project_ojt202.models.UniClass;

@Repository
public interface UniClassRepository extends JpaRepository<UniClass, Long>{
    List<UniClass> findBySubject_subjectID(String subjectID);
    List<UniClass> findBySemesterAndSubject_subjectID(String semester, String subjectID);


    // Linh
    List<UniClass> findByLectureProfileLectureID(String lectureID);

    List<UniClass> findByLectureProfile_LectureID(String lectureID);
    List<UniClass> findByLectureProfile_LectureIDAndSemester(String lectureID, String semester);
    int deleteBySubject_SubjectID(String subjectID);
}
