package com.zorvyn.financebackend.repository;

import com.zorvyn.financebackend.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    // Viewer: fetch records created by a specific user
    List<Record> findByCreatedBy(String createdBy);

    // Analyst/Admin: filter records by type, category, and date
    @Query("SELECT r FROM Record r WHERE " +
            "(:type IS NULL OR r.type = :type) AND " +
            "(:category IS NULL OR r.category = :category) AND " +
            "(:date IS NULL OR r.date = :date)")
    List<Record> filterRecords(String type, String category, LocalDate date);
}