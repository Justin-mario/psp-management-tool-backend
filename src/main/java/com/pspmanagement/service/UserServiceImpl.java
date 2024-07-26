package com.pspmanagement.service;

import com.pspmanagement.config.JwtTokenProvider;
import com.pspmanagement.config.UserDetailsImpl;
import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.dto.requestdto.LoginRequest;
import com.pspmanagement.dto.responsedto.RegistrationResponseDto;
import com.pspmanagement.dto.responsedto.LoginResponse;
import com.pspmanagement.exception.ConflictException;
import com.pspmanagement.exception.ResourceExistException;
import com.pspmanagement.model.entity.User;
import com.pspmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if(userRepository.existsByCompanyName(requestDto.getCompanyName())){
            User user = userRepository.findByCompanyName(requestDto.getCompanyName());
            String adminRole = user.getRoles().stream().filter(role -> role.equals("ADMIN")).toString();
            if(adminRole != null){
                throw new ResourceExistException(requestDto.getCompanyName() + " has an admin!");
            }
        }
        return getRegistrationResponseDto(requestDto);
    }

    @Override
    public RegistrationResponseDto registerDeveloper(RegistrationRequestDto requestDto) {
        return getRegistrationResponseDto(requestDto);
    }

    private RegistrationResponseDto getRegistrationResponseDto(RegistrationRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new ConflictException(requestDto.getUsername() + " is already taken!");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ConflictException(requestDto.getEmail() + " has been registered!");
        }
        User user = new User();
        user .setUsername(requestDto.getUsername());
        user .setEmail(requestDto.getEmail());
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        user .setPassword(encodedPassword);
        user .setRoles(requestDto.getRoles());
        user .setCompanyName(requestDto.getCompanyName());

        return new RegistrationResponseDto(userRepository.save(user ));
    }


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
