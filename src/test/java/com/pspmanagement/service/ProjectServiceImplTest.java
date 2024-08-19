package com.pspmanagement.service;

import com.pspmanagement.config.JwtTokenProvider;
import com.pspmanagement.dto.requestdto.ProjectRegistrationRequestDto;
import com.pspmanagement.dto.requestdto.ProjectRequestDto;
import com.pspmanagement.dto.responsedto.ProjectResponseDto;
import com.pspmanagement.exception.ResourceNotFoundException;
import com.pspmanagement.exception.UnauthorizedException;
import com.pspmanagement.model.constant.ProjectPhase;
import com.pspmanagement.model.constant.ProjectStatus;
import com.pspmanagement.model.entity.Project;
import com.pspmanagement.model.entity.User;
import com.pspmanagement.repository.ProjectRepository;
import com.pspmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

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
        String jwtToken = "validToken";
        requestDto.setProjectName("Test Project");
        requestDto.setProjectDeveloper("developer");
        requestDto.setProgrammingLanguage("Java");
        requestDto.setProjectDescription("Test Description");
        requestDto.setProjectPhase(ProjectPhase.valueOf("PLANNING"));

        User admin = new User();
        admin.setUsername("admin");
        admin.setCompanyName("Test Company");

        User developer = new User();
        developer.setUsername("developer");
        developer.setCompanyName("Test Company");

        when(jwtTokenProvider.validateToken(jwtToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(jwtToken)).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(userRepository.findByUsername("developer")).thenReturn(Optional.of(developer));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // Act
        ProjectResponseDto responseDto = projectService.addProject(jwtToken, requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals("Test Project", responseDto.getProjectName());
        assertEquals(ProjectStatus.IN_PROGRESS, responseDto.getProjectStatus());
        assertTrue(responseDto.getDefects().isEmpty());
        verify(projectRepository, times(1)).save(any(Project.class));

    }

    @Test
    void addProject_InvalidToken() {
        // Arrange
        ProjectRegistrationRequestDto requestDto = new ProjectRegistrationRequestDto();
        String jwtToken = "invalidToken";

        when(jwtTokenProvider.validateToken(jwtToken)).thenReturn(false);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> projectService.addProject(jwtToken, requestDto));
    }

    @Test
    void addProject_AdminNotFound() {
        // Arrange
        ProjectRegistrationRequestDto requestDto = new ProjectRegistrationRequestDto();
        String jwtToken = "validToken";

        when(jwtTokenProvider.validateToken(jwtToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(jwtToken)).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> projectService.addProject(jwtToken, requestDto));
    }

    @Test
    void addProject_DeveloperNotFound() {
        // Arrange
        ProjectRegistrationRequestDto requestDto = new ProjectRegistrationRequestDto();
        String jwtToken = "validToken";
        requestDto.setProjectDeveloper("developer");

        User admin = new User();
        admin.setUsername("admin");

        when(jwtTokenProvider.validateToken(jwtToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(jwtToken)).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(userRepository.findByUsername("developer")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> projectService.addProject(jwtToken, requestDto));
    }

    @Test
    void reassignProject_Success() {
        // Arrange
        Long projectId = 1L;
        ProjectRegistrationRequestDto requestDto = new ProjectRegistrationRequestDto();
        requestDto.setProjectDeveloper("newDeveloper");
        String jwtToken = "validToken";

        when(jwtTokenProvider.validateToken(jwtToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(jwtToken)).thenReturn("adminUser");

        User admin = new User();
        admin.setUsername("adminUser");
        admin.setRoles(Collections.singleton("ADMIN"));
        when(userRepository.findByUsername("adminUser")).thenReturn(Optional.of(admin));

        Project project = new Project();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        User newDeveloper = new User();
        newDeveloper.setUsername("newDeveloper");
        when(userRepository.findByUsername("newDeveloper")).thenReturn(Optional.of(newDeveloper));

        // Act
        String result = projectService.reassignProject(projectId, requestDto, jwtToken);
        // Assert
        assertEquals("Project successfully Reassigned", result);
        verify(projectRepository).save(project);
        assertEquals(newDeveloper, project.getProjectDeveloper());
    }

    @Test
    void reassignProject_InvalidToken() {
        // Arrange
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);
        // Act & Assert
        assertThrows(UnauthorizedException.class, () ->
                projectService.reassignProject(1L, new ProjectRegistrationRequestDto(), "invalidToken")
        );
    }

    @Test
    void completeProject_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        // Act
        Map<String, String> result = projectService.completeProject(projectId);
        // Assert
        assertEquals("Project successfully completed", result.get("message"));
        assertEquals(ProjectStatus.COMPLETED, project.getProjectStatus());
        assertNotNull(project.getEndDate());
        verify(projectRepository).save(project);
    }

    @Test
    void archiveProject_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        // Act
        Map<String, String> result = projectService.archiveProject(projectId);
        // Assert
        assertEquals("Project successfully archived", result.get("message"));
        assertEquals(ProjectStatus.ARCHIVED, project.getProjectStatus());
        verify(projectRepository).save(project);
    }

    private Project createTestProject(String name, User developer, User admin) {
        Project project = new Project();
        project.setProjectName(name);
        project.setProjectDeveloper(developer);
        project.setProjectAdmin(admin);
        project.setProjectStatus(ProjectStatus.IN_PROGRESS);
        project.setStartDate(LocalDateTime.now());
        return project;
    }

    @Test
    void getAllProjectsByDeveloper_Success() {
        // Arrange
        ProjectRequestDto requestDto = new ProjectRequestDto();
        requestDto.setProjectDeveloper("developer");

        User developer = new User();
        developer.setUsername("developer");

        User admin = new User();
        admin.setUsername("admin");

        List<Project> projects = Arrays.asList(
                createTestProject("Project 1", developer, admin),
                createTestProject("Project 2", developer, admin)
        );
        when(projectRepository.findAllByProjectDeveloper_Username("developer")).thenReturn(projects);

        // Act
        List<ProjectResponseDto> result = projectService.getAllProjectsByDeveloper(requestDto);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getProjectName());
        assertEquals("Project 2", result.get(1).getProjectName());
    }

    @Test
    void getAllProjectsByAdmin_Success() {
        // Arrange
        String jwtToken = "validToken";
        when(jwtTokenProvider.getUsernameFromJWT(jwtToken)).thenReturn("adminUser");

        User admin = new User();
        admin.setUsername("adminUser");
        admin.setRoles(Collections.singleton("ADMIN"));
        when(userRepository.findByUsername("adminUser")).thenReturn(Optional.of(admin));

        User developer = new User();
        developer.setUsername("developer");

        List<Project> projects = Arrays.asList(
                createTestProject("Project 1", developer, admin),
                createTestProject("Project 2", developer, admin)
        );
        when(projectRepository.findAllByProjectAdmin_Username("adminUser")).thenReturn(projects);
        // Act
        List<ProjectResponseDto> result = projectService.getAllProjectsByAdmin(jwtToken);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getProjectName());
        assertEquals("Project 2", result.get(1).getProjectName());
    }

    @Test
    void getAllProjectsByStatus_Success() {
        // Arrange
        ProjectRequestDto requestDto = new ProjectRequestDto();
        requestDto.setProjectStatus("IN_PROGRESS");

        User developer = new User();
        developer.setUsername("developer");

        User admin = new User();
        admin.setUsername("admin");

        List<Project> projects = Arrays.asList(
                createTestProject("Project 1", developer, admin),
                createTestProject("Project 2", developer, admin)
        );
        when(projectRepository.findAllByProjectStatus(ProjectStatus.IN_PROGRESS)).thenReturn(projects);

        // Act
        List<ProjectResponseDto> result = projectService.getAllProjectsByStatus(requestDto);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getProjectName());
        assertEquals("Project 2", result.get(1).getProjectName());
    }

    @Test
    void getAllProjectsByCompany_Success() {
        // Arrange
        String jwtToken = "validToken";
        when(jwtTokenProvider.getUsernameFromJWT(jwtToken)).thenReturn("adminUser");

        User admin = new User();
        admin.setUsername("adminUser");
        admin.setCompanyName("TestCompany");
        when(userRepository.findByUsername("adminUser")).thenReturn(Optional.of(admin));

        User developer = new User();
        developer.setUsername("developer");

        List<Project> projects = Arrays.asList(
                createTestProject("Project 1", developer, admin),
                createTestProject("Project 2", developer, admin)
        );
        when(projectRepository.findAllByProjectAdmin_CompanyName("TestCompany")).thenReturn(projects);
        // Act
        List<ProjectResponseDto> result = projectService.getAllProjectsByCompany(jwtToken);
        // Assert
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getProjectName());
        assertEquals("Project 2", result.get(1).getProjectName());
    }
}