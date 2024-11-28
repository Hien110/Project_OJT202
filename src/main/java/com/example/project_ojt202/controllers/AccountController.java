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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import com.example.project_ojt202.services.StudentProfileService;

import jakarta.servlet.http.HttpSession;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.services.AccountService;

@Controller
public class AccountController {

    private final StudentProfileService studentProfileService;
    private final AccountService accountService;

    public AccountController(StudentProfileService studentProfileService, AccountService accountService) {
        this.studentProfileService = studentProfileService;
        this.accountService = accountService;
    }

    // Hiển thị thông tin profile sinh viên
    @GetMapping("/studentProfile/{studentID}")
    public String accountInfo(@PathVariable("studentID") String studentID, Model model) {
        StudentProfile studentProfile = studentProfileService.getStudentProfileByStudentID(studentID);

        if (studentProfile == null) {
            model.addAttribute("error", "Không tìm thấy thông tin sinh viên.");
            return "errorPage";
        }

        model.addAttribute("studentProfile", studentProfile);
        return "studentProfile";
    }

    // Xử lý upload ảnh đại diện
    @PostMapping("/studentProfile/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar,
                               @RequestParam("studentID") String studentID,
                               RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra tập tin upload
            if (avatar.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn một ảnh.");
                return "redirect:/studentProfile/" + studentID;
            }
            if (!avatar.getContentType().startsWith("image/")) {
                redirectAttributes.addFlashAttribute("error", "Tập tin phải là ảnh.");
                return "redirect:/studentProfile/" + studentID;
            }

            // Lưu ảnh vào thư mục
            String avatarPath = saveAvatarFile(avatar, studentID);

            // Cập nhật avatar trong cơ sở dữ liệu
            studentProfileService.updateAvatar(studentID, avatarPath);
            redirectAttributes.addFlashAttribute("message", "Cập nhật ảnh đại diện thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Không thể cập nhật ảnh đại diện.");
        }
        return "redirect:/studentProfile/" + studentID;
    }

    // Lưu ảnh vào thư mục
    private String saveAvatarFile(MultipartFile file, String studentID) throws IOException {
        String fileName = studentID + "_" + file.getOriginalFilename();
        String uploadDir = "uploads/avatars/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/avatars/" + fileName;
    }

    // Hiển thị trang reset mật khẩu
    @GetMapping("/resetpassword")
    public String showResetPasswordPage() {
        return "resetpassword";
    }

    // Đổi mật khẩu
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Account account = (Account) session.getAttribute("account");
        if (account == null ) {
            redirectAttributes.addFlashAttribute("error", "Phiên làm việc đã hết hạn, vui lòng đăng nhập lại.");
            return "redirect:/login";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "redirect:/resetpassword";
        }

        boolean isPasswordChanged = accountService.changePassword(account.getAccountID(), oldPassword, newPassword);
        if (isPasswordChanged) {
            redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công!");
            return "redirect:/home";
        } else {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không chính xác.");
            return "redirect:/resetpassword";
        }
       
    }

    // Xử lý logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa session hiện tại
        return "redirect:/login";
    }
}
