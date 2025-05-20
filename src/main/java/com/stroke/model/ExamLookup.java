package com.stroke.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "exam_lookup")
public class ExamLookup {
    @Id
    @Column(name = "id", length = 10)
    private String id;

    @Column(name = "exam_count")
    private int examCount;

    @Column(name = "initial_exam_time")
    private LocalDateTime initialExamTime;

    @Column(name = "initial_exam_code", length = 14)
    private String initialExamCode;

    @Column(name = "followup1_time")
    private LocalDateTime followup1Time;

    @Column(name = "followup1_code", length = 14)
    private String followup1Code;

    @Column(name = "followup2_time")
    private LocalDateTime followup2Time;

    @Column(name = "followup2_code", length = 14)
    private String followup2Code;

    @Column(name = "followup3_time")
    private LocalDateTime followup3Time;

    @Column(name = "followup3_code", length = 14)
    private String followup3Code;

    @Column(name = "followup4_time")
    private LocalDateTime followup4Time;

    @Column(name = "followup4_code", length = 14)
    private String followup4Code;

    @Column(name = "followup5_time")
    private LocalDateTime followup5Time;

    @Column(name = "followup5_code", length = 14)
    private String followup5Code;

    @Column(name = "followup6_time")
    private LocalDateTime followup6Time;

    @Column(name = "followup6_code", length = 14)
    private String followup6Code;

    @Column(name = "followup7_time")
    private LocalDateTime followup7Time;

    @Column(name = "followup7_code", length = 14)
    private String followup7Code;

    @Column(name = "followup8_time")
    private LocalDateTime followup8Time;

    @Column(name = "followup8_code", length = 14)
    private String followup8Code;

    @Column(name = "followup9_time")
    private LocalDateTime followup9Time;

    @Column(name = "followup9_code", length = 14)
    private String followup9Code;

    @Column(name = "followup10_time")
    private LocalDateTime followup10Time;

    @Column(name = "followup10_code", length = 14)
    private String followup10Code;

    @Column(name = "followup11_time")
    private LocalDateTime followup11Time;

    @Column(name = "followup11_code", length = 14)
    private String followup11Code;

    @Column(name = "followup12_time")
    private LocalDateTime followup12Time;

    @Column(name = "followup12_code", length = 14)
    private String followup12Code;

    @Column(name = "followup13_time")
    private LocalDateTime followup13Time;

    @Column(name = "followup13_code", length = 14)
    private String followup13Code;
}