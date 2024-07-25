package com.pspmanagement.service;

import com.pspmanagement.config.JwtTokenProvider;
import com.pspmanagement.config.UserDetailsImpl;
import com.pspmanagement.dto.requestdto.AdminRegistrationRequestDto;
import com.pspmanagement.dto.requestdto.LoginRequest;
import com.pspmanagement.dto.responsedto.AdminRegistrationResponseDto;
import com.pspmanagement.dto.responsedto.LoginResponse;
import com.pspmanagement.model.entity.User;
import com.pspmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public AdminRegistrationResponseDto registerAsAdmin(AdminRegistrationRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        User admin = new User();
        admin.setUsername(requestDto.getUsername());
        admin.setEmail(requestDto.getEmail());
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        admin.setPassword(encodedPassword);
        admin.setRole(requestDto.getRole());
        admin.setCompanyName(requestDto.getCompanyName());

        return new AdminRegistrationResponseDto(userRepository.save(admin));
    }

//    public void registerUser(SignUpRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            throw new RuntimeException("Username is already taken!");
//        }
//
//        User user = new User();
//        user.setUsername(signUpRequest.getUsername());
//        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
//        user.setRoles(signUpRequest.getRoles());
//
//        userRepository.save(user);
//    }

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
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
}
