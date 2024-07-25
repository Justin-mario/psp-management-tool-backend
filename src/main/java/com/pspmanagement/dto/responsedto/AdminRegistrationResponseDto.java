package com.pspmanagement.dto.responsedto;

import com.pspmanagement.model.constant.UserRole;
import com.pspmanagement.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AdminRegistrationResponseDto {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private String companyName;

    public AdminRegistrationResponseDto(User admin) {
        id = admin.getId();
        username = admin.getUsername();
        email = admin.getEmail();
        role = admin.getRole();
        companyName = admin.getCompanyName();
    }

    @Override
    public String toString() {
        return "AdminRegistrationResponseDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}


