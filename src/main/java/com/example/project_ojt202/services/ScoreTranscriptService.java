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

    private List<ScoreTranscript> scoreTranscriptCache;

    // Process Excel File
    public List<ScoreTranscript> processExcelFile(MultipartFile file) throws IOException {
        List<ScoreTranscript> scoreTranscripts = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String nameTest = getCellValueAsString(row.getCell(1));
                int numberColumn = (int) row.getCell(2).getNumericCellValue();
                int totalPercent = (int) row.getCell(3).getNumericCellValue();
                String subjectID = getCellValueAsString(row.getCell(4));

                // Find subject by ID
                Subject subject = subjectRepository.findBySubjectID(subjectID);
                if (subject == null) {
                    throw new IllegalArgumentException("Không tìm thấy môn học với ID: " + subjectID);
                }

                // Create ScoreTranscript object
                ScoreTranscript scoreTranscript = new ScoreTranscript(null, nameTest, numberColumn, totalPercent, subject);
                scoreTranscripts.add(scoreTranscript);
            }
        } catch (IOException e) {
            throw new IOException("Đã xảy ra lỗi khi đọc file Excel: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IOException("Lỗi dữ liệu trong file Excel: " + e.getMessage());
        }

        // Cache data for saving
        this.scoreTranscriptCache = scoreTranscripts;
        return scoreTranscripts;
    }

    // Save cached data to database
    public void saveDataToDatabase() {
        if (scoreTranscriptCache != null && !scoreTranscriptCache.isEmpty()) {
            scoreTranscriptRepository.saveAll(scoreTranscriptCache);
        } else {
            throw new IllegalStateException("Không có dữ liệu nào để lưu vào cơ sở dữ liệu.");
        }
    }

    // Helper method to handle null or non-string cells
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf((int) cell.getNumericCellValue());
    }

    public List<ScoreTranscript> getAllScoreTranscripts() {
        return scoreTranscriptRepository.findAll();
    }
    
}
