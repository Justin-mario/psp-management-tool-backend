package com.pspmanagement.controller;

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
    private final UserService userServiceImpl;
    private final UserService userService;
    @Autowired
    public UserController(UserService userServiceImpl, UserService userService) {
        this.userServiceImpl = userServiceImpl;
        this.userService = userService;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<RegistrationResponseDto> registerAsAdmin(@Valid @RequestBody RegistrationRequestDto requestDto) {
        return new ResponseEntity<>(userServiceImpl.registerAsAdmin(requestDto), HttpStatus.CREATED);
    }
    @PostMapping("/register-developer")
    public ResponseEntity<RegistrationResponseDto> registerDeveloper(@Valid @RequestBody RegistrationRequestDto requestDto) {
        return new ResponseEntity<>(userServiceImpl.registerDeveloper(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

}
