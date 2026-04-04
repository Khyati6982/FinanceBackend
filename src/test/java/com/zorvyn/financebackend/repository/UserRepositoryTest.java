package com.zorvyn.financebackend.repository;

import com.zorvyn.financebackend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testFindAllUsers() {
        // Seed a user so the repository is not empty
        User user = new User();
        user.setUsername("repoSeedUser");
        user.setPassword(passwordEncoder.encode("seedpass"));
        user.setActive(true);
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty(), "User list should not be empty");
        users.forEach(u -> System.out.println(u.getUsername() + " | active=" + u.isActive()));
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User();
        user.setUsername("repoTestUser");
        user.setPassword(passwordEncoder.encode("password123")); // encode before saving
        user.setActive(true);

        User saved = userRepository.save(user);
        assertNotNull(saved.getId());

        User found = userRepository.findById(saved.getId()).orElseThrow();
        assertEquals("repoTestUser", found.getUsername());
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("uniqueUser");
        user.setPassword(passwordEncoder.encode("password123")); // encode before saving
        user.setActive(true);
        userRepository.save(user);

        User found = userRepository.findByUsername("uniqueUser").orElseThrow();
        assertEquals("uniqueUser", found.getUsername());
    }

    @Test
    void testPasswordIsEncryptedInRepository() {
        User user = new User();
        user.setUsername("encryptedRepoUser");
        user.setPassword(passwordEncoder.encode("plainpass")); // encode before saving
        user.setActive(true);

        User saved = userRepository.save(user);

        // Password should not be stored in plain text
        assertNotEquals("plainpass", saved.getPassword());
        // BCrypt hashes start with $2a$ or $2b$
        assertTrue(saved.getPassword().startsWith("$2"));
    }
}