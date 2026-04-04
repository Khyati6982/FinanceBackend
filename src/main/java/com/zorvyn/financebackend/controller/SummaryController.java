package com.zorvyn.financebackend.controller;

import com.zorvyn.financebackend.model.Record;
import com.zorvyn.financebackend.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    // Admin/Analyst: income
    @GetMapping("/income")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public Double getTotalIncome() {
        return summaryService.getTotalIncome();
    }

    // Admin/Analyst: expenses
    @GetMapping("/expenses")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public Double getTotalExpenses() {
        return summaryService.getTotalExpenses();
    }

    // Admin/Analyst: net balance
    @GetMapping("/netbalance")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public Double getNetBalance() {
        return summaryService.getNetBalance();
    }

    // Admin/Analyst: category totals
    @GetMapping("/categorytotals")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public Map<String, Double> getCategoryTotals() {
        return summaryService.getCategoryTotals();
    }

    // Admin/Analyst/Viewer: recent activity (dashboard)
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public List<Record> getRecentActivity() {
        return summaryService.getRecentActivity();
    }

    // Admin/Analyst: monthly totals (income vs expense trends)
    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public Map<String, Map<String, Double>> getMonthlyTotals() {
        return summaryService.getMonthlyTotals();
    }
}