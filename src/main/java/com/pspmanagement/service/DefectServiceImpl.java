package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.DefectRequestDto;
import com.pspmanagement.exception.ResourceNotFoundException;
import com.pspmanagement.model.constant.DefectStatus;
import com.pspmanagement.model.entity.Defect;
import com.pspmanagement.model.entity.Project;
import com.pspmanagement.repository.DefectRepository;
import com.pspmanagement.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefectServiceImpl implements DefectService{
    private final DefectRepository defectRepository;
    private final ProjectRepository projectRepository;


    public DefectServiceImpl(DefectRepository defectRepository, ProjectRepository projectRepository) {
        this.defectRepository = defectRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public String createDefect(Long projectId, DefectRequestDto defectRequestDto) {
        Project project = validateProject(projectId);
        Defect defect = new Defect();
        defect.setDescription(defectRequestDto.getDescription());
        defect.setDefectType(defectRequestDto.getDefectType());
        defect.setInjectedPhase(defectRequestDto.getInjectedPhase());
        defect.setRemovedPhase(defectRequestDto.getRemovedPhase());
        defect.setProject(project);
        defectRepository.save(defect);
        return "Defect created successfully";

    }

    @Override
    public Boolean changeDefectStatus(Long defectId, String newStatus) {
        Defect defect = getDefect(defectId);
        defect.setDefectStatus(DefectStatus.valueOf(newStatus));
        defectRepository.save(defect);
        return true;

    }

    @Override
    public Boolean setDefectFixTime(Long defectId) {
        Defect defect = getDefect(defectId);
        defect.setFixTime(LocalDateTime.now());
        defectRepository.save(defect);
        return null;
    }

    @Override
    public List<Defect> getDefectsByProjectId(Long projectId) {
        Project project = validateProject(projectId);
        return defectRepository.findByProject(project);
    }

    private Defect getDefect(Long defectId) {
        return defectRepository.findById(defectId).orElseThrow(()-> new ResourceNotFoundException("Defect with id " + defectId + " does not exist"));
    }
    private Project validateProject(Long projectId){
        return projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project with id " + projectId + " does not exist"));
    }
}
