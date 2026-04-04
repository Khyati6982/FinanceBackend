package com.zorvyn.financebackend.service;

import com.zorvyn.financebackend.model.Record;
import com.zorvyn.financebackend.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SummaryServiceTest {

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private RecordRepository recordRepository;

    @BeforeEach
    void setup() {
        // Clear existing records before each test
        recordRepository.deleteAll();

        // Seed with sample records
        recordRepository.save(new Record(1000.0, "income", "salary", LocalDate.of(2026, 4, 1), "Monthly salary"));
        recordRepository.save(new Record(200.0, "expense", "food", LocalDate.of(2026, 4, 2), "Lunch"));
        recordRepository.save(new Record(150.0, "expense", "transport", LocalDate.of(2026, 4, 3), "Bus fare"));
        recordRepository.save(new Record(300.0, "expense", "software", LocalDate.of(2026, 3, 28), "Subscription"));
    }

    @Test
    void testNetBalance() {
        double netBalance = summaryService.getNetBalance();
        // 1000 income - (200+150+300) expenses = 350
        assertEquals(350.0, netBalance);
    }

    @Test
    void testMonthlyTotals() {
        double aprilTotal = summaryService.getMonthlyTotal(2026, 4);
        // April: 1000 income - (200+150) expenses = 650
        assertEquals(650.0, aprilTotal);

        double marchTotal = summaryService.getMonthlyTotal(2026, 3);
        // March: -300 expense
        assertEquals(-300.0, marchTotal);
    }

    @Test
    void testCategoryTotals() {
        double foodTotal = summaryService.getCategoryTotal("food");
        assertEquals(-200.0, foodTotal);

        double salaryTotal = summaryService.getCategoryTotal("salary");
        assertEquals(1000.0, salaryTotal);
    }

    @Test
    void testEmptyCategoryReturnsZero() {
        double travelTotal = summaryService.getCategoryTotal("travel");
        assertEquals(0.0, travelTotal);
    }
}