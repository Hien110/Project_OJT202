package com.example.project_ojt202.services;

import org.springframework.stereotype.Service;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.repositories.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account getAccountByAccountIDAndPassword(String accountID, String accountPassword) {
        Account account = accountRepository.findByAccountID(accountID);
        if (account != null && account.getAccountPassword().equals(accountPassword)) {
            return account;
        }
        return null; // Trả về null nếu không tìm thấy tài khoản hoặc mật khẩu không đúng
    }
     
}
