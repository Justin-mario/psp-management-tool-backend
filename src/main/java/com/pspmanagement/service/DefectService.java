package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.DefectRequestDto;
import com.pspmanagement.dto.responsedto.DefectResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DefectService {
    String createDefect(Long projectId, DefectRequestDto defectRequestDto);
    Boolean changeDefectStatus(Long defectId, DefectRequestDto newStatus);
    Boolean setDefectFixTime(Long defectId);
    List<DefectResponseDto> getDefectsByProjectId(Long projectId);
}
