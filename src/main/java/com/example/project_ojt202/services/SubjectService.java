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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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

                // int stt = (int) row.getCell(0).getNumericCellValue();
                String subjectName = row.getCell(1).getStringCellValue();
                String subjectId = row.getCell(2).getStringCellValue();
                int subjectCredit = (int) row.getCell(3).getNumericCellValue();
                int ternNo = (int) row.getCell(4).getNumericCellValue();
                String majorId = row.getCell(5).getStringCellValue();

                // Lấy đối tượng Major từ majorID
                Major major = majorRepository.findByMajorID(majorId);

                // Tạo Subject
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

    public List<PrerequisiteSubject> processExcelFile1(MultipartFile file) throws IOException {
        List<PrerequisiteSubject> listPrerequisiteSubjects = new ArrayList<>();
        List<Subject> subjects = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String subjectName = row.getCell(1).getStringCellValue();
                String subjectId = row.getCell(2).getStringCellValue();
                int subjectCredit = (int) row.getCell(3).getNumericCellValue();
                int ternNo = (int) row.getCell(4).getNumericCellValue();
                String majorId = row.getCell(5).getStringCellValue();

                // Lấy đối tượng Major từ majorID
                Major major = majorRepository.findByMajorID(majorId);

                // Tạo Subject
                Subject subject = new Subject(subjectId, subjectName, subjectCredit, ternNo, major);

                subjects.add(subject);

                String prerequisiteSubjects = row.getCell(6).getStringCellValue(); // Cột môn tiên quyết

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

                String prerequisiteSubjects = row.getCell(6).getStringCellValue(); // Cột môn tiên quyết
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
}
