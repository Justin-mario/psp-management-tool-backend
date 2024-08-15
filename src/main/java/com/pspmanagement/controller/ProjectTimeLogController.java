package com.pspmanagement.controller;

import com.pspmanagement.service.ProjectTimeLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/psp-management-tool/project-time-log")
@RestController
public class ProjectTimeLogController {
    private final ProjectTimeLogService projectTimeLogService;


    public ProjectTimeLogController(ProjectTimeLogService projectTimeLogService) {
        this.projectTimeLogService = projectTimeLogService;
    }

    @PostMapping("/start/{projectId}")
    public ResponseEntity<Boolean> startProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectTimeLogService.startProject(projectId));
    }

    @PutMapping("/end/{projectId}")
    public ResponseEntity<Boolean> endProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectTimeLogService.endProject(projectId));
    }

    @GetMapping("/get-project-duration/{projectId}")
    public ResponseEntity<String> getProjectDuration(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectTimeLogService.getProjectDuration(projectId));
    }
}
