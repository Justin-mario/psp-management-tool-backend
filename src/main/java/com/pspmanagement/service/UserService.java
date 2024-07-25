package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.AdminRegistrationRequestDto;
import com.pspmanagement.dto.requestdto.LoginRequest;
import com.pspmanagement.dto.responsedto.AdminRegistrationResponseDto;
import com.pspmanagement.dto.responsedto.LoginResponse;

public interface UserService {
   AdminRegistrationResponseDto registerAsAdmin(AdminRegistrationRequestDto requestDto);
   LoginResponse authenticateUser(LoginRequest loginRequest);
}
