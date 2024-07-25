package com.pspmanagement.dto.requestdto;

import com.pspmanagement.model.constant.UserRole;
import com.pspmanagement.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegistrationRequestDto {
    private Long id;
    @NotBlank(message = "User name is required")
    private String username;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private UserRole role;
    @NotBlank(message = "Company name is required")
    private String companyName;



    @Override
    public String toString() {
        return "AdminRegistrationRequestDto{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
