package com.zorvyn.financebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorvyn.financebackend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCanCreateUser() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password123");
        user.setActive(true);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"ANALYST"})
    void testAnalystCannotCreateUser() throws Exception {
        User user = new User();
        user.setUsername("blockeduser");
        user.setPassword("password123");
        user.setActive(true);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access Denied"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCanUpdateUser() throws Exception {
        // First create a user
        User user = new User();
        user.setUsername("updatableuser");
        user.setPassword("password123");
        user.setActive(true);

        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        User created = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        // Now update that user
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("newpass");
        updatedUser.setActive(false);

        mockMvc.perform(put("/users/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    void testViewerCannotDeleteUser() throws Exception {
        // First create a user as Admin
        User user = new User();
        user.setUsername("deletableuser");
        user.setPassword("password123");
        user.setActive(true);

        MvcResult result = mockMvc.perform(post("/users")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        User created = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        // Now try to delete as Viewer
        mockMvc.perform(delete("/users/" + created.getId())
                .with(user("viewer").roles("VIEWER")))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access Denied"));
    }
}