package com.springboot.banking_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.banking_system.model.Stocks;
import com.springboot.banking_system.repository.StocksRepository;

@Service
public class StocksService {
	
	@Autowired
	private StocksRepository stocksRepository;

	public Stocks insert(Stocks stocks) {
		return stocksRepository.save(stocks);
	}

}
