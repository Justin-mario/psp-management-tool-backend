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
    private Set<String> roles;
    private String companyName;

    public RegistrationResponseDto(User admin) {
        id = admin.getId();
        username = admin.getUsername();
        email = admin.getEmail();
        roles = admin.getRoles();
        companyName = admin.getCompanyName();
    }

    @Override
    public String toString() {
        return "RegistrationResponseDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}


