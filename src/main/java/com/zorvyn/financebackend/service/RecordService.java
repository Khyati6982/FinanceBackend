package com.zorvyn.financebackend.service;

import com.zorvyn.financebackend.model.Record;
import com.zorvyn.financebackend.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    // Get all records (with optional filters)
    public List<Record> getAllRecords(String type, String category, String date) {
        if (type != null || category != null || date != null) {
            LocalDate parsedDate = (date != null) ? LocalDate.parse(date) : null;
            return recordRepository.filterRecords(type, category, parsedDate);
        }
        return recordRepository.findAll();
    }

    // Get record by ID
    public Record getRecordById(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    // Create new record
    public Record createRecord(Record record) {
        return recordRepository.save(record);
    }

    // Update record
    public Record updateRecord(Long id, Record updatedRecord) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        record.setAmount(updatedRecord.getAmount());
        record.setType(updatedRecord.getType());
        record.setCategory(updatedRecord.getCategory());
        record.setDate(updatedRecord.getDate());
        record.setNotes(updatedRecord.getNotes());

        return recordRepository.save(record);
    }

    // Delete record
    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }
}