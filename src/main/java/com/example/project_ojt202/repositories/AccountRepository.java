package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.Account;

public interface AccountRepository extends JpaRepository<Account, String>{
    Account findByAccountID (String accountID);
}
