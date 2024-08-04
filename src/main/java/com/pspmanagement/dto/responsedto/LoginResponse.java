package com.pspmanagement.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
public class LoginResponse {
    private Long id;
    private String username;
    private String email;
    private String companyName;
    private int pspLevel;
    private String token;
    private Set<String> roles;

    public LoginResponse(String token, RegistrationResponseDto responseDto, Set<String> roles) {
        this.token = token;
        this.id = responseDto.getId();
        this.username = responseDto.getUsername();
        this.email = responseDto.getEmail();
        this.companyName = responseDto.getCompanyName();
        this.pspLevel = responseDto.getPspLevel();
        this.roles = roles;
    }

//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
