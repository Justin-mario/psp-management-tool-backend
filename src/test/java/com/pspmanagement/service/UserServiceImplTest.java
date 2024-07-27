package com.pspmanagement.service;

import com.pspmanagement.config.JwtTokenProvider;
import com.pspmanagement.dto.requestdto.ChangePasswordRequest;
import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.exception.ResourceExistException;
import com.pspmanagement.exception.UnauthorizedException;
import com.pspmanagement.model.entity.User;
import com.pspmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private RegistrationRequestDto requestDto;

    private User savedUser;

    @BeforeEach
    void setUp() {
        requestDto = new RegistrationRequestDto();
        requestDto.setId(1L);
        requestDto.setUsername("John");
        requestDto.setPassword("password");
        requestDto.setEmail("john@example.com");
        requestDto.setCompanyName("UH");
        requestDto.setRoles(Collections.singleton("ADMIN"));

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("John");
        savedUser.setPassword("encodedPassword");
        savedUser.setEmail("john@example.com");
        savedUser.setCompanyName("UH");
        savedUser.setRoles(Collections.singleton("ADMIN"));
    }

    @Test
    void testCreateAdmin() {

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegistrationResponseDto responseDto = userService.registerAsAdmin(requestDto);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("John", responseDto.getUsername());
        assertEquals("john@example.com", responseDto.getEmail());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password");
    }

    @Test
    void testCannotCreateTwoAdminForSameCompany() {
        // Setup for first admin
//        RegistrationRequestDto requestDto = new RegistrationRequestDto();
//        requestDto.setId(1L);
//        requestDto.setUsername("John");
//        requestDto.setPassword("password");
//        requestDto.setEmail("john@example.com");
//        requestDto.setCompanyName("UH");
//        requestDto.setRoles(Collections.singleton("ADMIN"));
//
//        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("John");
        savedUser.setPassword("encodedPassword");
        savedUser.setEmail("john@example.com");
        savedUser.setCompanyName("UH");
        savedUser.setRoles(Collections.singleton("ADMIN"));

        // Mock repository methods
        when(userRepository.existsByCompanyName("UH")).thenReturn(false).thenReturn(true);
        when(userRepository.findByCompanyName("UH")).thenReturn(savedUser);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Register first admin - should succeed
        RegistrationResponseDto responseDto = userService.registerAsAdmin(requestDto);
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("John", responseDto.getUsername());
        assertEquals("john@example.com", responseDto.getEmail());

        // Attempt to register second admin - should fail
        RegistrationRequestDto requestDto2 = new RegistrationRequestDto();
        requestDto2.setId(2L);
        requestDto2.setUsername("Peter");
        requestDto2.setPassword("password");
        requestDto2.setEmail("peter@example.com");
        requestDto2.setCompanyName("UH");
        requestDto2.setRoles(Collections.singleton("ADMIN"));

        assertThrows(ResourceExistException.class, () -> userService.registerAsAdmin(requestDto2));

        // Verify that save was called only once (for the first admin)
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void adminCanCreateDeveloper() {
        // Setup admin JWT
        String adminJwt = "admin-jwt-token";
        when(tokenProvider.validateToken(adminJwt)).thenReturn(true);
        when(tokenProvider.getUsernameFromJWT(adminJwt)).thenReturn("John");


        // Setup developer user
        User developerUser = new User();
        developerUser.setId(2L);
        developerUser.setUsername("Peter");
        developerUser.setEmail("peter@example.com");
        developerUser.setCompanyName("UH");
        developerUser.setRoles(Collections.singleton("DEVELOPER"));

        // Mock repository methods
        when(userRepository.findByUsername("John")).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(User.class))).thenReturn(developerUser);

        // Setup developer registration request
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setUsername("Peter");
        requestDto.setEmail("peter@example.com");
        requestDto.setCompanyName("UH");
        requestDto.setRoles(Collections.singleton("DEVELOPER"));

        // Execute the method
        RegistrationResponseDto responseDto = userService.registerDeveloper(requestDto, adminJwt);

        // Assertions
        assertNotNull(responseDto);
        assertEquals(2L, responseDto.getId());
        assertEquals("Peter", responseDto.getUsername());
        assertEquals("peter@example.com", responseDto.getEmail());
        assertEquals("UH", responseDto.getCompanyName());
        assertTrue(responseDto.getRoles().contains("DEVELOPER"));

        // Verify interactions
        verify(tokenProvider).validateToken(adminJwt);
        verify(tokenProvider).getUsernameFromJWT(adminJwt);
        verify(userRepository).findByUsername("John");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void developerCannotCreateDeveloper() {
        // Setup admin JWT
        String adminJwt = "admin-jwt-token";
        when(tokenProvider.validateToken(adminJwt)).thenReturn(true);
        when(tokenProvider.getUsernameFromJWT(adminJwt)).thenReturn("John");

        // Setup first developer user
        User developerUser = new User();
        developerUser.setId(1L);
        developerUser.setUsername("John");
        developerUser.setEmail("john@example.com");
        developerUser.setCompanyName("UH");
        developerUser.setRoles(Collections.singleton("DEVELOPER"));

        // Setup another developer user
        User anotherDeveloperUser = new User();
        anotherDeveloperUser.setId(2L);
        anotherDeveloperUser.setUsername("Peter");
        anotherDeveloperUser.setEmail("peter@example.com");
        anotherDeveloperUser.setCompanyName("UH");
        anotherDeveloperUser.setRoles(Collections.singleton("DEVELOPER"));

        // Mock repository methods
        when(userRepository.findByUsername("John")).thenReturn(Optional.of(developerUser));
//        when(userRepository.save(any(User.class))).thenReturn(developerUser);

        // Setup developer registration request
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setUsername("Peter");
        requestDto.setEmail("peter@example.com");
        requestDto.setCompanyName("UH");
        requestDto.setRoles(Collections.singleton("DEVELOPER"));

        assertThrows(UnauthorizedException.class, () -> userService.registerDeveloper(requestDto, adminJwt));

    }

    @Test
    void testChangePassword() {
        // Arrange
        ChangePasswordRequest changeRequest = new ChangePasswordRequest();
        changeRequest.setNewPassword("newPassword");
        String newPassword = "newPassword";
        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("newPasswordHash");

        // Act
        userService.changePassword(savedUser.getId(), changeRequest);

        // Assert
        verify(userRepository).findById(savedUser.getId());
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(savedUser);
        assertEquals("newPasswordHash", savedUser.getPassword());
    }

}