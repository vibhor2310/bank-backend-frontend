package com.springboot.banking_system.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.banking_system.JwtUtil;
import com.springboot.banking_system.dto.JwtDto;
import com.springboot.banking_system.dto.ResponseMessageDto;
import com.springboot.banking_system.exception.InvalidUsernameException;
import com.springboot.banking_system.exception.ResourceNotFoundException;
import com.springboot.banking_system.model.Customer;
import com.springboot.banking_system.model.User;
import com.springboot.banking_system.service.CustomerService;
import com.springboot.banking_system.service.UserSecurityService;
import com.springboot.banking_system.service.UserService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/api/token")
	public ResponseEntity<?> getToken(@RequestBody User user, JwtDto dto ) {
		try {
		Authentication auth 
				= new UsernamePasswordAuthenticationToken
							(user.getUsername(), user.getPassword());

		authenticationManager.authenticate(auth);
		
		/*Check if username is in DB */
		user = (User) userSecurityService.loadUserByUsername(user.getUsername());
		
		String jwt = jwtUtil.generateToken(user.getUsername());
		dto.setUsername(user.getUsername());
		dto.setToken(jwt);
		return ResponseEntity.ok(dto);
		}
		catch(AuthenticationException ae) {
			return ResponseEntity.badRequest().body(ae.getMessage());
		}
	}
	
	@PostMapping("/auth/sign-up")
	public ResponseEntity<?> signUp(@RequestBody User user,ResponseMessageDto dto){
		try {
			return ResponseEntity.ok(userService.signUp(user));
		} 
		catch (InvalidUsernameException e) {
			dto.setMsg(e.getMessage());
			 return ResponseEntity.badRequest().body(dto);
		}
	}
	
	@GetMapping("/auth/login") 
	public ResponseEntity<?> login(Principal principal,ResponseMessageDto dto) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		if(!user.isEnabled()) {
			dto.setMsg("User disabled, Please contact Admin");
			return ResponseEntity.badRequest().body(dto);
		}
		return ResponseEntity.ok(user); 
	}
	
	@PostMapping("/auth/switch-status/{id}")
	public ResponseEntity<?> updateUserStatus(@PathVariable int id,
							     @RequestParam boolean status,
							     ResponseMessageDto dto) {
		try {
			User user = userService.updateUserStatus(id,status);
			return ResponseEntity.ok(user);  
		} catch (ResourceNotFoundException e) {
			dto.setMsg(e.getMessage());
			return ResponseEntity.badRequest().body(dto);
		}
	}
	
	@GetMapping("/auth/user")
	public User getUserDetails(Principal principal) {
		String loggedInUsername = principal.getName();
		User user  = (User)userSecurityService.loadUserByUsername(loggedInUsername);
		return user; 
	}
	
	
	@GetMapping("/exist")
	 public ResponseEntity<?> checkCustomerDetailsExist(Principal principal) {
		String username = principal.getName();
		 User user = userService.findByUsername(username);
		 boolean exist = customerService.customerDetailsExist(user.getId());
	 return ResponseEntity.ok(exist);
	}
	

}
