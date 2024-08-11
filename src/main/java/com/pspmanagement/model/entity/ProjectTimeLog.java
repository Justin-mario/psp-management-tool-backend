package com.pspmanagement.model.entity;

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
public class ProjectTimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectTimeLogId;
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;
    private String description;
    private ProjectPhase phase;
}
