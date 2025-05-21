package com.stroke.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hematoma_expansion")
public class HematomaExpansion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "first_exam_time")
    private LocalDateTime firstExamTime;

    @Column(name = "first_hematoma_volume")
    private Double firstHematomaVolume;

    @Column(name = "followup_exam_time")
    private LocalDateTime followupExamTime;

    @Column(name = "followup_hematoma_volume")
    private Double followupHematomaVolume;

    @Column(name = "volume_increase")
    private Double volumeIncrease;

    @Column(name = "relative_increase")
    private Double relativeIncrease;

    @Column(name = "expansion_probability")
    private Double expansionProbability;
}