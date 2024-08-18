package com.pspmanagement.repository;

import com.pspmanagement.model.entity.Defect;
import com.pspmanagement.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {
    List<Defect> findByProject(Project project);
}
