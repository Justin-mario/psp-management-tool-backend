package com.pspmanagement.repository;

import com.pspmanagement.model.constant.ProjectStatus;
import com.pspmanagement.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByProjectName(String projectName);
    Project findByProjectName(String projectName);
    List<Project> findAllByProjectStatus(ProjectStatus projectStatus);
    List<Project> findAllByProjectDeveloper_Username(String username);
    List<Project> findAllByProjectAdmin_Username(String username);
    List<Project> findAllByProjectAdmin_CompanyName(String companyName);



}
