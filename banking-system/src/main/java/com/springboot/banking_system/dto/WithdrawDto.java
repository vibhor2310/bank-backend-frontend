package com.springboot.banking_system.dto;

import org.springframework.stereotype.Component;

@Component
public class WithdrawDto {
	
	private String accountNumber;
	private double amount;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	

}
