package com.pspmanagement.service;

import com.pspmanagement.config.JwtTokenProvider;
import com.pspmanagement.dto.requestdto.ProjectRegistrationRequestDto;
import com.pspmanagement.dto.requestdto.ProjectRequestDto;
import com.pspmanagement.dto.responsedto.ProjectResponseDto;
import com.pspmanagement.exception.BadRequestException;
import com.pspmanagement.exception.ForbiddenException;
import com.pspmanagement.exception.ResourceNotFoundException;
import com.pspmanagement.exception.UnauthorizedException;
import com.pspmanagement.model.constant.ProjectStatus;
import com.pspmanagement.model.entity.Project;
import com.pspmanagement.model.entity.User;
import com.pspmanagement.repository.ProjectRepository;
import com.pspmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;


    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public ProjectResponseDto addProject(Long adminId, ProjectRegistrationRequestDto projectRegistrationRequestDto) {
        validateProjectRequest(adminId, projectRegistrationRequestDto);
        try {
            Project project = new Project();
            project.setProjectName(projectRegistrationRequestDto.getProjectName());
            User admin = findAdminById(adminId);
            User developer = findDeveloperByUsername(projectRegistrationRequestDto.getProjectDeveloper());
            project.setProjectDeveloper(developer);
            project.setProjectAdmin(admin);
            project.setProgrammingLanguage(projectRegistrationRequestDto.getProgrammingLanguage());
            project.setProjectDescription(projectRegistrationRequestDto.getProjectDescription());
            project.setProjectStatus(ProjectStatus.IN_PROGRESS);
            project.setStartDate(LocalDateTime.now());
            project.setProjecPhase(projectRegistrationRequestDto.getProjectPhase());
            projectRepository.save(project);

        return new ProjectResponseDto(project);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        }

    @Override
    @Transactional
    public String reassignProject(Long projectId, ProjectRegistrationRequestDto requestDto, String jwtToken) {
        try{
            // Validate the JWT token
            if (!tokenProvider.validateToken(jwtToken)) {
                throw new UnauthorizedException("Invalid or expired token");
            }
            // Extract the admin's username from the token
            String adminUsername = tokenProvider.getUsernameFromJWT(jwtToken);
            // Verify if the admin exists and has the right role
            User admin = userRepository.findByUsername(adminUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
            if (!admin.getRoles().contains("ADMIN")) {
                throw new UnauthorizedException("Only admins can reassign project");
            }
            Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            User developer = findDeveloperByUsername(requestDto.getProjectDeveloper());
            project.setProjectDeveloper(developer);
            projectRepository.save(project);
        }
       catch (UnauthorizedException e){
           throw new UnauthorizedException(e.getMessage());
       }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage());
        }

        return "Project successfully Reassigned";
    }

    @Override
    @Transactional
    public Map<String, String> completeProject(Long projectId) {
        try{
            Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            project.setProjectStatus(ProjectStatus.COMPLETED);
            project.setEndDate(LocalDateTime.now());
            projectRepository.save(project);
            Map<String, String> status =  new HashMap<>();
            status.put("message", "Project successfully completed");
            return status;
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public Map<String, String> archiveProject(Long projectId) {
        try{
            Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
            project.setProjectStatus(ProjectStatus.ARCHIVED);
            projectRepository.save(project);
            Map<String, String> status =  new HashMap<>();
            status.put("message", "Project successfully archived");
            return status;
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public List<ProjectResponseDto> getAllProjectsByDeveloper(ProjectRequestDto requestDto) {
        try {
            List<Project> foundProjects = projectRepository.findAllByProjectDeveloper_Username(requestDto.getProjectDeveloper());
            return foundProjects.stream().map(ProjectResponseDto::new).toList();
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public List<ProjectResponseDto> getAllProjectsByAdmin(String jwtToken) {
        try {
            String adminName = tokenProvider.getUsernameFromJWT(jwtToken);
            User userAdmin = userRepository.findByUsername(adminName).get();
            if (!userAdmin.getRoles().contains("ADMIN")) {
                throw new UnauthorizedException("Only admins can access all projects");
            }
            List<Project> foundProjects = projectRepository.findAllByProjectAdmin_Username(adminName);
            return foundProjects.stream().map(ProjectResponseDto::new).toList();
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<ProjectResponseDto> getAllProjectsByStatus(ProjectRequestDto requestDto) {
        try {
            ProjectStatus status = ProjectStatus.valueOf(requestDto.getProjectStatus());
            List<Project> foundProjects = projectRepository.findAllByProjectStatus(status);
            return foundProjects.stream().map(ProjectResponseDto::new).toList();
        }
        catch (BadRequestException e){
            throw new BadRequestException("Invalid project status");
        }

    }

    @Override
    @Transactional
    public List<ProjectResponseDto> getAllProjectsByCompany(String jwtToken) {
        try {
            String adminName = tokenProvider.getUsernameFromJWT(jwtToken);
            Optional<User> userAdmin = userRepository.findByUsername(adminName);
            if (userAdmin.isEmpty()) throw new ResourceNotFoundException("Company not found");
            String userAdminCompany = userAdmin.get().getCompanyName();
            List<Project> foundProjects = projectRepository.findAllByProjectAdmin_CompanyName(userAdminCompany);
            return foundProjects.stream().map(ProjectResponseDto::new).toList();
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }







    private User findAdminById(Long adminId) {
        return userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
    }
    private User findDeveloperByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
    }

    private void validateProjectRequest(Long adminId, ProjectRegistrationRequestDto projectRegistrationRequestDto) {
        try {
            Optional<User> admin = userRepository.findById(adminId);
            if (admin.isEmpty()) {
                throw new ResourceNotFoundException("Admin not found");
            }
            Optional<User> developer =  userRepository.findByUsername(projectRegistrationRequestDto.getProjectDeveloper());
            if (developer.isEmpty()) {
                throw new ResourceNotFoundException("Developer not found");
            }
            if(!admin.get().getCompanyName().equals(developer.get().getCompanyName())){
                throw new ForbiddenException("Admin can only create projects for their own company");
            }


        }catch (ForbiddenException e){
            throw new ForbiddenException(e.getMessage());
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    }



