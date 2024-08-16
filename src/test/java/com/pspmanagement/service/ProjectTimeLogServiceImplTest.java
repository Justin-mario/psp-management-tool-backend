package com.pspmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import com.pspmanagement.exception.ResourceNotFoundException;
import com.pspmanagement.model.entity.Project;
import com.pspmanagement.model.entity.ProjectTimeLog;
import com.pspmanagement.repository.ProjectRepository;
import com.pspmanagement.repository.ProjectTimeLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectTimeLogServiceImplTest {

        @Mock
        private ProjectRepository projectRepository;

        @Mock
        private ProjectTimeLogRepository projectTimeLogRepository;

        @InjectMocks
        private ProjectTimeLogServiceImpl projectTimeLogService;

        @Mock
        private Project project;
        private ProjectTimeLog projectTimeLog;

        @BeforeEach
        void setUp() {
            project = new Project();
            project.setProjectId(1L);

            projectTimeLog = new ProjectTimeLog();
            projectTimeLog.setProject(project);
            projectTimeLog.setStartTime(LocalDateTime.now());
        }

        @Test
        void startProject_shouldStartProjectSuccessfully() {
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(projectTimeLogRepository.save(any(ProjectTimeLog.class))).thenReturn(projectTimeLog);

            Boolean result = projectTimeLogService.startProject(1L);

            assertTrue(result);
            verify(projectTimeLogRepository, times(1)).save(any(ProjectTimeLog.class));
        }

        @Test
        void startProject_shouldThrowResourceNotFoundException() {
            when(projectRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> projectTimeLogService.startProject(1L));
        }

        @Test
        void endProject_shouldEndProjectSuccessfully() {
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(projectTimeLogRepository.findTopByProjectAndEndTimeIsNullOrderByStartTimeDesc(project))
                    .thenReturn(Optional.of(projectTimeLog));

            Boolean result = projectTimeLogService.endProject(1L);

            assertTrue(result);
            verify(projectTimeLogRepository, times(1)).save(any(ProjectTimeLog.class));
            assertNotNull(projectTimeLog.getEndTime());
        }

        @Test
        void endProject_shouldThrowIllegalStateException() {
            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
            when(projectTimeLogRepository.findTopByProjectAndEndTimeIsNullOrderByStartTimeDesc(project))
                    .thenReturn(Optional.empty());

            assertThrows(IllegalStateException.class, () -> projectTimeLogService.endProject(1L));
        }
//
//        @Test
//        void getProjectDuration_shouldReturnDuration() {
//            when(projectRepository.findByIdWithTimeLogs(1L)).thenReturn(Optional.of(project));
//            when(project.calculateDuration()).thenReturn("2 hours 30 minutes");
//
//            String result = projectTimeLogService.getProjectDuration(1L);
//
//            assertEquals("2 hours 30 minutes", result);
//        }

        @Test
        void getProjectDuration_shouldThrowResourceNotFoundException() {
            when(projectRepository.findByIdWithTimeLogs(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> projectTimeLogService.getProjectDuration(1L));
        }
    }

