package com.example.project_ojt202.services;

import com.example.project_ojt202.models.ScoreTranscript;
import com.example.project_ojt202.models.Subject;
import com.example.project_ojt202.models.UniClass;
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
import java.util.stream.Collectors;

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

                // Lấy giá trị từ các cột trong bảng Excel bằng cách nhận diện kiểu dữ liệu tự
                // động
                String nameTest = getCellValueAsString(row, 1); // Cột NameTest
                int numberCollum = getCellValueAsInt(row, 2); // Cột NumberCollum
                int totalPercent = getCellValueAsInt(row, 3); // Cột TotalPercent
                String subjectID = getCellValueAsString(row, 4); // Cột SubjectID

                // Lấy đối tượng Subject từ subjectID
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
    public List<ScoreTranscript> getScoreTranscriptsBySubjectId(String subjectId) {
        return scoreTranscriptRepository.findBySubjectSubjectID(subjectId);
    }
}
