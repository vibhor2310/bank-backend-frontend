package com.springboot.banking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.banking_system.model.MutualFunds;

@Repository
public interface MutualFundsRepository extends JpaRepository<MutualFunds, Integer>{

}
