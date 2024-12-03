package com.example.project_ojt202.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.project_ojt202.models.AnswerTest;
import com.example.project_ojt202.models.QuestionTest;
import com.example.project_ojt202.models.Test;
import com.example.project_ojt202.models.UniClass;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.*;
import java.util.List;
import java.util.Map;

import com.example.project_ojt202.repositories.TestRepository;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public Test saveTest(Test test) {
        return testRepository.save(test);
    }

    private String generateUniqueId(long stt, String prefix) {
        return prefix + stt; // Ví dụ: Q_1, Q_2, ...
    }

    private String getStringCellValue(Cell cell, String columnName) {
        if (cell.getCellType() != CellType.STRING) {
            throw new IllegalArgumentException("Column " + columnName + " must be a string.");
        }
        return cell.getStringCellValue().trim();
    }

    private double getNumericCellValue(Cell cell, String columnName) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            throw new IllegalArgumentException("Column " + columnName + " must be a numeric value.");
        }
        return cell.getNumericCellValue();
    }

    public List<QuestionTest> processExcelFile(MultipartFile file) throws IOException {
        List<QuestionTest> questions = new ArrayList<>();
        Map<Integer, List<AnswerTest>> answersMap = new HashMap<>();

        // Kiểm tra file rỗng
        if (file.isEmpty()) {
            throw new IOException("File is empty!");
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên trong file Excel

            // Duyệt từng dòng, bỏ qua dòng đầu tiên (header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Kiểm tra nếu dòng rỗng
                if (row == null)
                    continue;

                try {
                    // Đọc dữ liệu từ các cột với kiểm tra kiểu dữ liệu
                    long stt = (long) getNumericCellValue(row.getCell(0), "STT");
                    String questionContent = getStringCellValue(row.getCell(1), "Question_Content");
                    int chapter = (int) getNumericCellValue(row.getCell(2), "Chapter");
                    String questionLevel = getStringCellValue(row.getCell(3), "Question_Level");
                    String optionAContent = getStringCellValue(row.getCell(4), "A");
                    String optionBContent = getStringCellValue(row.getCell(5), "B");
                    String optionCContent = getStringCellValue(row.getCell(6), "C");
                    String optionDContent = getStringCellValue(row.getCell(7), "D");
                    String correctAnswer = getStringCellValue(row.getCell(8), "Answer");

                    String uniqueId = generateUniqueId(stt, "Q_");

                    // Tạo đối tượng QuestionTest
                    QuestionTest question = new QuestionTest();
                    question.setQuestionTestID(stt);
                    question.setQuestionTestContent(questionContent);
                    question.setQuestionChapter(chapter);
                    question.setQuestionLevel(questionLevel);

                    // Thêm câu hỏi vào danh sách
                    questions.add(question);

                    // Tạo danh sách các AnswerTest và ánh xạ bằng STT
                    List<AnswerTest> answers = new ArrayList<>();
                    answers.add(new AnswerTest(null, optionAContent, "A".equalsIgnoreCase(correctAnswer), question));
                    answers.add(new AnswerTest(null, optionBContent, "B".equalsIgnoreCase(correctAnswer), question));
                    answers.add(new AnswerTest(null, optionCContent, "C".equalsIgnoreCase(correctAnswer), question));
                    answers.add(new AnswerTest(null, optionDContent, "D".equalsIgnoreCase(correctAnswer), question));

                } catch (IllegalArgumentException e) {
                    System.err.println("Error processing row " + (i + 1) + ": " + e.getMessage());
                    // Có thể log lỗi hoặc tiếp tục bỏ qua dòng bị lỗi
                }
            }

        } catch (IOException e) {
            throw new IOException("Error processing Excel file", e);
        }

        return questions;
    }

    public List<AnswerTest> processExcelFiles(MultipartFile file) throws IOException {
        List<AnswerTest> answers = new ArrayList<>(); // Danh sách lưu các AnswerTest
        if (file.isEmpty()) {
            throw new IOException("File is empty!");
        }
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Chọn trang đầu tiên trong workbook

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                try {
                    long stt = (long) getNumericCellValue(row.getCell(0), "STT");
                    String questionContent = getStringCellValue(row.getCell(1), "Question_Content");
                    int chapter = (int) getNumericCellValue(row.getCell(2), "Chapter");
                    String questionLevel = getStringCellValue(row.getCell(3), "Question_Level");
                    String optionAContent = getStringCellValue(row.getCell(4), "A");
                    String optionBContent = getStringCellValue(row.getCell(5), "B");
                    String optionCContent = getStringCellValue(row.getCell(6), "C");
                    String optionDContent = getStringCellValue(row.getCell(7), "D");
                    String correctAnswer = getStringCellValue(row.getCell(8), "Answer");

                    // Tạo đối tượng QuestionTest
                    QuestionTest question = new QuestionTest();
                    question.setQuestionTestID(stt);
                    question.setQuestionTestContent(questionContent);
                    question.setQuestionChapter(chapter);
                    question.setQuestionLevel(questionLevel);

                    // Tạo danh sách các AnswerTest và ánh xạ bằng STT
                    answers.add(new AnswerTest(null, optionAContent, "A".equalsIgnoreCase(correctAnswer), question));
                    answers.add(new AnswerTest(null, optionBContent, "B".equalsIgnoreCase(correctAnswer), question));
                    answers.add(new AnswerTest(null, optionCContent, "C".equalsIgnoreCase(correctAnswer), question));
                    answers.add(new AnswerTest(null, optionDContent, "D".equalsIgnoreCase(correctAnswer), question));

                } catch (IllegalArgumentException e) {
                    System.err.println("Error processing row " + (i + 1) + ": " + e.getMessage());
                    // Có thể log lỗi hoặc tiếp tục bỏ qua dòng bị lỗi
                }
            }

        } catch (IOException e) {
            throw new IOException("Error processing Excel file", e);
        }

        return answers; // Trả về danh sách các AnswerTest
    }

    public List<Test> getAllTestByUniClass(Long UniclassId) {
        return testRepository.findByUniClass_uniClassId(UniclassId);
    }

    public Test getAllById(Long testID) {
        return testRepository.findByTestID(testID);
    }

    public Long getScoreTranscriptIDByTestID(Long testID) {
        return testRepository.findScoreTranscriptIDByTestID(testID);
    }

    public UniClass getUniClassByTestId(Long testID) {
        Test test = testRepository.findByTestID(testID);
        if (test != null) {
            return test.getUniClass();
        }
        return null; 
    }
}
