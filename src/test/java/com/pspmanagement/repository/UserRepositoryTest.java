package com.pspmanagement.repository;

import com.pspmanagement.dto.requestdto.AdminRegistrationRequestDto;
import com.pspmanagement.model.constant.UserRole;
import com.pspmanagement.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerAdminTest() {
        AdminRegistrationRequestDto requestDto = new AdminRegistrationRequestDto();
        requestDto.setId(1L);
        requestDto.setUsername("John");
        requestDto.setPassword("password");
        requestDto.setEmail("john@example.com");
        requestDto.setCompanyName("UH");
        requestDto.setRole(UserRole.ADMIN);

        User user = new User(requestDto);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("John", savedUser.getUsername());
    }

    @Test
    void testFindByUsername() {
        AdminRegistrationRequestDto requestDto = new AdminRegistrationRequestDto();
        requestDto.setId(1L);
        requestDto.setUsername("JohnDoe");
        requestDto.setPassword("password");
        requestDto.setEmail("john@example.com");
        requestDto.setCompanyName("UH");
        requestDto.setRole(UserRole.ADMIN);

        User user = new User(requestDto);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("JohnDoe");

        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }
}