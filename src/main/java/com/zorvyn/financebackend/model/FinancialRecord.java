package com.zorvyn.financebackend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity

@Table(name = "financial_records")
public class FinancialRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double amount;
	
	@Column(nullable = false)
	private String type; //income or expense
	
	private String category;
	
	private LocalDate date;
	
	private String notes;
	
	public FinancialRecord() {}

	public FinancialRecord(Double amount, String type, String category, LocalDate date, String notes) {
		this.amount = amount;
		this.type = type;
		this.category = category;
		this.date = date;
		this.notes = notes;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}