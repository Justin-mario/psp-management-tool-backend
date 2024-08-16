package com.pspmanagement.service;

import com.pspmanagement.exception.ResourceNotFoundException;
import com.pspmanagement.model.entity.Project;
import com.pspmanagement.model.entity.ProjectTimeLog;
import com.pspmanagement.repository.ProjectRepository;
import com.pspmanagement.repository.ProjectTimeLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ProjectTimeLogServiceImpl implements ProjectTimeLogService{
    private final ProjectRepository projectRepository;
    private final ProjectTimeLogRepository projectTimeLogRepository;

    public ProjectTimeLogServiceImpl(ProjectRepository projectRepository, ProjectTimeLogRepository projectTimeLogRepository) {
        this.projectRepository = projectRepository;
        this.projectTimeLogRepository = projectTimeLogRepository;
    }

    @Override
    @Transactional
    public Boolean startProject(Long projectId) {
            Project project = validateProject(projectId);
            ProjectTimeLog projectTimeLog = new ProjectTimeLog();
            projectTimeLog.setProject(project);
            projectTimeLog.setStartTime(LocalDateTime.now());
            projectTimeLogRepository.save(projectTimeLog);
            return true;


    }

    @Override
    @Transactional
    public Boolean endProject(Long projectId) {
        Project project = validateProject(projectId);
        ProjectTimeLog timeLog = projectTimeLogRepository.findTopByProjectAndEndTimeIsNullOrderByStartTimeDesc(project)
                .orElseThrow(() -> new IllegalStateException("No active work session found for project with id: " + projectId));
        timeLog.setEndTime(LocalDateTime.now());
        projectTimeLogRepository.save(timeLog);
        return true;
    }


    @Override
    @Transactional
    public String getProjectDuration(Long projectId) {
        Project project = projectRepository.findByIdWithTimeLogs(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        return project.calculateDuration();
    }

    private Project validateProject(Long projectId){
        return projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project with id " + projectId + " does not exist"));
    }
}
