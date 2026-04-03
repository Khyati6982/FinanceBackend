package com.zorvyn.financebackend.controller;

import com.zorvyn.financebackend.security.JwtUtil;
import com.zorvyn.financebackend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Login endpoint: validates credentials and returns JWT
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            if (authentication.isAuthenticated()) {
                return jwtUtil.generateToken(username);
            } else {
                throw new RuntimeException("Invalid login attempt");
            }
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    // Register endpoint: saves user to DB
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        userDetailsService.registerUser(username, password);
        return "User registered successfully";
    }
}