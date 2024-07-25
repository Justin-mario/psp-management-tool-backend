package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.model.entity.User;
import com.pspmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

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

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreateAdmin() {
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setId(1L);
        requestDto.setUsername("John");
        requestDto.setPassword("password");
        requestDto.setEmail("john@example.com");
        requestDto.setCompanyName("UH");
        requestDto.setRoles(Collections.singleton("ADMIN"));

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("John");
        savedUser.setPassword("encodedPassword");
        savedUser.setEmail("john@example.com");
        savedUser.setCompanyName("UH");
        savedUser.setRoles(Collections.singleton("ADMIN"));

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
}