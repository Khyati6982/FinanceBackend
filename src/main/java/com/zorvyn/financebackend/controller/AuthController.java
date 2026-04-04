package com.zorvyn.financebackend.controller;

import com.zorvyn.financebackend.model.User;
import com.zorvyn.financebackend.security.JwtUtil;
import com.zorvyn.financebackend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Login endpoint: validates credentials and returns JWT in JSON
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                throw new BadCredentialsException("Invalid login attempt");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", "Invalid username or password"));
        }
    }

    // Register endpoint: saves user and returns JSON
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User created = userDetailsService.registerUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(created);
    }
}