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

    // Total income
    @GetMapping("/income")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public Double getTotalIncome() {
        return summaryService.getTotalIncome();
    }

    // Total expenses
    @GetMapping("/expenses")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public Double getTotalExpenses() {
        return summaryService.getTotalExpenses();
    }

    // Net balance
    @GetMapping("/netbalance")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public Double getNetBalance() {
        return summaryService.getNetBalance();
    }

    // Category-wise totals
    @GetMapping("/categorytotals")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public Map<String, Double> getCategoryTotals() {
        return summaryService.getCategoryTotals();
    }

    // Recent activity (last 5 records)
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public List<Record> getRecentActivity() {
        return summaryService.getRecentActivity();
    }

    // Monthly totals (income vs expense)
    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public Map<String, Map<String, Double>> getMonthlyTotals() {
        return summaryService.getMonthlyTotals();
    }
}