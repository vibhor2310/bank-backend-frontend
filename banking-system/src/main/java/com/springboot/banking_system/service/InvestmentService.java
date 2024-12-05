package com.springboot.banking_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.banking_system.model.FixedDeposit;
import com.springboot.banking_system.model.Investment;
import com.springboot.banking_system.repository.InvestmentRepository;

@Service
public class InvestmentService {
	
	@Autowired
	private InvestmentRepository investmentRepository;

	public Investment insert(Investment investment) {
		return investmentRepository.save(investment);
	}

	public List<Investment> getInvestmentDetails(int aid) {
		return investmentRepository.getInvestmentDetails(aid);
	}

//	public Investment applyForFixedDeposit(FixedDeposit fixedDeposit) {
//		Investment investment = new Investment();
//		investment.se
//	}

}
