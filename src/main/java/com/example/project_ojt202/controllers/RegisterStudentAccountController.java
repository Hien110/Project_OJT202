package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.services.RegisterStudentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
public class RegisterStudentAccountController {

    @Autowired
    private RegisterStudentAccountService excelService;

    // Display upload page
    @GetMapping("/uploadFileStudentAccount")
    public String index() {
        return "uploadFileStudentAccount"; // Render the upload page without any data initially
    }

    // Handle file upload and process Excel file
    @PostMapping("/uploadFileStudentAccountDetail")
    public String uploadExcel(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Process the uploaded file and extract the data
            List<StudentProfile> studentProfiles = excelService.processExcelFile(file);

            // Add the list of students to the model to display on the page
            model.addAttribute("studentProfiles", studentProfiles);

            // Add a flag to indicate that the file was uploaded successfully
            model.addAttribute("fileUploaded", true);
        } catch (IOException e) {
            model.addAttribute("uploadError", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("uploadError", e.getMessage());
        }

        // Render the same page with the uploaded data displayed
        return "uploadFileStudentAccount"; // The same Thymeleaf template is reused
    }

    // Save uploaded data to the database
    @PostMapping("/submitUploadStudentAccount")
    @ResponseBody
    public String submitData() {
        excelService.saveDataToDatabase();
        return "Dữ liệu đã được lưu thành công!";
    }
}
