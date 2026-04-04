package com.zorvyn.financebackend.controller;

import com.zorvyn.financebackend.model.User;
import com.zorvyn.financebackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Admin only: list all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Admin only: get user by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Admin only: create new user
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    // Admin only: update user info
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // Admin only: delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User " + id + " deleted successfully";
    }

    // Admin only: assign role to user
    @PostMapping("/{id}/assignRole")
    @PreAuthorize("hasRole('ADMIN')")
    public User assignRole(@PathVariable Long id, @RequestParam String roleName) {
        return userService.assignRole(id, roleName);
    }

    // Admin only: activate/deactivate user
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUserStatus(@PathVariable Long id, @RequestParam boolean active) {
        return userService.updateUserStatus(id, active);
    }
}