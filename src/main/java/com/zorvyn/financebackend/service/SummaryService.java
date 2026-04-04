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

    // Category-wise totals
    public Map<String, Double> getCategoryTotals() {
        return recordRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Record::getCategory,
                        Collectors.summingDouble(Record::getAmount)
                ));
    }

    // Recent activity (last 5 records)
    public List<Record> getRecentActivity() {
        return recordRepository.findAll().stream()
                .sorted(Comparator.comparing(Record::getDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // Monthly totals (income vs expense)
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
}