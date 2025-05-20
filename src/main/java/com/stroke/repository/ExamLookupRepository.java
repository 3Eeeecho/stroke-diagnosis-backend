package com.stroke.repository;

import com.stroke.model.ExamLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ExamLookupRepository extends JpaRepository<ExamLookup, String> {
    Optional<ExamLookup> findById(String id);

    Optional<ExamLookup> findByInitialExamCode(String initialExamCode);
}
