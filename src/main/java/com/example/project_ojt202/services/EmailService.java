package com.example.project_ojt202.services;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.repositories.AccountRepository;
import com.example.project_ojt202.repositories.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void sendEmails() {
        List<StudentProfile> students = studentProfileRepository.findAll();

        for (StudentProfile student : students) {
            Account account = accountRepository.findByStudentProfile(student);

            if (account != null) {
                String subject = "Thông Tin Tài Khoản Của Bạn";
                String message = String.format("Xin chào %s %s,\n" +
                        "Thông tin tài khoản của bạn như sau:\n" +
                        "Mã Tài Khoản: %s\n" +
                        "Mật Khẩu Tài Khoản: %s\n" +
                        "Mã Tài Khoản Phụ Huynh: %s" + "PH\n" +
                        "Mật Khẩu Tài Khoản: %s\n\n" +
                        "Trân trọng,\nNhóm của bạn.",
                        student.getFirstName(), student.getLastName(),
                        account.getAccountID(), account.getAccountPassword(), account.getAccountID(),
                        account.getAccountPassword());

                sendEmail(student.getEmail(), subject, message);
            }
        }
    }

    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

}
