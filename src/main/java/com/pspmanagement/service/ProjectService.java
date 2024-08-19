package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.ProjectRegistrationRequestDto;
import com.pspmanagement.dto.requestdto.ProjectRequestDto;
import com.pspmanagement.dto.responsedto.ProjectResponseDto;

import java.util.List;
import java.util.Map;


public interface ProjectService {
    ProjectResponseDto addProject(String jwtToken, ProjectRegistrationRequestDto projectRegistrationRequestDto);
    String reassignProject(Long projectId, ProjectRegistrationRequestDto requestDto, String jwtToken);
    Map<String, String> completeProject(Long projectId);
    Map<String, String> archiveProject(Long projectId);
    List<ProjectResponseDto> getAllProjectsByDeveloper(ProjectRequestDto requestDto);
    List<ProjectResponseDto> getAllProjectsByAdmin(String jwtToken);
    List<ProjectResponseDto> getAllProjectsByStatus(ProjectRequestDto requestDto);
    List<ProjectResponseDto> getAllProjectsByCompany(String jwtToken);

}
