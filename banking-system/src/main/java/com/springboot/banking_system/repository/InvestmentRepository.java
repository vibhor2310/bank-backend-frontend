package com.springboot.banking_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.banking_system.model.Investment;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer>{

	@Query("select i from Investment i join i.account a where a.id=?1")
	List<Investment> getInvestmentDetails(int aid);

}
