package com.pspmanagement.service;

import com.pspmanagement.dto.requestdto.ChangePasswordRequest;
import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.requestdto.LoginRequest;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.dto.responsedto.LoginResponse;
import java.util.List;


public interface UserService {
   RegistrationResponseDto registerAsAdmin(RegistrationRequestDto requestDto);
   RegistrationResponseDto registerDeveloper(RegistrationRequestDto requestDto, String adminUsername);
   String changePassword(Long userId, ChangePasswordRequest newPassword);
   LoginResponse authenticateUser(LoginRequest loginRequest);
   List<RegistrationResponseDto> getAllDeveloperByCompany(String jwtToken);
}
