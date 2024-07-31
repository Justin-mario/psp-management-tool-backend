package com.pspmanagement.model.entity;


import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.model.constant.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.HashSet;
import java.util.List;
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

    @Column(nullable = false, unique = true)
    private String username;

    @Email
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String companyName;

    private int pspLevel;

    @OneToMany(mappedBy = "projectAdmin")
    private List<Project> adminProjects;

    @OneToMany(mappedBy = "projectDeveloper")
    private List<Project> developerProjects;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();


    public User(RegistrationRequestDto dto){
        this.id = dto.getId();
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.companyName = dto.getCompanyName();

        // Initialize roles
        this.roles = new HashSet<>();
//        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
//            this.roles.addAll(dto.getRoles());
//        } else {
//            // Always add ADMIN role for RegistrationRequestDto
//            this.roles.add(UserRole.ADMIN.name());
//        }
//
//
//        // Add any additional roles from the DTO
//        if (dto.getRoles() != null) {
//            this.roles.addAll(dto.getRoles());
//        }
//



//        // Initialize roles if not null, otherwise create a new HashSet
//        this.roles = dto.getRoles() != null
//                ? new HashSet<>(dto.getRoles())
//                : new HashSet<>();
//
//        // Ensure ADMIN role is added for RegistrationRequestDto
//        this.roles.add(UserRole.ADMIN.name());
    }
    public void addRole(UserRole role) {
        this.roles.add(role.name());
    }


    // override equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }



}
