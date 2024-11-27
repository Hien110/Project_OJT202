package com.example.project_ojt202.controllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import com.example.project_ojt202.services.StudentProfileService;
import com.example.project_ojt202.models.StudentProfile;

@Controller
public class AccountController {
    
    private final StudentProfileService studentProfileService;

    public AccountController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }
    @GetMapping("/studentProfile/{studentID}")
    public String accountInfo(@PathVariable("studentID") String studentID, Model model) {
        // Lấy thông tin profile dựa trên studentID
        StudentProfile studentProfile = studentProfileService.getStudentProfileByStudentID(studentID);
        System.out.println("Student Profile: " + studentProfile); // Log để kiểm tra studentProfile
    
        if (studentProfile == null) {
            return "errorPage"; // Nếu không tìm thấy profile, có thể chuyển đến trang lỗi
        }
    
        model.addAttribute("studentProfile", studentProfile);
        return "studentProfile"; // Trả về tên của trang HTML
    }
    // Phương thức xử lý upload ảnh đại diện
    @PostMapping("/studentProfile/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar, 
                               @RequestParam("studentID") String studentID) {
        try {
            // Lưu ảnh vào thư mục
            String avatarPath = saveAvatarFile(avatar, studentID);

            // Cập nhật avatar vào database
            studentProfileService.updateAvatar(studentID, avatarPath);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/studentProfile?error=uploadFailed";
        }
        return "redirect:/studentProfile?success=avatarUpdated";
    }

    // Phương thức lưu ảnh vào thư mục
    private String saveAvatarFile(MultipartFile file, String studentID) throws IOException {
        String fileName = studentID + "_" + file.getOriginalFilename();
        String uploadDir = "uploads/avatars/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/avatars/" + fileName; // Trả về đường dẫn lưu ảnh
    }
    
    
    @GetMapping("/resetpassword")
    public String resetpassword() {
        return "resetpassword"; // Xác nhận rằng resetpassword.html tồn tại trong /templates
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login"; // Điều hướng đến trang login sau khi logout
    }
    
}