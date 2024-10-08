package com.pspmanagement.model.entity;

import com.pspmanagement.model.constant.DefectStatus;
import com.pspmanagement.model.constant.DefectType;
import com.pspmanagement.model.constant.ProjectPhase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Defect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long defectId;

    private String description;

    @Enumerated(EnumType.STRING)
    private DefectStatus defectStatus;

    @Enumerated(EnumType.STRING)
    private DefectType defectType;

    @Enumerated(EnumType.STRING)
    private ProjectPhase injectedPhase;

    @Enumerated(EnumType.STRING)
    private ProjectPhase removedPhase;

    private LocalDateTime fixTime;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}

