package com.springboot.banking_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.banking_system.dto.CustomerDto;
import com.springboot.banking_system.enums.Role;
import com.springboot.banking_system.exception.ResourceNotFoundException;
import com.springboot.banking_system.model.Account;
import com.springboot.banking_system.model.Customer;
import com.springboot.banking_system.model.User;
import com.springboot.banking_system.repository.CustomerRepository;
import com.springboot.banking_system.repository.UserRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	public Customer insert(Customer customer){
		
		User user = customer.getUser();
		user.setRole(Role.CUSTOMER);
		String encPassword = passwordEncoder.encode(user.getPassword());
		 
		user.setPassword(encPassword);
		user = userRepository.save(user); //complete user with role, password and id
		
		customer.setUser(user);
		
		return customerRepository.save(customer);
			
		}
		

	public Customer validate(int cid) throws ResourceNotFoundException {
		
		Optional<Customer>optional = customerRepository.findById(cid);
		
		if(optional.isEmpty()) {
			throw new ResourceNotFoundException("Given id is invalid try again...");
		}
		
		Customer customer = optional.get();
		
		return customer;
		
		
	}

	public void delete(int id) {
		customerRepository.deleteById(id);
		
	}

	public List<Customer> getCustomerDetail(int id) {
		return customerRepository.getCustomerDetail(id);
	}


	public boolean customerDetailsExist(int id) {
		
		Optional<Customer>optional = customerRepository.findById(id);
		if(optional.isEmpty())
			return true;
		return false;
	}

	
	// api related to front - end

	public Customer getCustomerDetailByUsername(String username) {
		return customerRepository.getCustomerDetailByUsername(username);
	}


	public List<Account> getAllAccountsByUsername(String username) {
		return customerRepository.getAllAccountsByUsername(username);
	}


	public Customer updateCustomer(Customer newCustomer) {
		
		return customerRepository.save(newCustomer);
	}


	public Customer findCustomerByUsername(String username) {
		
		return customerRepository.findCustomerByUsername(username);
	}


	public Customer updateCustomerDetails(Customer customer, CustomerDto newCustomer) {
		
		if(newCustomer.getFirstName()!=null)
			customer.setFirstName(newCustomer.getFirstName());
		if(newCustomer.getLastName()!=null)
			customer.setLastName(newCustomer.getLastName());
		if(newCustomer.getDateOfBirth()!=null)
			customer.setDateOfBirth(newCustomer.getDateOfBirth());
		if(newCustomer.getContactNumber()!=null)
			customer.setContactNumber(newCustomer.getContactNumber());
		if(newCustomer.getAddress()!=null)
			customer.setAddress(newCustomer.getAddress());
		
		return customerRepository.save(customer);
		
	}


	public Customer getCustomerDetailsByAccount(String accountNumber) {
	return customerRepository.getCustomerDetailsByAccount(accountNumber);
	}


}
