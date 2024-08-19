package com.pspmanagement.model.entity;

import com.pspmanagement.dto.requestdto.ProjectRegistrationRequestDto;
import com.pspmanagement.model.constant.ProjectPhase;
import com.pspmanagement.model.constant.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
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


    @OneToMany(mappedBy = "project",  fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProjectTimeLog> timeLogs;

    @Transient
    private int duration;

    @Enumerated(EnumType.STRING)
    private ProjectPhase projectPhase;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Defect> defects  = new ArrayList<>(); ;

    public Project(ProjectRegistrationRequestDto requestDto) {
        this.projectName = requestDto.getProjectName();
        this.projectDescription = requestDto.getProjectDescription();
        this.programmingLanguage = requestDto.getProgrammingLanguage();
        this.projectStatus = ProjectStatus.IN_PROGRESS;
        this.startDate = LocalDateTime.now();
    }

    public String calculateDuration() {
        long totalMinutes = 0;
        if(this.timeLogs != null) {
            for (ProjectTimeLog timeLog : this.timeLogs) {
                LocalDateTime startTime = timeLog.getStartTime();
                LocalDateTime endTime = timeLog.getEndTime();
                if (endTime != null) {
                    totalMinutes += java.time.temporal.ChronoUnit.MINUTES.between(startTime, endTime);
                }
            }
        }
        if (totalMinutes < 60) {
            return totalMinutes + " minutes";
        } else {
            long hours = totalMinutes / 60;
            long remainingMinutes = totalMinutes % 60;
            return hours + " hours " + remainingMinutes + " minutes";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return duration == project.duration && Objects.equals(projectId, project.projectId) && Objects.equals(projectName, project.projectName) && Objects.equals(projectDescription, project.projectDescription) && Objects.equals(projectAdmin, project.projectAdmin) && Objects.equals(projectDeveloper, project.projectDeveloper) && Objects.equals(programmingLanguage, project.programmingLanguage) && projectStatus == project.projectStatus && Objects.equals(startDate, project.startDate) && Objects.equals(EndDate, project.EndDate) && Objects.equals(timeLogs, project.timeLogs) && projectPhase == project.projectPhase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, projectDescription, projectAdmin, projectDeveloper, programmingLanguage, projectStatus, startDate, EndDate, timeLogs, duration, projectPhase);
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
                ", startDate=" + startDate +
                ", EndDate=" + EndDate +
                ", timeLogs=" + timeLogs +
                ", duration=" + duration +
                ", projecPhase=" + projectPhase +
                '}';
    }
}
