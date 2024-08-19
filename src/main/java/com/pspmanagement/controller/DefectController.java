package com.pspmanagement.controller;

import com.pspmanagement.dto.requestdto.DefectRequestDto;
import com.pspmanagement.dto.responsedto.ProjectResponseDto;
import com.pspmanagement.model.entity.Defect;
import com.pspmanagement.service.DefectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/psp-management-tool/defect")
public class DefectController {

    private final DefectService defectService;

    public DefectController(DefectService defectService) {
        this.defectService = defectService;
    }

    @PostMapping("/create/{projectId}")
    public ResponseEntity<String> createDefect(@PathVariable Long projectId, @RequestBody DefectRequestDto defectRequestDto) {
        return new ResponseEntity<>(defectService.createDefect(projectId, defectRequestDto), HttpStatus.CREATED);
    }
    @PutMapping("/change-status/{defectId}")
    public ResponseEntity<Boolean> changeDefectStatus(@PathVariable Long defectId, @RequestBody DefectRequestDto newStatus) {
        return new ResponseEntity<> (defectService.changeDefectStatus(defectId, newStatus),HttpStatus.OK);
    }
    @PutMapping("/set-fix-time/{defectId}")
    public ResponseEntity<Boolean> setDefectFixTime(@PathVariable Long defectId) {
        return new ResponseEntity<> (defectService.setDefectFixTime(defectId),HttpStatus.OK);
    }
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?>getDefectsByProjectId(@PathVariable Long projectId) {
        return new ResponseEntity<>(defectService.getDefectsByProjectId(projectId),HttpStatus.OK);
    }


}
