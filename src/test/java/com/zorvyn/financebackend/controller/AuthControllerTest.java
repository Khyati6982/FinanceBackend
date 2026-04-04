package com.zorvyn.financebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorvyn.financebackend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("authuser");
        user.setPassword("password123");
        user.setActive(true);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("authuser"));
    }

    @Test
    void testLoginUser() throws Exception {
        // First register the user
        User user = new User();
        user.setUsername("loginuser");
        user.setPassword("password123");
        user.setActive(true);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        // Then login
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"loginuser\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()); // JWT should be returned
    }

    @Test
    void testLoginFailsWithWrongPassword() throws Exception {
        // Register user
        User user = new User();
        user.setUsername("wrongpassuser");
        user.setPassword("password123");
        user.setActive(true);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        // Try login with wrong password
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"wrongpassuser\",\"password\":\"badpass\"}"))
                .andExpect(status().isUnauthorized());
    }
}