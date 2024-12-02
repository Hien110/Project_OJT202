package com.example.project_ojt202.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.project_ojt202.models.Account;
import com.example.project_ojt202.models.Learn;
import com.example.project_ojt202.models.LectureProfile;
import com.example.project_ojt202.models.StudentProfile;
import com.example.project_ojt202.models.UniClass;
import com.example.project_ojt202.repositories.AccountRepository;
import com.example.project_ojt202.repositories.LearnRepository;
import com.example.project_ojt202.repositories.LectureProfileRepository;
import com.example.project_ojt202.repositories.UniClassRepository;

@Service
public class LectureProfileService {
    private final LectureProfileRepository lectureProfileRepository;
    private final UniClassRepository uniClassRepository;
    private final LearnRepository learnRepository;
    @Autowired
    private AccountRepository accountRepository;

    public LectureProfileService(LectureProfileRepository lectureProfileRepository,
            UniClassRepository uniClassRepository,
            LearnRepository learnRepository) {
        this.lectureProfileRepository = lectureProfileRepository;
        this.uniClassRepository = uniClassRepository;
        this.learnRepository = learnRepository;
    }

    public LectureProfile getLecProfileByLectureID(String lectureID) {
        LectureProfile lecture = lectureProfileRepository.findByLectureID(lectureID);
        return lecture;
    }

    public List<LectureProfile> getLecProfileByMajorID(String majorID) {
        List<LectureProfile> majLectureProfile = lectureProfileRepository.findByMajor_majorID(majorID);
        return majLectureProfile;
    }

    public List<UniClass> getClassesForLecturer(String lectureID) {
        return uniClassRepository.findByLectureProfile_LectureID(lectureID);
    }

    public List<StudentProfile> getStudentsForClass(Long uniClassId) {
        return learnRepository.findByUniClass_UniClassId(uniClassId)
                .stream()
                .map(Learn::getStudentProfile)
                .collect(Collectors.toList());
    }

    public List<UniClass> filterClassesBySemester(String lectureID, String semester) {
        return uniClassRepository.findByLectureProfile_LectureIDAndSemester(lectureID, semester);
    }

    //allOfLecturerList
    public Page<LectureProfile> getAllLecturers(int page, int size) {
        return lectureProfileRepository.findAll(PageRequest.of(page, size));
    }

    public List<Account> getLecturerAccounts() {
        return accountRepository.findByAccountRole("lecturer");
    }

    public Account getLecturerAccountById(String lecturerID) {
        return accountRepository.findByAccountID(lecturerID);
    }

    public LectureProfile findByLectureID(String lectureID) {
        return lectureProfileRepository.findByLectureID(lectureID);
    }

     // Phương thức để đọc danh sách sinh viên từ file Excel
    public List<StudentProfile> getStudentsFromExcel(MultipartFile file) throws IOException {
        List<StudentProfile> students = new ArrayList<>();

        // Đọc file Excel
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);  // Lấy sheet đầu tiên

            // Lặp qua từng dòng (bắt đầu từ dòng thứ 1 nếu có tiêu đề)
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Lấy dữ liệu từ các cột (giả sử cột 0 là mã sinh viên, cột 1 là tên sinh viên, cột 2 là email...)
                String studentId = row.getCell(0).getStringCellValue();
                String studentName = row.getCell(1).getStringCellValue();
                String email = row.getCell(2).getStringCellValue();

                // Tạo đối tượng StudentProfile và thêm vào danh sách
                StudentProfile student = new StudentProfile();
                student.setStudentID(studentId);
                student.setFirstName(email);
                student.setEmail(email);

                students.add(student);
            }
        }

        return students;
    }
}
