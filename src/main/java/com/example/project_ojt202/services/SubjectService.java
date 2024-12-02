package com.example.project_ojt202.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.Major;
import com.example.project_ojt202.models.PrerequisiteSubject;
import com.example.project_ojt202.repositories.SubjectRepository;
import com.example.project_ojt202.repositories.MajorRepository;
import com.example.project_ojt202.repositories.PrerequisiteSubjectRepository;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SubjectService {

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getSubjectByMajorIDAndTernNo(String majorID, int ternNo) {
        List<Subject> subjects = subjectRepository.findByMajor_MajorIDAndTernNo(majorID, ternNo);
        return subjects;
    }

    public Subject getSubjectBySubjectID(String subjectID) {
        Subject subject = subjectRepository.findBySubjectID(subjectID);
        return subject;
    }

    // H.anh
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private PrerequisiteSubjectRepository PrerequisiteSubjectRepository;

    @Autowired
    private MajorRepository majorRepository;

    private List<Subject> subjectCache;
    private List<PrerequisiteSubject> prerequisiteSubjectCache;

    public List<Subject> processExcelFile(MultipartFile file) throws IOException {
        List<Subject> subjects = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Lấy các giá trị từ các cột, sử dụng phương thức nhận diện kiểu dữ liệu tự
                // động
                String subjectName = getCellValueAsString(row, 1); // Cột Subject Name
                String subjectId = getCellValueAsString(row, 2); // Cột Subject ID
                int subjectCredit = getCellValueAsInt(row, 3); // Cột Subject Credit
                int ternNo = getCellValueAsInt(row, 4); // Cột Tern No
                String majorId = getCellValueAsString(row, 5); // Cột Major ID

                // Lấy đối tượng Major từ majorId
                Major major = majorRepository.findByMajorID(majorId);

                // Tạo đối tượng Subject
                Subject subject = new Subject(subjectId, subjectName, subjectCredit, ternNo, major);

                subjects.add(subject);
            }
        } catch (IOException e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        } catch (Exception e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        }

        this.subjectCache = subjects;
        return subjects;
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

    public List<PrerequisiteSubject> processExcelFile1(MultipartFile file) throws IOException {
        List<PrerequisiteSubject> listPrerequisiteSubjects = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Lấy các giá trị từ các cột, sử dụng phương thức nhận diện kiểu dữ liệu tự
                // động
                String subjectName = getCellValueAsString(row, 1); // Cột Subject Name
                String subjectId = getCellValueAsString(row, 2); // Cột Subject ID
                int subjectCredit = getCellValueAsInt(row, 3); // Cột Subject Credit
                int ternNo = getCellValueAsInt(row, 4); // Cột Tern No
                String majorId = getCellValueAsString(row, 5); // Cột Major ID

                // Lấy đối tượng Major từ majorId
                Major major = majorRepository.findByMajorID(majorId);

                // Tạo đối tượng Subject
                Subject subject = new Subject(subjectId, subjectName, subjectCredit, ternNo, major);

                String prerequisiteSubjects = getCellValueAsString(row, 6); // Cột môn tiên quyết

                // Xử lý môn tiên quyết
                String[] prerequisiteIds = prerequisiteSubjects.split(","); // Phân tách danh sách môn tiên quyết
                for (String prerequisiteId : prerequisiteIds) {
                    prerequisiteId = prerequisiteId.trim(); // Loại bỏ khoảng trắng nếu có

                    // Tạo đối tượng PrerequisiteSubject
                    PrerequisiteSubject prerequisite = new PrerequisiteSubject(null, prerequisiteId, subject);
                    listPrerequisiteSubjects.add(prerequisite);
                }
            }
        } catch (IOException e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        } catch (Exception e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        }

        this.prerequisiteSubjectCache = listPrerequisiteSubjects;
        return listPrerequisiteSubjects;
    }

    public List<String> processExcelFile2(MultipartFile file) throws IOException {
        List<String> presite = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Lấy giá trị của cột môn tiên quyết
                String prerequisiteSubjects = getCellValueAsString(row, 6); // Cột môn tiên quyết
                presite.add(prerequisiteSubjects);
            }
        } catch (IOException e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        } catch (Exception e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        }
        return presite;
    }

    public void saveDataToDatabase() {
        if (subjectCache != null) {
            for (Subject subject : subjectCache) {

                // Lưu dữ liệu vào subject
                subjectRepository.save(subject);
            }
            for (PrerequisiteSubject prerequisiteSubject : prerequisiteSubjectCache) {
                PrerequisiteSubjectRepository.save(prerequisiteSubject);
            }
        }
    }

    // allOfSubject
    public Page<Subject> getAllSubjects(int page, int size) {
        return subjectRepository.findAll(PageRequest.of(page, size));
    }

    public Subject getSubjectById(String subjectID) {
        return subjectRepository.findBySubjectID(subjectID);
    }
}
