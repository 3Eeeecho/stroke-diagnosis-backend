package com.stroke.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "imaging_volume")
public class ImagingVolume {
    @Id
    @Column(name = "exam_code", length = 14)
    private String examCode;

    @Column(name = "patient_id", length = 10)
    private String patientId;

    @Column(name = "HM_volume")
    private int hmVolume;

    @Column(name = "HM_ACA_R_Ratio")
    private float hmAcaRRatio;

    @Column(name = "HM_MCA_R_Ratio")
    private float hmMcaRRatio;

    @Column(name = "HM_PCA_R_Ratio")
    private float hmPcaRRatio;

    @Column(name = "HM_Pons_Medulla_R_Ratio")
    private float hmPonsMedullaRRatio;

    @Column(name = "HM_Cerebellum_R_Ratio")
    private float hmCerebellumRRatio;

    @Column(name = "HM_ACA_L_Ratio")
    private float hmAcaLRatio;

    @Column(name = "HM_MCA_L_Ratio")
    private float hmMcaLRatio;

    @Column(name = "HM_PCA_L_Ratio")
    private float hmPcaLRatio;

    @Column(name = "HM_Pons_Medulla_L_Ratio")
    private float hmPonsMedullaLRatio;

    @Column(name = "HM_Cerebellum_L_Ratio")
    private float hmCerebellumLRatio;

    @Column(name = "ED_volume")
    private int edVolume;

    @Column(name = "ED_ACA_R_Ratio")
    private float edAcaRRatio;

    @Column(name = "ED_MCA_R_Ratio")
    private float edMcaRRatio;

    @Column(name = "ED_PCA_R_Ratio")
    private float edPcaRRatio;

    @Column(name = "ED_Pons_Medulla_R_Ratio")
    private float edPonsMedullaRRatio;

    @Column(name = "ED_Cerebellum_R_Ratio")
    private float edCerebellumRRatio;

    @Column(name = "ED_ACA_L_Ratio")
    private float edAcaLRatio;

    @Column(name = "ED_MCA_L_Ratio")
    private float edMcaLRatio;

    @Column(name = "ED_PCA_L_Ratio")
    private float edPcaLRatio;

    @Column(name = "ED_Pons_Medulla_L_Ratio")
    private float edPonsMedullaLRatio;

    @Column(name = "ED_Cerebellum_L_Ratio")
    private float edCerebellumLRatio;
}