package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.StudentProfile;

public interface AccountRepository extends JpaRepository<Account, String>{
    Account findByAccountID (String accountID);
    Account findByStudentProfile(StudentProfile studentProfile);
}
