package com.pspmanagement.repository;

import com.pspmanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByCompanyName(String companyName);

    User findByCompanyName(String companyName);

}
