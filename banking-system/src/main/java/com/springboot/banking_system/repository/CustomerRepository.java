package com.springboot.banking_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.banking_system.model.Account;
import com.springboot.banking_system.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	@Query("select c from Customer c where c.id = ?1")
	List<Customer> getCustomerDetail(int id);

	
	@Query("select c from Customer c "
			+ "join c.user u"
			+ " where u.username=?1")
	Customer getCustomerDetailByUsername(String username);


	@Query("select a from Account a"
			+ " join a.customer c"
			+ " join c.user u "
			+ "where u.username=?1")
	List<Account> getAllAccountsByUsername(String username);


	@Query("select c from Customer c"
			+ " join c.user u"
			+ " where u.username=?1")
	Customer findCustomerByUsername(String username);


	@Query("select c from Account a join a.customer c where a.accountNumber=?1")
	Customer getCustomerDetailsByAccount(String accountNumber);




}
