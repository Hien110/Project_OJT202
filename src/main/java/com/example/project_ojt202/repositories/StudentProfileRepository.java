package com.example.project_ojt202.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project_ojt202.models.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, String> {

    // Tìm kiếm thông tin sinh viên qua studentID
    StudentProfile findByStudentID(String studentID);

    // Giả sử bạn có cách lấy ra thông tin người dùng hiện tại từ session hoặc token
    // Phương thức này có thể được điều chỉnh tùy theo cách bạn quản lý phiên người dùng
    StudentProfile findCurrentUser();  // Hàm này có thể được thay thế bằng logic phù hợp
}
