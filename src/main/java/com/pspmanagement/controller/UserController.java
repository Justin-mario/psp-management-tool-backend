package com.pspmanagement.controller;

import com.pspmanagement.dto.requestdto.AdminRegistrationRequestDto;
import com.pspmanagement.dto.responsedto.AdminRegistrationResponseDto;
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

    @Autowired
    public UserController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
    @GetMapping("/test")
    public String test() {
        return "test";
    }
    @PostMapping("/register-admin")
    public ResponseEntity<AdminRegistrationResponseDto> registerAsAdmin(@Valid @RequestBody AdminRegistrationRequestDto requestDto) {
        return new ResponseEntity<>(userServiceImpl.registerAsAdmin(requestDto), HttpStatus.CREATED);
    }

}
