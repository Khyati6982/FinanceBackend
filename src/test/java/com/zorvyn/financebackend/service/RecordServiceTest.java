package com.zorvyn.financebackend.service;

import com.zorvyn.financebackend.model.Record;
import com.zorvyn.financebackend.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private RecordRepository recordRepository;

    @Test
    void testCreateRecord() {
        Record record = new Record();
        record.setAmount(500.0);
        record.setType("income");
        record.setCategory("salary");
        record.setDate(LocalDate.now());
        record.setNotes("Monthly salary");

        Record saved = recordService.createRecord(record);

        assertNotNull(saved.getId());
        assertEquals("income", saved.getType());
        assertEquals("salary", saved.getCategory());
    }

    @Test
    void testUpdateRecord() {
        Record record = new Record();
        record.setAmount(200.0);
        record.setType("expense");
        record.setCategory("food");
        record.setDate(LocalDate.now());
        record.setNotes("Lunch");

        Record saved = recordService.createRecord(record);

        saved.setAmount(250.0);
        Record updated = recordService.updateRecord(saved.getId(), saved);

        assertEquals(250.0, updated.getAmount());
    }

    @Test
    void testDeleteRecord() {
        Record record = new Record();
        record.setAmount(100.0);
        record.setType("expense");
        record.setCategory("transport");
        record.setDate(LocalDate.now());
        record.setNotes("Bus fare");

        Record saved = recordService.createRecord(record);
        Long id = saved.getId();

        recordService.deleteRecord(id);

        assertFalse(recordRepository.findById(id).isPresent());
    }
}