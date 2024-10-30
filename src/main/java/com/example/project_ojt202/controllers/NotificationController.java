package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.Notification;
import com.example.project_ojt202.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

@Controller

public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    Dotenv dotenv = Dotenv.load();
    Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

    @GetMapping("/notifications")
    public String listNotifications(Model model) {
        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);
        return "a_createNotification"; // Chỉ định view cho homeAdmin
    }

    @GetMapping("/notifications/list")
    @ResponseBody
    public List<Notification> getNotifications() {
        return notificationService.findAll();
    }

    @PostMapping("/notifications/create")
    @ResponseBody
    public String createNotification(@ModelAttribute Notification notification,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("notificationImage") String notificationImage) {
        try {
            // Tải lên file nếu có
            if (!file.isEmpty()) {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                notification.setNotificationFile((String) uploadResult.get("secure_url"));
            }

            // Sử dụng URL hình ảnh từ form
            notification.setNotificationImage(notificationImage);
            notification.setNotificationDate(LocalDate.now());
            notificationService.save(notification);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @PostMapping("/notifications/edit/{id}")
    @ResponseBody
    public String updateNotification(@PathVariable Long id,
                                      @ModelAttribute Notification notification,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("notificationImage") String notificationImage) {
        try {
            if (!file.isEmpty()) {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                notification.setNotificationFile((String) uploadResult.get("secure_url"));
            }

            // Sử dụng URL hình ảnh từ form
            notification.setNotificationImage(notificationImage);

            if (notification.getNotificationDate() == null) {
                notification.setNotificationDate(LocalDate.now());
            }
            notification.setNotificationID(id);
            notificationService.save(notification);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @GetMapping("/notifications/delete/{id}")
    @ResponseBody
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteById(id);
        return "success";
    }

    @GetMapping("/notification")
    public String viewStudentNotifications(Model model) {
        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);
        return "homeStudent"; 
    }
}
