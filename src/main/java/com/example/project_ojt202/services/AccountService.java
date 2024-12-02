package com.example.project_ojt202.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.repositories.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    // Constructor để inject AccountRepository vào service
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Lấy thông tin tài khoản bằng accountID và mật khẩu
    public Account getAccountByAccountIDAndPassword(String accountID, String accountPassword) {
        Account account = accountRepository.findByAccountID(accountID);
        if (account != null && account.getAccountPassword().equals(accountPassword)) {
            return account;
        }
        return null; // Trả về null nếu không tìm thấy tài khoản hoặc mật khẩu không đúng
    }


    @Transactional
    public boolean changePassword(String accountID, String oldPassword, String newPassword) {
        Account account = accountRepository.findByAccountID(accountID);
        if (account != null && account.getAccountPassword().equals(oldPassword)) {
            account.setAccountPassword(newPassword); // Cập nhật mật khẩu mới
            accountRepository.save(account); // Lưu tài khoản với mật khẩu mới
            accountRepository.flush();  // Đảm bảo thay đổi được commit vào database ngay lập tức
            return true;
        }
        return false;
    }    

public void updatePassword(String accountID, String newPassword) {
    Account account = accountRepository.findById(accountID).orElseThrow(() -> new RuntimeException("Account not found"));
    account.setAccountPassword(newPassword);  // Đặt mật khẩu mới
    accountRepository.save(account);  // Lưu lại vào database
}

    public Account getAccountByID(String accountID){
        return accountRepository.findById(accountID).orElse(null);
    }

    public List<Account> getAllAccount(){
        return accountRepository.findAll();
    }
}
