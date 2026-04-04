package com.zorvyn.financebackend.service;

import com.zorvyn.financebackend.model.Record;
import com.zorvyn.financebackend.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SummaryService {

    @Autowired
    private RecordRepository recordRepository;

    // Total income
    public Double getTotalIncome() {
        return recordRepository.findAll().stream()
                .filter(r -> "income".equalsIgnoreCase(r.getType()))
                .mapToDouble(Record::getAmount)
                .sum();
    }

    // Total expenses
    public Double getTotalExpenses() {
        return recordRepository.findAll().stream()
                .filter(r -> "expense".equalsIgnoreCase(r.getType()))
                .mapToDouble(Record::getAmount)
                .sum();
    }

    // Net balance
    public Double getNetBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    // Category-wise totals (all categories)
    public Map<String, Double> getCategoryTotals() {
        return recordRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Record::getCategory,
                        Collectors.summingDouble(r -> "income".equalsIgnoreCase(r.getType()) ? r.getAmount() : -r.getAmount())
                ));
    }

    // Category total for a single category
    public Double getCategoryTotal(String category) {
        return recordRepository.findAll().stream()
                .filter(r -> r.getCategory().equalsIgnoreCase(category))
                .mapToDouble(r -> "income".equalsIgnoreCase(r.getType()) ? r.getAmount() : -r.getAmount())
                .sum();
    }

    // Recent activity (last 5 records)
    public List<Record> getRecentActivity() {
        return recordRepository.findAll().stream()
                .sorted(Comparator.comparing(Record::getDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // Monthly totals (income vs expense grouped)
    public Map<String, Map<String, Double>> getMonthlyTotals() {
        return recordRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getDate()).toString(),
                        Collectors.groupingBy(
                                Record::getType,
                                Collectors.summingDouble(Record::getAmount)
                        )
                ));
    }

    // Monthly net total (income - expenses for given year/month)
    public Double getMonthlyTotal(int year, int month) {
        return recordRepository.findAll().stream()
                .filter(r -> r.getDate().getYear() == year && r.getDate().getMonthValue() == month)
                .mapToDouble(r -> "income".equalsIgnoreCase(r.getType()) ? r.getAmount() : -r.getAmount())
                .sum();
    }
}