package com.pspmanagement.repository;

import com.pspmanagement.model.entity.Project;
import com.pspmanagement.model.entity.ProjectTimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ProjectTimeLogRepository extends JpaRepository<ProjectTimeLog, Long> {
    ProjectTimeLog findByProject(Project project);

}
