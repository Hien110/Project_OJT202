package com.example.project_ojt202.services;

import com.example.project_ojt202.models.CheckAttendance;
import com.example.project_ojt202.models.Scheduce;
import com.example.project_ojt202.repositories.CheckAttendanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckAttendanceService {

    private final CheckAttendanceRepository checkAttendanceRepository;
    private final StudentProfileService studentProfileService;
    private final ScheduceService scheduceService;

    public CheckAttendanceService(CheckAttendanceRepository checkAttendanceRepository,
                                  StudentProfileService studentProfileService,
                                  ScheduceService scheduceService) {
        this.checkAttendanceRepository = checkAttendanceRepository;
        this.studentProfileService = studentProfileService;
        this.scheduceService = scheduceService;
    }

    public void recordAttendance(String studentID, Long uniClassId, boolean isPresent) {
        CheckAttendance attendance = new CheckAttendance();

        // Get StudentProfile by studentID
        attendance.setStudentProfile(studentProfileService.getStudentProfileById(studentID));

        // Get the first Scheduce for the given uniClassId
        Scheduce schedule = scheduceService.getFirstSchedule(uniClassId);
        if (schedule != null) {
            attendance.setScheduce(schedule);
        } else {
            System.out.println("No matching Scheduce found for classId: " + uniClassId);
            return;
        }

        // Record attendance status
        attendance.setCheckAttendance(isPresent);

        // Save attendance record
        checkAttendanceRepository.save(attendance);
    }

    public List<CheckAttendance> listCheckAttendanceByScheduce(Scheduce scheduce) {
        return checkAttendanceRepository.findByScheduce(scheduce);
    }

    @Transactional
    public void updateCheckAttendance(Long scheduceID, String studentID, boolean checkAttendance) {
        int rowsUpdated = checkAttendanceRepository.updateCheckAttendance(checkAttendance, scheduceID, studentID);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("No record found for scheduceID: " + scheduceID + " and studentID: " + studentID);
        }
    }

    public CheckAttendance getCheckAttendanceByScheduceAndStudent(Long scheduceID, String studentID) {
        return checkAttendanceRepository.findByScheduce_scheduceIDAndStudentProfile_studentID(scheduceID, studentID);
    }
}
