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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpSession;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    Dotenv dotenv = Dotenv.load();
    Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

    @GetMapping("/notifications")
    public String listNotifications(Model model, HttpSession session) {
        List<Notification> notifications = notificationService.findAllByDateDesc();
        Collections.reverse(notifications);
        session.setAttribute("notifications", notifications);
    
        LocalDate today = LocalDate.now();
        List<Notification> todayNotifications = notifications.stream()
                .filter(notification -> notification.getNotificationDate().isEqual(today))
                .toList();
    
        session.setAttribute("todayNotifications", todayNotifications); // Lưu thông báo hôm nay vào session
    
        model.addAttribute("notifications", notifications);
        model.addAttribute("todayNotifications", todayNotifications); // Danh sách thông báo hôm nay
        return "a_createNotification"; // Chỉ định view cho homeAdmin
    }
    

    @GetMapping("/notifications/list")
    @ResponseBody
    public List<Notification> getNotifications(HttpSession session) {
       
        List<Notification> notifications = (List<Notification>) session.getAttribute("notifications");
        if (notifications == null) {
            notifications = notificationService.findAllByNewestFirst();
            session.setAttribute("notifications", notifications);
        }
        return notifications;
    }

    @PostMapping("/notifications/create")
    @ResponseBody
    public String createNotification(@ModelAttribute Notification notification,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     @RequestParam(value = "notificationImage", required = false) String notificationImage,
                                     HttpSession session) {
        try {
            // Tải lên file khác nếu có
            if (file != null && !file.isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                notification.setNotificationFile((String) uploadResult.get("secure_url"));
            }

            // Sử dụng URL hình ảnh từ widget nếu có
            if (notificationImage != null && !notificationImage.isEmpty()) {
                notification.setNotificationImage(notificationImage);
            }

            // Thiết lập ngày thông báo
            notification.setNotificationDate(LocalDate.now());
            notificationService.save(notification);

            // Cập nhật session với thông báo mới
            List<Notification> notifications = notificationService.findAll();
            session.setAttribute("notifications", notifications);

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
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     @RequestParam(value = "notificationImage", required = false) String notificationImage,
                                     HttpSession session) {
        try {
            // Tải lên file khác nếu có
            if (file != null && !file.isEmpty()) {
                
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                notification.setNotificationFile((String) uploadResult.get("secure_url"));
            }

            // Sử dụng URL hình ảnh từ widget nếu có
            if (notificationImage != null && !notificationImage.isEmpty()) {
                notification.setNotificationImage(notificationImage);
            }

            // Đảm bảo ngày thông báo tồn tại
            if (notification.getNotificationDate() == null) {
                notification.setNotificationDate(LocalDate.now());
            }

            // Thiết lập ID để cập nhật thông báo
            notification.setNotificationID(id);
            notificationService.save(notification);

            // Cập nhật session với thông báo mới
            List<Notification> notifications = notificationService.findAll();
            session.setAttribute("notifications", notifications);

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @GetMapping("/notifications/delete/{id}")
    @ResponseBody
    public String deleteNotification(@PathVariable Long id, HttpSession session) {
        notificationService.deleteById(id);

        // Cập nhật session sau khi xóa thông báo
        List<Notification> notifications = notificationService.findAll();
        session.setAttribute("notifications", notifications);

        return "success";
    }

}
