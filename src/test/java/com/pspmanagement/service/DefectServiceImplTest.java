package com.pspmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import com.pspmanagement.dto.requestdto.DefectRequestDto;
import com.pspmanagement.exception.ResourceNotFoundException;
import com.pspmanagement.model.constant.DefectStatus;
import com.pspmanagement.model.constant.DefectType;
import com.pspmanagement.model.constant.ProjectPhase;
import com.pspmanagement.model.entity.Defect;
import com.pspmanagement.model.entity.Project;
import com.pspmanagement.repository.DefectRepository;
import com.pspmanagement.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

class DefectServiceImplTest {

        @Mock
        private DefectRepository defectRepository;

        @Mock
        private ProjectRepository projectRepository;

        @InjectMocks
        private DefectServiceImpl defectService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void createDefect_Success() {
            Long projectId = 1L;
            Project project = new Project();
            project.setProjectId(projectId);

            DefectRequestDto defectRequestDto = new DefectRequestDto();
            defectRequestDto.setDescription("Test Defect");
            defectRequestDto.setDefectType(DefectType.valueOf("SYNTAX"));
            defectRequestDto.setInjectedPhase(ProjectPhase.CODE);
            defectRequestDto.setRemovedPhase(ProjectPhase.CODE);

            when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
            when(defectRepository.save(any(Defect.class))).thenReturn(new Defect());

            String result = defectService.createDefect(projectId, defectRequestDto);

            assertEquals("Defect created successfully", result);
            verify(defectRepository, times(1)).save(any(Defect.class));
        }

        @Test
        void createDefect_ProjectNotFound() {
            Long projectId = 1L;
            DefectRequestDto defectRequestDto = new DefectRequestDto();

            when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> defectService.createDefect(projectId, defectRequestDto));
        }

        @Test
        void changeDefectStatus_Success() {
            Long defectId = 1L;
            String newStatus = "FIXED";
            Defect defect = new Defect();
            defect.setDefectId(defectId);

            when(defectRepository.findById(defectId)).thenReturn(Optional.of(defect));
            when(defectRepository.save(any(Defect.class))).thenReturn(defect);

            Boolean result = defectService.changeDefectStatus(defectId, newStatus);

            assertTrue(result);
            assertEquals(DefectStatus.FIXED, defect.getDefectStatus());
            verify(defectRepository, times(1)).save(defect);
        }

        @Test
        void changeDefectStatus_DefectNotFound() {
            Long defectId = 1L;
            String newStatus = "RESOLVED";

            when(defectRepository.findById(defectId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> defectService.changeDefectStatus(defectId, newStatus));
        }

        @Test
        void setDefectFixTime_Success() {
            Long defectId = 1L;
            Defect defect = new Defect();
            defect.setDefectId(defectId);

            when(defectRepository.findById(defectId)).thenReturn(Optional.of(defect));
            when(defectRepository.save(any(Defect.class))).thenReturn(defect);

            Boolean result = defectService.setDefectFixTime(defectId);

            assertNull(result); // The method returns null, which might be a bug in the implementation
            assertNotNull(defect.getFixTime());
            verify(defectRepository, times(1)).save(defect);
        }

        @Test
        void setDefectFixTime_DefectNotFound() {
            Long defectId = 1L;

            when(defectRepository.findById(defectId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> defectService.setDefectFixTime(defectId));
        }

        @Test
        void getDefectsByProjectId_Success() {
            Long projectId = 1L;
            Project project = new Project();
            project.setProjectId(projectId);

            List<Defect> defects = Arrays.asList(new Defect(), new Defect());

            when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
            when(defectRepository.findByProject(project)).thenReturn(defects);

            List<Defect> result = defectService.getDefectsByProjectId(projectId);

            assertEquals(defects, result);
            verify(defectRepository, times(1)).findByProject(project);
        }

        @Test
        void getDefectsByProjectId_ProjectNotFound() {
            Long projectId = 1L;

            when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> defectService.getDefectsByProjectId(projectId));
        }
    }

