package com.springboot.banking_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.banking_system.model.FixedDeposit;
import com.springboot.banking_system.repository.FixedDepositRepository;

@Service
public class FixedDepositService {
	
	@Autowired
	private FixedDepositRepository fixedDepositRepository;

	public FixedDeposit insert(FixedDeposit fixedDeposit) {
		return fixedDepositRepository.save(fixedDeposit);
		
	}

}
