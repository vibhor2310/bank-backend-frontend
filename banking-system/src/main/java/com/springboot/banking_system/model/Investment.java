package com.springboot.banking_system.model;

import java.time.LocalDate;

import com.springboot.banking_system.enums.InvestmentStatus;
import com.springboot.banking_system.enums.InvestmentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Investment {

	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Enumerated(EnumType.STRING)
	private InvestmentStatus status;
	
	@Column(nullable =false)
	private LocalDate purchase_date;
	
	@Enumerated(EnumType.STRING)
	private InvestmentType type;
	
	@ManyToOne
	private Account account;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public InvestmentStatus getStatus() {
		return status;
	}

	public void setStatus(InvestmentStatus status) {
		this.status = status;
	}

	public LocalDate getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(LocalDate purchase_date) {
		this.purchase_date = purchase_date;
	}

	public InvestmentType getType() {
		return type;
	}

	public void setType(InvestmentType type) {
		this.type = type;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	
}