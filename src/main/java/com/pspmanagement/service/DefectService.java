package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.DefectRequestDto;
import com.pspmanagement.model.entity.Defect;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DefectService {
    String createDefect(Long projectId, DefectRequestDto defectRequestDto);
    Boolean changeDefectStatus(Long defectId, String newStatus);
    Boolean setDefectFixTime(Long defectId);
    List<Defect> getDefectsByProjectId(Long projectId);

}
