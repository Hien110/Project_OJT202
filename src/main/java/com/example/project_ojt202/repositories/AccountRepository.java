package com.example.project_ojt202.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.StudentProfile;

public interface AccountRepository extends JpaRepository<Account, String>{
    Account findByAccountID (String accountID);
    Account findByStudentProfile(StudentProfile studentProfile);
    Account findByLectureProfile(LectureProfile lectureProfile);
    List<Account> findByAccountRole(String accountRole);
}
