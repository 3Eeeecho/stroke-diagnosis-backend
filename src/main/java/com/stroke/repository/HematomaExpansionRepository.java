package com.stroke.repository;

import com.stroke.model.HematomaExpansion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HematomaExpansionRepository extends JpaRepository<HematomaExpansion, Long> {
    HematomaExpansion findByPatientId(String patientId);
}