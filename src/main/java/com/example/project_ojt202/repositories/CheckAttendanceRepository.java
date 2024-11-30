package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project_ojt202.models.CheckAttendance;
import com.example.project_ojt202.models.Scheduce;

public interface CheckAttendanceRepository extends JpaRepository<CheckAttendance, Long> {
    // Additional custom queries can go here if needed
    List<CheckAttendance> findByScheduce(Scheduce scheduce);
     @Modifying
    @Query("UPDATE CheckAttendance ca " +
           "SET ca.checkAttendance = :checkAttendance " +
           "WHERE ca.scheduce.scheduceID = :scheduceID AND ca.studentProfile.studentID = :studentID")
    int updateCheckAttendance(@Param("checkAttendance") boolean checkAttendance, 
                              @Param("scheduceID") Long scheduceID, 
                              @Param("studentID") String studentID);

       CheckAttendance findByScheduce_scheduceIDAndStudentProfile_studentID(Long ScheduceID , String studentID);
}
