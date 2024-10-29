package com.example.project_ojt202.controllers;

import com.example.project_ojt202.models.Notification;
import com.example.project_ojt202.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/homeAdmin")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    private final String UPLOAD_DIR = "uploads/"; // Đường dẫn thư mục lưu trữ file

    @GetMapping("/notifications")
    public String listNotifications(Model model) {
        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);
        return "admin/homeAdmin"; // Chỉ định view cho homeAdmin
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
                                      @RequestParam("image") MultipartFile image) {
        try {
            // Lưu file và image vào thư mục
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.write(path, bytes);
                notification.setNotificationFile(file.getOriginalFilename());
            }

            if (!image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOAD_DIR + image.getOriginalFilename());
                Files.write(path, bytes);
                notification.setNotificationImage(image.getOriginalFilename());
            }

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
                                      @RequestParam("image") MultipartFile image) {
        try {
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.write(path, bytes);
                notification.setNotificationFile(file.getOriginalFilename());
            }

            if (!image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOAD_DIR + image.getOriginalFilename());
                Files.write(path, bytes);
                notification.setNotificationImage(image.getOriginalFilename());
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
}