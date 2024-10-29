package com.example.project_ojt202.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_ojt202.models.Scheduce;

public interface ScheduceRepository extends JpaRepository<Scheduce, Long>{
    List<Scheduce> findByUniClass_UniClassId(Long uniClassId);
}
