package com.pspmanagement.service;

import com.pspmanagement.config.JwtTokenProvider;
import com.pspmanagement.config.UserDetailsImpl;
import com.pspmanagement.dto.requestdto.ChangePasswordRequest;
import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.requestdto.LoginRequest;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.dto.responsedto.LoginResponse;
import com.pspmanagement.exception.*;
import com.pspmanagement.model.entity.User;
import com.pspmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }
    @Override
    public RegistrationResponseDto registerAsAdmin(RegistrationRequestDto requestDto) {
        try {
            if (userRepository.existsByCompanyName(requestDto.getCompanyName())) {
                User existingUser = userRepository.findByCompanyName(requestDto.getCompanyName());
                if (existingUser != null && existingUser.getRoles().contains("ADMIN")) {
                    throw new ResourceExistException(requestDto.getCompanyName() + " already has an admin!");
                }
            }
            validateRegistration(requestDto);

            User admin = new User();
            admin .setUsername(requestDto.getUsername());
            admin .setEmail(requestDto.getEmail());
            String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
            admin .setPassword(encodedPassword);
            admin .setRoles(Collections.singleton("ADMIN"));
            admin .setCompanyName(requestDto.getCompanyName());
            admin.setPspLevel(0);
            User user = userRepository.save(admin );
            // Create UserDetailsImpl instance
            UserDetailsImpl userDetails = new UserDetailsImpl(admin);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            String jwt = tokenProvider.generateToken(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new RegistrationResponseDto(jwt,user);
        }
        catch (ResourceExistException e) {
            throw new ResourceExistException(e.getMessage());
        }


    }


    @Override
    public RegistrationResponseDto registerDeveloper(RegistrationRequestDto requestDto, String jwtToken) {
        try {
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
                throw new UnauthorizedException("Only admins can create developer accounts");
            }
            // Check if the company of the admin matches the company in the request
            if (!admin.getCompanyName().equals(requestDto.getCompanyName())) {
                throw new ForbiddenException("Admin can only create developers for their own company");
            }
            validateRegistration(requestDto);
            User developer = new User();
            developer .setUsername(requestDto.getUsername());
            developer .setEmail(requestDto.getEmail());
            String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
            developer .setPassword(encodedPassword);
            developer .setRoles(Collections.singleton("DEVELOPER"));
            developer .setCompanyName(admin.getCompanyName());
            developer.setPspLevel(0);
            return new RegistrationResponseDto(userRepository.save(developer ));
        }
        catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        }
        catch (ForbiddenException e) {
            throw new ForbiddenException(e.getMessage());
        }

    }


    private void validateRegistration(RegistrationRequestDto requestDto) {
        try {
            if (userRepository.existsByUsername(requestDto.getUsername())) {
                throw new ConflictException(requestDto.getUsername() + " is already taken!");
            }
            if (userRepository.existsByEmail(requestDto.getEmail())) {
                throw new ConflictException(requestDto.getEmail() + " has been registered!");
            }
        }
        catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        }


    }

    @Override
    public String changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
            return "Password changed successfully";
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            return new LoginResponse(jwt, userDetails.getUsername(), roles);
        }
        catch (Exception e) {
            throw new UnauthorizedException("Invalid username or password");
        }

    }
}
