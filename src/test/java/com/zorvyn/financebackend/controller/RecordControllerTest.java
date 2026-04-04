package com.zorvyn.financebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zorvyn.financebackend.model.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Record sampleRecord() {
        Record r = new Record();
        r.setAmount(250.0);
        r.setType("expense");
        r.setCategory("food");
        r.setDate(LocalDate.of(2026, 4, 4));
        r.setNotes("Dinner");
        return r;
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCanCreateRecord() throws Exception {
        mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRecord())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("food"));
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"ANALYST"})
    void testAnalystCannotCreateRecord() throws Exception {
        mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRecord())))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access Denied"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCanUpdateRecord() throws Exception {
        // First create a record
        MvcResult result = mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRecord())))
                .andExpect(status().isOk())
                .andReturn();

        Record created = objectMapper.readValue(result.getResponse().getContentAsString(), Record.class);

        // Now update that record
        Record updated = sampleRecord();
        updated.setCategory("transport");

        mockMvc.perform(put("/records/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("transport"));
    }

    @Test
    void testViewerCannotDeleteRecord() throws Exception {
        // First create a record as Admin
        MvcResult result = mockMvc.perform(post("/records")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRecord())))
                .andExpect(status().isOk())
                .andReturn();

        Record created = objectMapper.readValue(result.getResponse().getContentAsString(), Record.class);

        // Now try to delete as Viewer
        mockMvc.perform(delete("/records/" + created.getId())
                .with(user("viewer").roles("VIEWER")))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access Denied"));
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"ANALYST"})
    void testAnalystCanViewRecords() throws Exception {
        mockMvc.perform(get("/records"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "viewer", roles = {"VIEWER"})
    void testViewerCannotViewRecords() throws Exception {
        mockMvc.perform(get("/records"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access Denied"));
    }
}