package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.UniClass;

@Repository
public interface UniClassRepository extends JpaRepository<UniClass, Long> {
    List<UniClass> findBySubject_subjectID(String subjectID);

    List<UniClass> findBySemesterAndSubject_subjectID(String semester, String subjectID);

    // Linh
    List<UniClass> findByLectureProfileLectureID(String lectureID);

    List<UniClass> findByLectureProfile_LectureID(String lectureID);

    List<UniClass> findByLectureProfile_LectureIDAndSemester(String lectureID, String semester);

    int deleteBySubject_SubjectID(String subjectID);

    List<UniClass> findBySemester(String semester);

    @Query("SELECT DISTINCT u.semester FROM UniClass u")
    List<String> findDistinctSemesters();

    //H.Anh
    // Tìm các UniClass dựa trên danh sách Subject
    List<UniClass> findBySubjectIn(List<Subject> subjects);
}
