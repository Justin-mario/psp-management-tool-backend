package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.requestdto.LoginRequest;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.dto.responsedto.LoginResponse;

public interface UserService {
   RegistrationResponseDto registerAsAdmin(RegistrationRequestDto requestDto);
   RegistrationResponseDto registerDeveloper(RegistrationRequestDto requestDto);
   LoginResponse authenticateUser(LoginRequest loginRequest);
}
