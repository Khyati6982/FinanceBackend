package com.zorvyn.financebackend.controller;

import com.zorvyn.financebackend.model.Record;
import com.zorvyn.financebackend.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    // Analyst/Admin: list all records (with optional filters)
    @GetMapping
    @PreAuthorize("hasAnyRole('ANALYST','ADMIN')")
    public List<Record> getAllRecords(@RequestParam(required = false) String type,
                                      @RequestParam(required = false) String category,
                                      @RequestParam(required = false) String date) {
        return recordService.getAllRecords(type, category, date);
    }

    // Analyst/Admin: get record by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST','ADMIN')")
    public Record getRecordById(@PathVariable Long id) {
        return recordService.getRecordById(id);
    }

    // Admin only: create new record
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Record createRecord(@Valid @RequestBody Record record) {
        return recordService.createRecord(record);
    }

    // Admin only: update record
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Record updateRecord(@PathVariable Long id, @Valid @RequestBody Record updatedRecord) {
        return recordService.updateRecord(id, updatedRecord);
    }

    // Admin only: delete record
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return "Record " + id + " deleted successfully";
    }
}