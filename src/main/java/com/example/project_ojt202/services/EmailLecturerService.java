package com.example.project_ojt202.services;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.repositories.AccountRepository;
import com.example.project_ojt202.repositories.LectureProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailLecturerService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LectureProfileRepository lecturetProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void sendEmails() {
        List<LectureProfile> lecturers = lecturetProfileRepository.findAll();

        for (LectureProfile lecturer : lecturers) {
            Account account = accountRepository.findByLectureProfile(lecturer);

            if (account != null) {
                String subject = "Thông Tin Tài Khoản Của Bạn";
                String message = String.format("Xin chào %s %s,\n" +
                        "Thông tin tài khoản của bạn như sau:\n" +
                        "Mã Tài Khoản: %s\n" +
                        "Mật Khẩu Tài Khoản: %s\n" +
                        "Trân trọng,\nNhóm của bạn.",
                        lecturer.getFirstName(), lecturer.getLastName(),
                        account.getAccountID(), account.getAccountPassword());

                sendEmail(lecturer.getEmail(), subject, message);
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
