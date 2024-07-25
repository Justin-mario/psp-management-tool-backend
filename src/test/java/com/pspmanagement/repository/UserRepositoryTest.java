package com.pspmanagement.repository;

import com.pspmanagement.dto.requestdto.RegistrationRequestDto;
import com.pspmanagement.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerAdminTest() {
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setId(1L);
        requestDto.setUsername("John");
        requestDto.setPassword("password");
        requestDto.setEmail("john@example.com");
        requestDto.setCompanyName("UH");
        requestDto.setRoles(Collections.singleton("ADMIN"));

        User user = new User(requestDto);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("John", savedUser.getUsername());
    }

    @Test
    void testFindByUsername() {
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setId(1L);
        requestDto.setUsername("JohnDoe");
        requestDto.setPassword("password");
        requestDto.setEmail("john@example.com");
        requestDto.setCompanyName("UH");
        requestDto.setRoles(Collections.singleton("ADMIN"));

        User user = new User(requestDto);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("JohnDoe");

        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }
}