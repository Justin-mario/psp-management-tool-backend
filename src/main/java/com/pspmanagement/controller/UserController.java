package com.pspmanagement.controller;

import com.pspmanagement.dto.requestdto.ChangePasswordRequest;
import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.requestdto.LoginRequest;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.dto.responsedto.LoginResponse;
import com.pspmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/psp-management-tool/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<RegistrationResponseDto> registerAsAdmin(@Valid @RequestBody RegistrationRequestDto requestDto) {
        return new ResponseEntity<>(userService.registerAsAdmin(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/register-developer")
    public ResponseEntity<RegistrationResponseDto> registerDeveloper(
            @Valid @RequestBody RegistrationRequestDto requestDto,
            @RequestHeader("Authorization") String authHeader) {
        // Extract the JWT token from the Authorization header
        String jwtToken = extractJwtToken(authHeader);
        return new ResponseEntity<>(userService.registerDeveloper(requestDto, jwtToken), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(id, changePasswordRequest);
        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }

    private String extractJwtToken(String authHeader) {
        // Check if the header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the token (remove "Bearer " prefix)
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}