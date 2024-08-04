package com.pspmanagement.dto.responsedto;

import com.pspmanagement.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class RegistrationResponseDto {
    private Long id;
    private String username;
    private String email;
    private String companyName;
    private int pspLevel;
    private Set<String> roles;
    private String token;

    public RegistrationResponseDto(User user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        roles = user.getRoles();
        companyName = user.getCompanyName();
        pspLevel = user.getPspLevel();
    }

    public RegistrationResponseDto(String jwt, User user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        roles = user.getRoles();
        companyName = user.getCompanyName();
        pspLevel = user.getPspLevel();
        token = jwt;
    }

    @Override
    public String toString() {
        return "RegistrationResponseDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", companyName='" + companyName + '\'' +
                ", pspLevel=" + pspLevel +
                ", token='" + token + '\'' +
                '}';
    }
}


