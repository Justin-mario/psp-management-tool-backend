package com.pspmanagement.model.entity;

import com.pspmanagement.dto.requestdto.ProjectRegistrationRequestDto;
import com.pspmanagement.model.constant.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "projects")
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false, unique = true)
    private String projectName;

    @Column(nullable = false)
    private String projectDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User projectAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private User projectDeveloper;

    @Column(nullable = false)
    private String programmingLanguage;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
    private LocalDateTime EndDate;

    public Project(ProjectRegistrationRequestDto requestDto) {
        this.projectName = requestDto.getProjectName();
        this.projectDescription = requestDto.getProjectDescription();
        this.programmingLanguage = requestDto.getProgrammingLanguage();
        this.projectStatus = ProjectStatus.IN_PROGRESS;
        this.startDate = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectId == project.projectId && Objects.equals(projectName, project.projectName) && Objects.equals(projectDescription, project.projectDescription) && Objects.equals(projectAdmin, project.projectAdmin) && Objects.equals(projectDeveloper, project.projectDeveloper) && Objects.equals(programmingLanguage, project.programmingLanguage) && projectStatus == project.projectStatus && Objects.equals(startDate, project.startDate) && Objects.equals(EndDate, project.EndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, projectDescription, projectAdmin, projectDeveloper, programmingLanguage, projectStatus, startDate, EndDate);
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", projectAdmin=" + projectAdmin +
                ", projectDeveloper=" + projectDeveloper +
                ", programmingLanguage='" + programmingLanguage + '\'' +
                ", projectStatus=" + projectStatus +
                ", StartDAte=" + startDate +
                ", EndDate=" + EndDate +
                '}';
    }
}
