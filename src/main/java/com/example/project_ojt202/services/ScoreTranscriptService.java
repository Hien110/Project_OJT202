package com.example.project_ojt202.services;

import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.repositories.ScoreTranscriptRepository;
import com.example.project_ojt202.repositories.SubjectRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreTranscriptService {

    @Autowired
    private ScoreTranscriptRepository scoreTranscriptRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public ScoreTranscriptService(ScoreTranscriptRepository scoreTranscriptRepository) {
        this.scoreTranscriptRepository = scoreTranscriptRepository;
    }

    public List<ScoreTranscript> findAllScoreTranscripts() {
        return scoreTranscriptRepository.findAll();
    }

    // H Anh

    private List<ScoreTranscript> scoreTranscriptCache;

    public List<ScoreTranscript> processExcelFile(MultipartFile file) throws IOException {
        List<ScoreTranscript> scoreTranscripts = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String nameTest = row.getCell(1).getStringCellValue();
                int numberCollum = (int) row.getCell(2).getNumericCellValue();
                int totalPercent = (int) row.getCell(3).getNumericCellValue();
                String subjectID = row.getCell(4).getStringCellValue();

                // Lấy đối tượng subject
                Subject subject = subjectRepository.findBySubjectID(subjectID);

                // Tạo ScoreTranscript
                ScoreTranscript scoreTranscript = new ScoreTranscript(null, nameTest, numberCollum, totalPercent,
                        subject);
                scoreTranscripts.add(scoreTranscript);
            }
        } catch (IOException e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        } catch (Exception e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file. Vui lòng kiểm tra lại định dạng file");
        }

        this.scoreTranscriptCache = scoreTranscripts;
        return scoreTranscripts;
    }

    public void saveDataToDatabase() {
        if (scoreTranscriptCache != null) {
            for (ScoreTranscript scoreTranscript : scoreTranscriptCache) {

                // Lưu dữ liệu vào ScoreTranscript
                scoreTranscriptRepository.save(scoreTranscript);
            }
        }
    }

    public ScoreTranscript findById(Long id) {
        return scoreTranscriptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ScoreTranscript not found with ID: " + id));
    }
}
