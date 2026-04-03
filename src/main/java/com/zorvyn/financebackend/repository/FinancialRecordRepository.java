package com.zorvyn.financebackend.repository;

import com.zorvyn.financebackend.model.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
	List<FinancialRecord> findByCategory(String category);
	List<FinancialRecord> findByType(String type);
	List<FinancialRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);
}