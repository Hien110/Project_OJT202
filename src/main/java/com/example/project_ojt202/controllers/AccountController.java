package com.example.project_ojt202.controllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.nio.file.Path;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.models.LectureProfile;
import java.nio.file.Files;
import com.example.project_ojt202.services.StudentProfileService;
import com.example.project_ojt202.services.ParentProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.services.AccountService;




@Controller
public class AccountController {

    private final StudentProfileService studentProfileService;
    private final AccountService accountService;
    private final LectureProfileService lectureProfileService;
    private final ParentProfileService parentProfileService;

    @Autowired
    private MailSender mailSender;

    public AccountController(StudentProfileService studentProfileService, AccountService accountService, LectureProfileService lectureProfileService, ParentProfileService parentProfileService) {
        this.studentProfileService = studentProfileService;
        this.accountService = accountService;
        this.lectureProfileService = lectureProfileService;
        this.parentProfileService = parentProfileService;
    }

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

    @GetMapping("/lectureProfile/{lectureID}")
    public String LectureAccountInfo(@PathVariable("lectureID") String lectureID, Model model) {
        LectureProfile lectureProfile = lectureProfileService.getLecProfileByLectureID(lectureID);


        if (lectureProfile == null) {
            model.addAttribute("error", "Không tìm thấy thông tin giảng viên.");
            return "errorPage";
        }

        model.addAttribute("lectureProfile", lectureProfile);
        return "lectureProfile"; 
    }

    @GetMapping("/adminProfile")
    public String showAdminProfilePage(HttpSession session, Model model) {
    Account account = (Account) session.getAttribute("account");

    model.addAttribute("account", account);

    return "adminProfile";
}
    
    


    @PostMapping("/studentProfile/uploadAvatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(@RequestParam("avatar") MultipartFile avatar,
                                                            @RequestParam("studentID") String studentID) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (avatar.isEmpty()) {
                response.put("success", false);
                response.put("message", "Vui lòng chọn một ảnh.");
                return ResponseEntity.badRequest().body(response);
            }
            if (!avatar.getContentType().startsWith("image/")) {
                response.put("success", false);
                response.put("message", "Tập tin phải là ảnh.");
                return ResponseEntity.badRequest().body(response);
            }
    
            String avatarPath = saveAvatarFile(avatar, studentID);
            studentProfileService.updateAvatar(studentID, avatarPath);
    
            response.put("success", true);
            response.put("message", "Cập nhật ảnh thành công!");
            return ResponseEntity.ok(response);
    
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Không thể cập nhật ảnh đại diện.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    


    private String saveAvatarFile(MultipartFile avatar, String studentID) throws IOException {

        String fileName = avatar.getOriginalFilename();
    
   
        String uploadDirPath = Paths.get("uploads").toAbsolutePath().toString(); 
        Path uploadDir = Paths.get(uploadDirPath);
    
 
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    

        Path filePath = uploadDir.resolve(fileName);
    

        avatar.transferTo(filePath.toFile());
    

        return "/uploads/" + fileName;
    }    


    @GetMapping("/resetpassword")
    public String showResetPasswordPage() {
        return "resetpassword";
    }



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
        session.invalidate(); 
        return "redirect:/login";
    }

    @GetMapping("/forgotPassword")
public String showForgotPasswordPage() {
    return "forgotPassword"; 
}

    @GetMapping("/codeSent")
    public String showCodeSentPage(Model model) {
        model.addAttribute("message", "Mã xác nhận đã được gửi đến email của bạn. Vui lòng kiểm tra.");
        return "codeSent"; 
    }



    @GetMapping("/changePasswordafterVertify")
    public String showChangePasswordPage() {
        return "changePasswordafterVertify";
    }

    @PostMapping("/changePasswordafterVertify")
    public String changePasswordafterVertify(@RequestParam("newPassword") String newPassword,
                                             @RequestParam("confirmPassword") String confirmPassword,
                                             HttpSession session,
                                             RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "redirect:/changePasswordafterVertify"; 
        }
    
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại.");
            return "redirect:/login";
        }

        try {
            accountService.updatePassword(account.getAccountID(), newPassword); 
            redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được thay đổi thành công.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Không thể đổi mật khẩu: " + e.getMessage());
            return "redirect:/changePasswordafterVertify";
        }
    
        return "redirect:/login";
    }    

    @PostMapping("/send-reset-code")
    public String sendResetCode(@RequestParam("email") String email) {
       
        String resetCode = generateResetCode(); 
      
        sendResetCodeToEmail(email, resetCode); 
        
        return "forgotPassword"; 
    }

    private String generateResetCode() {
        
        return UUID.randomUUID().toString().substring(0, 6); 
    }

     private void sendResetCodeToEmail(String email, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã xác nhận quên mật khẩu");
        message.setText("Mã xác nhận của bạn là: " + resetCode);

        mailSender.send(message);  
    }


    @PostMapping("/verifyResetCode")
    public String verifyResetCode(@RequestParam("resetCode") String resetCode, 
                                  HttpSession session, 
                                  RedirectAttributes redirectAttributes) {
        
        String storedResetCode = (String) session.getAttribute("resetCode");
        if (storedResetCode != null && storedResetCode.equals(resetCode)) {
            redirectAttributes.addFlashAttribute("message", "Mã xác nhận chính xác. Vui lòng nhập mật khẩu mới.");
            return "redirect:/changePasswordafterVertify";
        } else {
            redirectAttributes.addFlashAttribute("error", "Mã xác nhận không chính xác.");
            return "redirect:/codeSent"; 
        }
    }
    

    @PostMapping("/send-reset-code-session")
    public String sendResetCodeWithSession(@RequestParam("email") String email, 
                                           HttpSession session, 
                                           RedirectAttributes redirectAttributes) {
        try {
            String resetCode = generateResetCode();
            sendResetCodeToEmail(email, resetCode);
            session.setAttribute("resetCode", resetCode);
            redirectAttributes.addFlashAttribute("message", "Mã xác nhận đã được gửi đến email của bạn.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể gửi mã xác nhận. Vui lòng thử lại.");
        }
        return "redirect:/codeSent"; 
    }
    
}