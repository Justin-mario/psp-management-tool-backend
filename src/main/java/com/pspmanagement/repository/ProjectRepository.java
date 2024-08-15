package com.pspmanagement.repository;

import com.pspmanagement.model.constant.ProjectStatus;
import com.pspmanagement.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.timeLogs WHERE p.projectId = :id")
    Optional<Project> findByIdWithTimeLogs(@Param("id") Long id);
    List<Project> findAllByProjectStatus(ProjectStatus projectStatus);
    List<Project> findAllByProjectDeveloper_Username(String username);
    List<Project> findAllByProjectAdmin_Username(String username);
    List<Project> findAllByProjectAdmin_CompanyName(String companyName);



}
