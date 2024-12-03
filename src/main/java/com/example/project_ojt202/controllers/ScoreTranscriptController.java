package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.services.ScoreTranscriptService;

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
public class ScoreTranscriptController {

    @Autowired
    private ScoreTranscriptService excelService;

    // Display upload page
    @GetMapping("/uploadFileScoreTranscript")
    public String index() {
        return "uploadFileScoreTranscript"; // Render upload page
    }

    // Handle file upload and process Excel file
    @PostMapping("/uploadFileScoreTranscriptDetail")
    public String uploadExcel(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Process uploaded file
            List<ScoreTranscript> scoreTranscripts = excelService.processExcelFile(file);
            model.addAttribute("scoreTranscripts", scoreTranscripts);
            model.addAttribute("fileUploaded", true);
        } catch (IOException e) {
            model.addAttribute("uploadError", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("uploadError", e.getMessage());
        }

        return "uploadFileScoreTranscript"; // Render the same page
    }

    // Save data to database
    @PostMapping("/submitUploadScoreTranscript")
    @ResponseBody
    public String submitData() {
        excelService.saveDataToDatabase();
        return "Dữ liệu đã được lưu thành công!";
    }

    @GetMapping("/viewScoreTranscript")
    public String viewScoreTranscript() {
    return "viewscoretranscript";
}
}
