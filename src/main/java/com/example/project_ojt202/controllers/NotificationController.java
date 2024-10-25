package com.example.project_ojt202.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project_ojt202.models.Notification;
import com.example.project_ojt202.services.NotificationService;

@Controller
@RequestMapping("/") // Đường dẫn chính
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/homeAdmin")
    public String getHomeAdmin(Model model) {
        List<Notification> notifications = notificationService.getAllNotifications();
        model.addAttribute("notifications", notifications);
        return "homeAdmin"; // Trả về trang homeAdmin
    }

    @GetMapping("/notifications/create")
    public String createNotificationForm(Model model) {
        model.addAttribute("notification", new Notification());
        return "createNotification"; // Trả về trang tạo thông báo
    }

    @PostMapping("/notifications")
    public String createNotification(@ModelAttribute Notification notification) {
        notificationService.createNotification(notification);
        return "redirect:/admin/homeAdmin"; // Quay lại trang homeAdmin
    }

    @GetMapping("/notifications/edit/{id}")
    public String editNotificationForm(@PathVariable Long id, Model model) {
        Notification notification = notificationService.getNotificationById(id);
        model.addAttribute("notification", notification);
        return "editNotification"; // Trả về trang chỉnh sửa thông báo
    }

    @PostMapping("/notifications/{id}")
    public String updateNotification(@PathVariable Long id, @ModelAttribute Notification notification) {
        notificationService.updateNotification(id, notification);
        return "redirect:/admin/homeAdmin"; // Quay lại trang homeAdmin
    }

    @GetMapping("/notifications/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/admin/homeAdmin"; // Quay lại trang homeAdmin
    }
}