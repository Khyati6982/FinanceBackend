package com.zorvyn.financebackend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Admin: full access
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCanAccessNetBalance() throws Exception {
        mockMvc.perform(get("/summary/netbalance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    // Analyst: can access insights
    @Test
    @WithMockUser(username = "analyst", roles = {"ANALYST"})
    void testAnalystCanAccessCategoryTotals() throws Exception {
        mockMvc.perform(get("/summary/categorytotals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"ANALYST"})
    void testAnalystCanAccessMonthlyTotals() throws Exception {
        mockMvc.perform(get("/summary/monthly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    @WithMockUser(username = "analyst", roles = {"ANALYST"})
    void testAnalystCanAccessIncomeAndExpenses() throws Exception {
        mockMvc.perform(get("/summary/income"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());

        mockMvc.perform(get("/summary/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    // Viewer: only dashboard summaries
    @Test
    @WithMockUser(username = "viewer", roles = {"VIEWER"})
    void testViewerCannotAccessNetBalance() throws Exception {
        mockMvc.perform(get("/summary/netbalance"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access Denied"));
    }

    @Test
    @WithMockUser(username = "viewer", roles = {"VIEWER"})
    void testViewerCannotAccessCategoryTotals() throws Exception {
        mockMvc.perform(get("/summary/categorytotals"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access Denied"));
    }

    @Test
    @WithMockUser(username = "viewer", roles = {"VIEWER"})
    void testViewerCanAccessRecentActivity() throws Exception {
        mockMvc.perform(get("/summary/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}