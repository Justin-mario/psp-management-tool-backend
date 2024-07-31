package com.pspmanagement.controller;

import com.pspmanagement.dto.requestdto.ProjectRegistrationRequestDto;
import com.pspmanagement.dto.requestdto.ProjectRequestDto;
import com.pspmanagement.dto.responsedto.ProjectResponseDto;
import com.pspmanagement.service.ProjectService;
import com.pspmanagement.util.Util;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/psp-management-tool/project")
@RestController
public class ProjectController {
    private final ProjectService projectService;
    private final Util util;

    public ProjectController(ProjectService projectService, Util util) {
        this.projectService = projectService;
        this.util = util;
    }

    @PostMapping("/add/{adminId}")
    public ResponseEntity<ProjectResponseDto> addProject(@Valid @PathVariable Long adminId,@RequestBody ProjectRegistrationRequestDto projectRegistrationRequestDto) {
        return new ResponseEntity<>(projectService.addProject(adminId, projectRegistrationRequestDto), HttpStatus.CREATED);
    }
    @PutMapping("/update/{projectId}")
    public ResponseEntity<?> reassignProject(@Valid  @PathVariable Long projectId, @RequestBody ProjectRegistrationRequestDto projectRegistrationRequestDto, @RequestHeader("Authorization") String authHeader) {
            // Extract the JWT token from the Authorization header
            String jwtToken = util.extractJwtToken(authHeader);
        return new ResponseEntity<>(projectService.reassignProject(projectId, projectRegistrationRequestDto, jwtToken), HttpStatus.CREATED);
    }

    @PutMapping("/complete/{projectId}")
    public ResponseEntity<?> completeProject(@Valid @PathVariable Long projectId) {
        return new ResponseEntity<>(projectService.completeProject(projectId), HttpStatus.OK);
    }
    @PutMapping("/archive/{projectId}")
    public ResponseEntity<?> cancelProject(@Valid @PathVariable Long projectId) {
        return new ResponseEntity<>(projectService.archiveProject(projectId), HttpStatus.OK);
    }

    @GetMapping("/get-all-developer-projects")
    public ResponseEntity<?> getAllDeveloperProject(@Valid @RequestBody ProjectRequestDto requestDto) {
        return new ResponseEntity<>(projectService.getAllProjectsByDeveloper(requestDto), HttpStatus.OK);
    }
    @GetMapping("/get-all-admin-projects")
    public ResponseEntity<?> getAllAdminProject(@RequestHeader("Authorization") String authHeader) {
        String jwtToken = util.extractJwtToken(authHeader);
        return new ResponseEntity<>(projectService.getAllProjectsByAdmin(jwtToken), HttpStatus.OK);
    }
    @GetMapping("/get-all-projects-by-status")
    public ResponseEntity<?> getAllProjectsByStatus(@Valid @RequestBody ProjectRequestDto requestDto) {
        return new ResponseEntity<>(projectService.getAllProjectsByStatus(requestDto), HttpStatus.OK);
    }
    @GetMapping("/get-all-projects-by-company")
    public ResponseEntity<?> getAllProjectsByCompany(@RequestHeader("Authorization") String authHeader) {
        String jwtToken = util.extractJwtToken(authHeader);
        return new ResponseEntity<>(projectService.getAllProjectsByCompany(jwtToken), HttpStatus.OK);
    }


}
