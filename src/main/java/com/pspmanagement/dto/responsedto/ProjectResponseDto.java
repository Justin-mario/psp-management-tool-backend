package com.pspmanagement.dto.responsedto;

import com.pspmanagement.model.constant.ProjectPhase;
import com.pspmanagement.model.constant.ProjectStatus;
import com.pspmanagement.model.entity.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProjectResponseDto {
    private Long projectId;

    private String projectName;

    private String projectDescription;

    private String projectDeveloper;

    private String programmingLanguage;

    private ProjectStatus projectStatus;

    private String startDate;

    private String endDate;

    private String projectAdmin;

    private ProjectPhase phase;

    private String duration;

    private List<DefectResponseDto> defects;

    private String companyName;



    public ProjectResponseDto(Project project) {
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
        this.projectDescription = project.getProjectDescription();
        this.projectDeveloper = project.getProjectDeveloper().getUsername();
        this.programmingLanguage = project.getProgrammingLanguage();
        this.projectStatus = project.getProjectStatus();
        this.startDate = String.valueOf(project.getStartDate());
        this.endDate = String.valueOf(project.getEndDate());
        this.projectAdmin = project.getProjectAdmin().getUsername();
        this.phase = project.getProjectPhase();
        this.duration = project.calculateDuration();
        this.companyName = project.getProjectAdmin().getCompanyName();
//        this.defects = project.getDefects().stream()
//                .map(DefectResponseDto::new)
//                .collect(Collectors.toList());

        this.defects = project.getDefects() != null
                ? project.getDefects().stream().map(DefectResponseDto::new).collect(Collectors.toList())
                : new ArrayList<>();

    }





    @Override
    public String toString() {
        return "ProjectResponseDto{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", projectDeveloper='" + projectDeveloper + '\'' +
                ", programmingLanguage='" + programmingLanguage + '\'' +
                ", projectStatus=" + projectStatus +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", projectAdmin='" + projectAdmin + '\'' +
                ", projecPhase=" + phase +
                ", duration=" + duration +
                ", defects=" + defects +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
