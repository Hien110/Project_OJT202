package com.example.project_ojt202.services;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Major;
import com.example.project_ojt202.models.ParentProfile;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.repositories.AccountRepository;
import com.example.project_ojt202.repositories.MajorRepository;
import com.example.project_ojt202.repositories.ParentProfileRepository;
import com.example.project_ojt202.repositories.StudentProfileRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterStudentAccountService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private ParentProfileRepository parentProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MajorRepository majorRepository;

    private List<StudentProfile> studentProfilesCache;

    public List<StudentProfile> processExcelFile(MultipartFile file) {
        List<StudentProfile> studentProfiles = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                int stt = (int) row.getCell(0).getNumericCellValue();
                String firstName = row.getCell(1).getStringCellValue();
                String lastName = row.getCell(2).getStringCellValue();
                LocalDate dob = row.getCell(3).getDateCellValue().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                boolean gender = row.getCell(4).getStringCellValue().equalsIgnoreCase("Nam");
                String address = row.getCell(5).getStringCellValue();
                String studentPhoneNumber = row.getCell(6).getStringCellValue();
                String studentEmail = row.getCell(7).getStringCellValue();
                String parentName = row.getCell(8).getStringCellValue();
                String parentPhone = row.getCell(9).getStringCellValue();
                String parentEmail = row.getCell(10).getStringCellValue();
                String majorId = row.getCell(11).getStringCellValue();
                int yearOfSubmission = (int) row.getCell(12).getNumericCellValue();
                String relationship = row.getCell(13).getStringCellValue();
                int schoolYear = (int) row.getCell(14).getNumericCellValue();

                // Tạo account_id_student, account_id_parent
                String accountIdStudent = majorId + schoolYear + stt;
                String studentId = accountIdStudent;
                String parentId = studentId + "PH";

                // Tạo ParentProfile
                ParentProfile parentProfile = new ParentProfile(parentId, parentName, parentPhone, parentEmail,
                        relationship);
                // Lấy đối tượng Major từ majorID
                Major major = majorRepository.findByMajorID(majorId);

                // Tạo StudentProfile
                StudentProfile studentProfile = new StudentProfile(studentId, firstName, lastName, dob, gender, address,
                        studentPhoneNumber, studentEmail, yearOfSubmission, schoolYear, major, parentProfile);

                studentProfiles.add(studentProfile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.studentProfilesCache = studentProfiles;
        return studentProfiles;
    }

    public void saveDataToDatabase() {
        if (studentProfilesCache != null) {
            for (StudentProfile studentProfile : studentProfilesCache) {
                ParentProfile parentProfile = studentProfile.getParent();

                // Lưu dữ liệu vào ParentProfile
                parentProfileRepository.save(parentProfile);

                // Lưu dữ liệu vào StudentProfile
                studentProfileRepository.save(studentProfile);

                // Lưu dữ liệu vào bảng Account (student)
                Account studentAccount = new Account(studentProfile.getStudentID(), studentProfile, null, null, "a123",
                        "student", null);
                accountRepository.save(studentAccount);

                // Lưu dữ liệu vào bảng Account (parent)
                Account parentAccount = new Account(parentProfile.getParentID(), null, parentProfile, null, "a123",
                        "parent", null);
                accountRepository.save(parentAccount);
            }
        }
    }
}
