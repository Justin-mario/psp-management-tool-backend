package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.DefectRequestDto;
import com.pspmanagement.dto.responsedto.DefectResponseDto;
import com.pspmanagement.exception.ResourceNotFoundException;
import com.pspmanagement.model.constant.DefectStatus;
import com.pspmanagement.model.entity.Defect;
import com.pspmanagement.model.entity.Project;
import com.pspmanagement.repository.DefectRepository;
import com.pspmanagement.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefectServiceImpl implements DefectService{
    private final DefectRepository defectRepository;
    private final ProjectRepository projectRepository;


    public DefectServiceImpl(DefectRepository defectRepository, ProjectRepository projectRepository) {
        this.defectRepository = defectRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public String createDefect(Long projectId, DefectRequestDto defectRequestDto) {
        Project project = validateProject(projectId);
        Defect defect = new Defect();
        defect.setDescription(defectRequestDto.getDescription());
        defect.setDefectType(defectRequestDto.getDefectType());
        defect.setInjectedPhase(defectRequestDto.getInjectedPhase());
        defect.setRemovedPhase(defectRequestDto.getRemovedPhase());
        defect.setDefectStatus(defectRequestDto.getDefectStatus());
        defect.setProject(project);
        defectRepository.save(defect);
        return "Defect created successfully";

    }

    @Override
    @Transactional
    public Boolean changeDefectStatus(Long defectId, DefectRequestDto newStatus) {
        Defect defect = getDefect(defectId);
        defect.setDefectStatus(newStatus.getDefectStatus());
        defectRepository.save(defect);
        return true;

    }

    @Override
    @Transactional
    public Boolean setDefectFixTime(Long defectId) {
        Defect defect = getDefect(defectId);
        defect.setFixTime(LocalDateTime.now());
        defectRepository.save(defect);
        return null;
    }

    @Override
    @Transactional
    public List<DefectResponseDto> getDefectsByProjectId(Long projectId) {
        Project project = validateProject(projectId);
        List<Defect> defects = project.getDefects();
        return defects.stream()
                .map(DefectResponseDto::new)
                .collect(Collectors.toList());

    }

    private Defect getDefect(Long defectId) {
        return defectRepository.findById(defectId).orElseThrow(()-> new ResourceNotFoundException("Defect with id " + defectId + " does not exist"));
    }

    private Project validateProject(Long projectId){
        return projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project with id " + projectId + " does not exist"));
    }
}
