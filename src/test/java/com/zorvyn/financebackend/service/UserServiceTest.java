package com.zorvyn.financebackend.service;

import com.zorvyn.financebackend.exception.DuplicateUsernameException;
import com.zorvyn.financebackend.exception.UserNotFoundException;
import com.zorvyn.financebackend.model.User;
import com.zorvyn.financebackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterNewUser() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password123");
        user.setActive(true);

        User saved = userService.registerUser(user);

        assertNotNull(saved.getId());
        assertEquals("newuser", saved.getUsername());
        assertTrue(saved.isActive());
    }

    @Test
    void testDuplicateUsernameThrowsException() {
        User user1 = new User();
        user1.setUsername("duplicateuser");
        user1.setPassword("password123");
        user1.setActive(true);
        userService.registerUser(user1);

        User user2 = new User();
        user2.setUsername("duplicateuser");
        user2.setPassword("password456");
        user2.setActive(true);

        assertThrows(DuplicateUsernameException.class, () -> userService.registerUser(user2));
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("loaduser");
        user.setPassword("password123");
        user.setActive(true);
        userService.registerUser(user);

        UserDetails details = userService.loadUserByUsername("loaduser");

        assertEquals("loaduser", details.getUsername());
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername("ghostuser"));
    }

    @Test
    void testPasswordIsEncryptedOnRegister() {
        User user = new User();
        user.setUsername("secureuser");
        user.setPassword("plainpass");
        user.setActive(true);

        User saved = userService.registerUser(user);

        // Password should not be stored in plain text
        assertNotEquals("plainpass", saved.getPassword());
        // BCrypt hashes start with $2a$ or $2b$
        assertTrue(saved.getPassword().startsWith("$2"));
    }
}