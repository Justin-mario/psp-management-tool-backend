package com.pspmanagement.service;

import com.pspmanagement.config.JwtTokenProvider;
import com.pspmanagement.dto.requestdto.ProjectRegistrationRequestDto;
import com.pspmanagement.dto.responsedto.ProjectResponseDto;
import com.pspmanagement.exception.ForbiddenException;
import com.pspmanagement.exception.ResourceNotFoundException;
import com.pspmanagement.model.constant.ProjectStatus;
import com.pspmanagement.model.entity.Project;
import com.pspmanagement.model.entity.User;
import com.pspmanagement.repository.ProjectRepository;
import com.pspmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ProjectServiceImplTest {

        @Mock
        private ProjectRepository projectRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private JwtTokenProvider jwtTokenProvider;

        private ProjectServiceImpl projectService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            projectService = new ProjectServiceImpl(projectRepository, userRepository, jwtTokenProvider);
        }

        @Test
        void addProject_Success() {
            // Arrange
            ProjectRegistrationRequestDto requestDto = new ProjectRegistrationRequestDto();
            Long adminId = 1L;
            requestDto.setProjectName("Test Project");
            requestDto.setProjectDeveloper("developer");
            requestDto.setProgrammingLanguage("Java");
            requestDto.setProjectDescription("Test Description");

            User admin = new User();
            admin.setId(1L);
            admin.setCompanyName("Test Company");

            User developer = new User();
            developer.setUsername("developer");
            developer.setCompanyName("Test Company");

            when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
            when(userRepository.findByUsername("developer")).thenReturn(Optional.of(developer));
            when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            ProjectResponseDto responseDto = projectService.addProject(adminId,requestDto);

            // Assert
            assertNotNull(responseDto);
            assertEquals("Test Project", responseDto.getProjectName());
            assertEquals(ProjectStatus.IN_PROGRESS, responseDto.getProjectStatus());
            verify(projectRepository, times(1)).save(any(Project.class));
        }

        @Test
        void addProject_AdminNotFound() {
            // Arrange
            ProjectRegistrationRequestDto requestDto = new ProjectRegistrationRequestDto();
            Long adminId = 1L;

            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> projectService.addProject(adminId,requestDto));
        }

        @Test
        void addProject_DeveloperNotFound() {
            // Arrange
            ProjectRegistrationRequestDto requestDto = new ProjectRegistrationRequestDto();
            Long adminId = 1L;
            requestDto.setProjectDeveloper("developer");

            User admin = new User();
            admin.setId(1L);

            when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
            when(userRepository.findByUsername("developer")).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> projectService.addProject(adminId,requestDto));
        }

        @Test
        void addProject_ForbiddenCompanyMismatch() {
            // Arrange
            ProjectRegistrationRequestDto requestDto = new ProjectRegistrationRequestDto();
            Long adminId = 1L;
            requestDto.setProjectDeveloper("developer");

            User admin = new User();
            admin.setId(1L);
            admin.setCompanyName("Company A");

            User developer = new User();
            developer.setUsername("developer");
            developer.setCompanyName("Company B");

            when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
            when(userRepository.findByUsername("developer")).thenReturn(Optional.of(developer));

            // Act & Assert
            assertThrows(ForbiddenException.class, () -> projectService.addProject(adminId,requestDto));
        }

}