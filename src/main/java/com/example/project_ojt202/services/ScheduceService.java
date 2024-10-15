package com.example.project_ojt202.services;

import com.example.project_ojt202.models.Scheduce;
import com.example.project_ojt202.repositories.ScheduceRepository;
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
}
