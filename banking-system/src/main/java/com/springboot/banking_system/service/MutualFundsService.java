package com.springboot.banking_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.banking_system.model.MutualFunds;
import com.springboot.banking_system.repository.MutualFundsRepository;

@Service
public class MutualFundsService {
	
	@Autowired
	private MutualFundsRepository mutualFundsRepository;

	public MutualFunds insert(MutualFunds mutualFunds) {
		return mutualFundsRepository.save(mutualFunds);
	}

}
