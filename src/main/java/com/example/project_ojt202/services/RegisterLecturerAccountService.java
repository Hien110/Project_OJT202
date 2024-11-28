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
import java.text.Normalizer;

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

                // Lấy giá trị từ các cột trong bảng Excel bằng cách nhận diện kiểu dữ liệu tự
                // động
                int stt = getCellValueAsInt(row, 0); // Cột STT
                String firstName = getCellValueAsString(row, 1); // Cột First Name
                String lastName = getCellValueAsString(row, 2); // Cột Last Name
                LocalDate dob = getCellValueAsDate(row, 3); // Cột Date of Birth
                boolean gender = row.getCell(4).getStringCellValue().equalsIgnoreCase("Nam");                String address = getCellValueAsString(row, 5); // Cột Address
                String phoneNumber = getCellValueAsString(row, 6); // Cột Phone Number
                String email = getCellValueAsString(row, 7); // Cột Email
                String majorId = getCellValueAsString(row, 8); // Cột Major ID
                int yearOfSubmission = getCellValueAsInt(row, 9); // Cột Year of Submission

                // Normalize Vietnamese characters to remove accents
                String normalizedFirstName = normalizeVietnameseName(firstName);
                String normalizedLastName = normalizeVietnameseName(lastName);

                // Tạo account_id
                String[] nameParts = normalizedFirstName.split(" ");
                StringBuilder initials = new StringBuilder();
                for (String part : nameParts) {
                    initials.append(part.charAt(0)); // Lấy ký tự đầu
                }

                // Ghép lại thành accountId
                String accountId = normalizedLastName + initials.toString().toUpperCase() + String.format("%03d", stt);
                String lecturerId = accountId;

                // Lấy đối tượng Major từ majorID
                Major major = majorRepository.findByMajorID(majorId);

                // Tạo LectureProfile
                LectureProfile lectureProfile = new LectureProfile(lecturerId, firstName, lastName, dob, false, "null",
                        gender, address, phoneNumber, email, yearOfSubmission, major);

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

    // Phương thức lấy giá trị ô dưới dạng String
    private String getCellValueAsString(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // Phương thức lấy giá trị ô dưới dạng int
    private int getCellValueAsInt(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return 0;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    // Phương thức lấy giá trị ô dưới dạng LocalDate (dành cho ngày tháng)
    private LocalDate getCellValueAsDate(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toInstant()
                            .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                }
            case STRING:
                try {
                    return LocalDate.parse(cell.getStringCellValue());
                } catch (Exception e) {
                    return null;
                }
            default:
                return null;
        }
    }

    private String normalizeVietnameseName(String name) {
        // Normalize the name to remove accents (diacritical marks)
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); // Remove diacritical marks
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
