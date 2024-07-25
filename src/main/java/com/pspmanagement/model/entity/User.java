package com.pspmanagement.model.entity;

import com.pspmanagement.model.constant.UserRole;
import com.pspmanagement.dto.requestdto.AdminRegistrationRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "psp_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Email
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String companyName;

    public User(AdminRegistrationRequestDto adminRegistrationRequestDto){
        id = adminRegistrationRequestDto.getId();
        username = adminRegistrationRequestDto.getUsername();
        email = adminRegistrationRequestDto.getEmail();
        password = adminRegistrationRequestDto.getPassword();
        role = UserRole.ADMIN;
        companyName = adminRegistrationRequestDto.getCompanyName();
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", companyName='" + companyName + '\'' +
                '}';
    }



}
