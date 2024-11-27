package com.example.project_ojt202.services;

import com.example.project_ojt202.models.Scheduce;
import com.example.project_ojt202.repositories.ScheduceRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ScheduceService {
    private final ScheduceRepository scheduceRepository;

    public ScheduceService(ScheduceRepository scheduceRepository){
        this.scheduceRepository = scheduceRepository;
    }

    public List<Scheduce> findScheduceOfUniClass(Long uniClassID){
        List<Scheduce> scheduces = scheduceRepository.findByUniClass_UniClassId(uniClassID);
        return scheduces;
    }

    public void saveScheduce(Scheduce scheduce){
        scheduceRepository.save(scheduce);
    }

    public Scheduce getScheduceById(Long id){
        return scheduceRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
    public List<Scheduce> getSchedulesByClassId(Long uniClassId) {
        return scheduceRepository.findByUniClass_UniClassId(uniClassId);
    }

    public Scheduce getFirstSchedule(Long uniClassId) {
        List<Scheduce> schedules = getSchedulesByClassId(uniClassId);
        if (!schedules.isEmpty()) {
            return schedules.get(0);
        } else {
            return null;
        }
    }
}
