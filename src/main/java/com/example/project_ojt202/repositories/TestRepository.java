package com.example.project_ojt202.repositories;

import com.example.project_ojt202.models.Test;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findByUniClass_uniClassId(Long uniClassId);

    Test findByTestID(Long testID);

    @Query("SELECT t.scoreTranscript.scoreTranscriptID FROM Test t WHERE t.testID = :testID")
    Long findScoreTranscriptIDByTestID(Long testID);

}
