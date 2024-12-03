package com.example.project_ojt202.controllers;

import com.example.project_ojt202.services.CloudinaryService;
import com.example.project_ojt202.services.LectureProfileService;
import com.example.project_ojt202.services.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private LectureProfileService lectureProfileService;
    @PostMapping("/uploadAvatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "studentID", required = false) String studentID,
            @RequestParam(value = "lectureID", required = false) String lectureID
            ) {
        Map<String, Object> response = new HashMap<>();
        try {
            String imageUrl = cloudinaryService.uploadImage(file);
            if (studentID != null && !studentID.isEmpty()) {
                studentProfileService.updateAvatar(studentID, imageUrl);
            } else {
                lectureProfileService.updateAvatar(lectureID, imageUrl);
            }
            response.put("success", true);
            response.put("imageUrl", imageUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Image upload failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
