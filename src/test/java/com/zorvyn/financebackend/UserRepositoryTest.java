package com.zorvyn.financebackend;

import com.zorvyn.financebackend.model.User;
import com.zorvyn.financebackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty(), "User list should not be empty");
        users.forEach(u -> System.out.println(u.getUsername() + " | active=" + u.isActive()));
    }
}