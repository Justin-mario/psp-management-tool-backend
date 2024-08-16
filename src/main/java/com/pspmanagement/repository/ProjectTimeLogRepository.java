package com.pspmanagement.repository;

import com.pspmanagement.model.entity.Project;
import com.pspmanagement.model.entity.ProjectTimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProjectTimeLogRepository extends JpaRepository<ProjectTimeLog, Long> {
    ProjectTimeLog findByProject(Project project);

    Optional<ProjectTimeLog> findTopByProjectAndEndTimeIsNullOrderByStartTimeDesc(Project project);
}
