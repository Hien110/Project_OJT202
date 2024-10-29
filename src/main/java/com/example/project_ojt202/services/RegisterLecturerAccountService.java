package com.example.project_ojt202.services;

import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.Major;
import com.example.project_ojt202.repositories.AccountRepository;
import com.example.project_ojt202.repositories.LectureProfileRepository;
import com.example.project_ojt202.repositories.MajorRepository;
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
public class RegisterLecturerAccountService {

    @Autowired
    private LectureProfileRepository lectureProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MajorRepository majorRepository;

    private List<LectureProfile> lecturerProfilesCache;

    public List<LectureProfile> processExcelFile(MultipartFile file) throws IOException {
        List<LectureProfile> lectureProfiles = new ArrayList<>();
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
                String phoneNumber = row.getCell(6).getStringCellValue();
                String email = row.getCell(7).getStringCellValue();
                String majorId = row.getCell(8).getStringCellValue();
                int yearOfSubmission = (int) row.getCell(9).getNumericCellValue();

                // Tạo account_id
                String[] nameParts = firstName.split(" ");
                StringBuilder initials = new StringBuilder();
                for (String part : nameParts) {
                    initials.append(part.charAt(0)); // Lấy ký tự đầu
                }

                // Ghép lại thành accountId
                String accountId = lastName + initials.toString().toUpperCase() + String.format("%03d", stt);
                String lecturerId = accountId;

                // Lấy đối tượng Major từ majorID
                Major major = majorRepository.findByMajorID(majorId);

                // Tạo StudentProfile
                LectureProfile lectureProfile = new LectureProfile(lecturerId, firstName, lastName, dob, false, "null", gender, address, phoneNumber, email, yearOfSubmission, major);

                lectureProfiles.add(lectureProfile);
            }
        } catch (IOException e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        } catch (Exception e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        }

        this.lecturerProfilesCache = lectureProfiles;
        return lectureProfiles;
    }

    public void saveDataToDatabase() {
        if (lecturerProfilesCache != null) {
            for (LectureProfile lectureProfile : lecturerProfilesCache) {

                // Lưu dữ liệu vào LectureProfile
                lectureProfileRepository.save(lectureProfile);

                // Lưu dữ liệu vào bảng Account (lecturer)
                Account lectureAccount = new Account(lectureProfile.getLectureID(), null, null, lectureProfile, "a123",
                        "lecturer", null);
                accountRepository.save(lectureAccount);
            }
        }
    }
}
