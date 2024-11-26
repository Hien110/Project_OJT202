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

    public List<StudentProfile> processExcelFile(MultipartFile file) throws IOException {
        List<StudentProfile> studentProfiles = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                int stt = getCellValueAsInt(row, 0); // Cột STT
                String firstName = getCellValueAsString(row, 1); // Cột First Name
                String lastName = getCellValueAsString(row, 2); // Cột Last Name
                LocalDate dob = getCellValueAsDate(row, 3); // Cột Date of Birth
                boolean gender = row.getCell(4).getStringCellValue().equalsIgnoreCase("Nam");
                String address = getCellValueAsString(row, 5); // Cột Address
                String studentPhoneNumber = getCellValueAsString(row, 6); // Cột Student Phone
                String studentEmail = getCellValueAsString(row, 7); // Cột Student Email
                String parentName = getCellValueAsString(row, 8); // Cột Parent Name
                String parentPhone = getCellValueAsString(row, 9); // Cột Parent Phone
                String parentEmail = getCellValueAsString(row, 10); // Cột Parent Email
                String majorId = getCellValueAsString(row, 11); // Cột Major ID
                int yearOfSubmission = getCellValueAsInt(row, 12); // Cột Year of Submission
                String relationship = getCellValueAsString(row, 13); // Cột Relationship
                int schoolYear = getCellValueAsInt(row, 14); // Cột School Year

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
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        } catch (Exception e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        }

        this.studentProfilesCache = studentProfiles;
        System.out.println(studentProfiles);
        return studentProfiles;
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
