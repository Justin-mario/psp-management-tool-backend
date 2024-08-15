package com.pspmanagement.controller;

import com.pspmanagement.dto.requestdto.ChangePasswordRequest;
import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.requestdto.LoginRequest;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.dto.responsedto.LoginResponse;
import com.pspmanagement.service.UserService;
import com.pspmanagement.util.Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/psp-management-tool/user")
public class UserController {
    private final UserService userService;
    private final Util util;
    @Autowired
    public UserController(UserService userService, Util util) {
        this.userService = userService;
        this.util = util;
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
        String jwtToken = util.extractJwtToken(authHeader);
        return new ResponseEntity<>(userService.registerDeveloper(requestDto, jwtToken), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(id, changePasswordRequest);
        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }

    @GetMapping("/get-all-developers")
    public ResponseEntity<?> getAllDevelopers(@RequestHeader("Authorization") String authHeader) {
        String jwtToken = util.extractJwtToken(authHeader);
        return ResponseEntity.ok(userService.getAllDeveloperByCompany(jwtToken));
    }

}