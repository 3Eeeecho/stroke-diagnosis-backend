package com.stroke.repository;

import com.stroke.model.PatientInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo, String> {
    Page<PatientInfo> findByAgeGreaterThanEqual(Integer age, Pageable pageable);

    Page<PatientInfo> findByMrs90Days(Integer mrs90Days, Pageable pageable);

    Page<PatientInfo> findByImagingExamNumberContaining(String imagingExamNumber, Pageable pageable);

    long countByAgeGreaterThanEqual(Integer age);

    long countByMrs90Days(Integer mrs90Days);

    long countByImagingExamNumberContaining(String imagingExamNumber);

    long countByMrs90DaysGreaterThanEqual(Integer mrs90Days);

    Page<PatientInfo> findByHematomaExpansionEventTrue(Pageable pageable);
}