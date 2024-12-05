package com.springboot.banking_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.banking_system.model.Bonds;
import com.springboot.banking_system.repository.BondsRepository;

@Service
public class BondsService {
	
	@Autowired
	private BondsRepository bondsRepository;

	public Bonds insert(Bonds bonds) {
		return bondsRepository.save(bonds);
	}

}
