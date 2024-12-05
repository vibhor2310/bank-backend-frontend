package com.springboot.banking_system.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.banking_system.dto.CustomerDto;
import com.springboot.banking_system.dto.DepositDto;
import com.springboot.banking_system.dto.ResponseMessageDto;
import com.springboot.banking_system.dto.TransactionDto;
import com.springboot.banking_system.dto.WithdrawDto;
import com.springboot.banking_system.enums.InvestmentStatus;
import com.springboot.banking_system.enums.InvestmentType;
import com.springboot.banking_system.exception.ResourceNotFoundException;
import com.springboot.banking_system.model.Account;
import com.springboot.banking_system.model.Bonds;
import com.springboot.banking_system.model.Card;
import com.springboot.banking_system.model.Customer;
import com.springboot.banking_system.model.FixedDeposit;
import com.springboot.banking_system.model.Investment;
import com.springboot.banking_system.model.Loan;
import com.springboot.banking_system.model.MutualFunds;
import com.springboot.banking_system.model.Stocks;
import com.springboot.banking_system.model.Transaction;
import com.springboot.banking_system.service.AccountService;
import com.springboot.banking_system.service.BondsService;
import com.springboot.banking_system.service.CardService;
import com.springboot.banking_system.service.CustomerService;
import com.springboot.banking_system.service.FixedDepositService;
import com.springboot.banking_system.service.InvestmentService;
import com.springboot.banking_system.service.LoanService;
import com.springboot.banking_system.service.MutualFundsService;
import com.springboot.banking_system.service.StocksService;
import com.springboot.banking_system.service.TransactionService;
//import com.springboot.banking_system.service.TransactionService;
import com.springboot.banking_system.service.UserService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private LoanService loanService;
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private InvestmentService investmentService;
	
	@Autowired
	private FixedDepositService fixedDepositService;
	
	@Autowired
	private MutualFundsService mutualFundsService;
	
	@Autowired
	private StocksService stocksService;
	
	@Autowired
	private BondsService bondsService;
	
	
	@PostMapping("/customer/register")
	public Customer registerCustomer(@RequestBody Customer customer,ResponseMessageDto dto) {
		// its used to insert the customer details i.e. to register customer
		  return customerService.insert(customer);
		 
	}
	
	@PutMapping("/customer/update/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable int id,@RequestBody Customer newCustomer ,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		// validate id
		
			Customer existingCustomerDb = customerService.validate(id);
			if(newCustomer.getContactNumber()!=null)
				existingCustomerDb.setContactNumber(newCustomer.getContactNumber());
			if(newCustomer.getAddress()!=null)
				existingCustomerDb.setAddress(newCustomer.getAddress());
			if(newCustomer.getAadharNumber()!=null)
				existingCustomerDb.setAadharNumber(newCustomer.getAadharNumber());
			if(newCustomer.getEmail()!=null)
				existingCustomerDb.setEmail(newCustomer.getEmail());
			if(newCustomer.getFirstName()!=null)
				existingCustomerDb.setFirstName(newCustomer.getFirstName());
			if(newCustomer.getLastName()!=null) 
				existingCustomerDb.setLastName(newCustomer.getLastName());
			if(newCustomer.getPanNumber()!=null)
				existingCustomerDb.setPanNumber(newCustomer.getPanNumber());
			

			//re save this existing customer having new updated value 
			existingCustomerDb = customerService.insert(existingCustomerDb);
			return ResponseEntity.ok(existingCustomerDb);
		
			
	}
	
	@DeleteMapping("/customer/delete/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable int id,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		
			customerService.validate(id);// to validate the id  given
			customerService.delete(id); // to delete the id given
		
		dto.setMsg("Customer deleted");
		return ResponseEntity.ok(dto);
		
	}
	
	@GetMapping("/customer/detail/{id}")
	public ResponseEntity<?> getCustomerDetail(@PathVariable int id,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		
			customerService.validate(id); // to validate id
		
		
		List<Customer> list =  customerService.getCustomerDetail(id);
		return ResponseEntity.ok(list);
		
		
	}
	
	

	
	
	
	
	// account - Ops;
	
	// front - end API
	@PostMapping("/customer/account/add/{cid}")
	public ResponseEntity<?> addAccount(@PathVariable int cid,@RequestBody Account accountDet,Account account ,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		// validate customer id first;
		Customer customer = null;
	
	
		customer = customerService.validate(cid);
	
	account.setCustomer(customer);
	account.setAccountType(accountDet.getAccountType());
	account.setBalance(accountDet.getBalance());
	account.setDateCreated(LocalDate.now());
	
	account.setAadharNumber(accountDet.getAadharNumber());
	account.setPanNumber(accountDet.getPanNumber());
	
//	System.out.println(account.getAadharNumber());
	account = accountService.insert(account);
//	System.out.println(account.getAadharNumber());
	return ResponseEntity.ok(account);	
	}
	
	
	@GetMapping("/customer/account/details/{cid}")
	public ResponseEntity<?> getAccountDetails(@PathVariable int cid,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		
			customerService.validate(cid);
		
		
		List<Account> list = accountService.getAccountDetails(cid);
		return ResponseEntity.ok(list);
		
	}
	
	@PutMapping("/customer/account/update/{aid}")
	public ResponseEntity<?> updateCustomerAccount(@PathVariable int aid,@RequestBody Account newAccount ,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		// validate id
		
			Account existingAccountDb = accountService.validate(aid);
			if(newAccount.getAadharNumber()!=null)
				existingAccountDb.setAadharNumber(newAccount.getAadharNumber());
			if(newAccount.getPanNumber()!=null)
				existingAccountDb.setPanNumber(newAccount.getPanNumber());
			

			//re save this existing customer having new updated value 
			existingAccountDb = accountService.insertUpdatedData(existingAccountDb);
			return ResponseEntity.ok(existingAccountDb);
		
		
			
	}
	
	
	
	// transactions ops;
	
	// to deposit money
	@PostMapping("/customer/account/deposit/{aid}/{amount}")
	public ResponseEntity<?> depositMoney(@PathVariable int aid,@PathVariable double amount,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		// validate account id;
		Account account= null;
		    
				account = accountService.validateIdAndAmount(aid,amount);
			
		    
		     
		     Transaction transaction = transactionService.depositMoney(aid,amount);
		     return ResponseEntity.ok(transaction);
		
		
	}
	
	// to withdraw money
	@PostMapping("/customer/account/withdraw/{aid}/{amount}")
	public ResponseEntity<?> withdrawMoney(@PathVariable int aid,@PathVariable double amount,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		// validate account id;
		Account account= null;
		    
				account = accountService.validateIdAndAmount(aid,amount);
			
		    
		     
		     Transaction transaction = transactionService.withdrawMoney(aid,amount);
		     return ResponseEntity.ok(transaction);
		
		
	}
	
	// to transfer money;
	@PostMapping("/customer/account/transfer/{aid}/{reaccno}/{amount}")
	public ResponseEntity<?> transferMoney(@PathVariable int aid,@PathVariable String reaccno,@PathVariable double amount,ResponseMessageDto dto) throws ResourceNotFoundException {
		// validate account id;
		Account account= null;
			
				 account = accountService.validateIdAndAmountAndBalance(aid,amount,reaccno);
			
//			 System.out.println(reaccno);
			 Transaction transaction = transactionService.transferMoney(aid,reaccno,amount);
			
			 return ResponseEntity.ok(transaction);
	}
	
	
	@GetMapping("/customer/account/transaction/{aid}")
	public ResponseEntity<?> getTransactionHistory(@PathVariable int aid,ResponseMessageDto dto) throws ResourceNotFoundException {

		
			accountService.validate(aid);
		
		
		List<Transaction> list = accountService.getTransactionHistory(aid);
		return ResponseEntity.ok(list);
		
		
	}
	
	
	// loan ops
	
	@PostMapping("/customer/account/loan/add/{aid}")
	public ResponseEntity<?> addLoan(@PathVariable int aid,@RequestBody Loan loanDet ,Loan loan,ResponseMessageDto dto) throws ResourceNotFoundException {
		// validate account id first;
		Account account = null;
	
	
		account = accountService.validate(aid);
	
	loan.setLoanType(loanDet.getLoanType());
	loan.setPurpose(loanDet.getPurpose());
	loan.setAmount(loanDet.getAmount());
	loan.setStatus("Not Aprroved");
	loan.setAccount(account);
	
	loan = loanService.insert(loan);
	return ResponseEntity.ok(loan);
	
	
	
		
	}
	
	@GetMapping("/customer/account/loan/detail/{aid}")
	public ResponseEntity<?> getLoanDetails(@PathVariable int aid,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		
			accountService.validate(aid);
		
		List<Loan> list = loanService.getLoanDetails(aid);
		return ResponseEntity.ok(list);
		
	}
	
	
	//  card ops
	
	@PostMapping("/customer/account/card/add/{aid}")
	public ResponseEntity<?> addcard(@PathVariable int aid,@RequestBody Card cardDet ,Card card,ResponseMessageDto dto) throws ResourceNotFoundException {
		// validate account id first;
		Account account = null;
	
	
		account = accountService.validate(aid);
	
	
	
	card.setBalance(account.getBalance());
	card.setExpiryDate(LocalDate.now().plusYears(5));
	card.setCardType(cardDet.getCardType());
	card.setStatus("Approved");
	card.setAccount(account);

	card = cardService.insert(card);
	return ResponseEntity.ok(account);	
	
	
	
		
	}
	
	
	@GetMapping("/customer/account/card/detail/{aid}")
	public ResponseEntity<?> getCardDetails(@PathVariable int aid,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		
			accountService.validate(aid);
		
		
		List<Card> list = cardService.getCardDetails(aid);
		return ResponseEntity.ok(list);
		
	}
	
	// investment - post ops
	
	
	@PostMapping("/customer/account/investment/fd/{aid}")
	public ResponseEntity<?> applyForFixedDeposit(@PathVariable int aid,@RequestBody FixedDeposit fixedDepositDet,Investment investment,FixedDeposit fixedDeposit,ResponseMessageDto dto) throws ResourceNotFoundException{
		
		
			Account account = accountService.validate(aid);
			
			
			investment.setAccount(account);
			investment.setType(InvestmentType.FIXEDDEPOSITS);
			investment.setStatus(InvestmentStatus.PENDING);
			investment.setPurchase_date(LocalDate.now());
			
			Investment savedInvestment = investmentService.insert(investment);
			
			
			
			fixedDeposit.setDepositAmount(fixedDepositDet.getDepositAmount());
			
			fixedDeposit.setYears(fixedDepositDet.getYears());
			
			fixedDeposit.setMaturityDate(LocalDate.now().plusYears((long) fixedDepositDet.getYears()));
			
			fixedDeposit.setInvestment(savedInvestment);
			
			fixedDeposit = fixedDepositService.insert(fixedDeposit);
			
			
			return ResponseEntity.ok(fixedDeposit);	
		
	}
	
	@PostMapping("/customer/account/investment/mutual-fund/{aid}")
	public ResponseEntity<?> applyForMutualFunds(@PathVariable int aid,@RequestBody MutualFunds mutualFundsDet,Investment investment,MutualFunds mutualFunds,ResponseMessageDto dto) throws ResourceNotFoundException{
		
		
			Account account = accountService.validate(aid);
			
			
			investment.setAccount(account);
			investment.setType(InvestmentType.MUTUALFUNDS);
			investment.setStatus(InvestmentStatus.PENDING);
			investment.setPurchase_date(LocalDate.now());
			
			Investment savedInvestment = investmentService.insert(investment);
			
			mutualFunds.setInvestment(savedInvestment);
			
			mutualFunds.setFundName(mutualFundsDet.getFundName());
			mutualFunds.setUnitsPurchased(mutualFundsDet.getUnitsPurchased());
			mutualFunds.setPurchasePrice(mutualFundsDet.getPurchasePrice());
			mutualFunds.setTotalPrice(mutualFundsDet.getPurchasePrice()*mutualFundsDet.getUnitsPurchased());
			
			mutualFunds = mutualFundsService.insert(mutualFunds);
			
			
			return ResponseEntity.ok(mutualFunds);	
		
	}
	
	
	@PostMapping("/customer/account/investment/stocks/{aid}")
	public ResponseEntity<?> applyForStocks(@PathVariable int aid,@RequestBody Stocks stocksDet,Investment investment,Stocks stocks,ResponseMessageDto dto) throws ResourceNotFoundException{
		
		
			Account account = accountService.validate(aid);
			
			
			investment.setAccount(account);
			investment.setType(InvestmentType.STOCKS);
			investment.setStatus(InvestmentStatus.PENDING);
			investment.setPurchase_date(LocalDate.now());
			
			Investment savedInvestment = investmentService.insert(investment);
			
			stocks.setInvestment(savedInvestment);
			stocks.setNumberOfShares(stocksDet.getNumberOfShares());
			stocks.setPurchasePrice(stocksDet.getPurchasePrice());
			
			stocks = stocksService.insert(stocks);
			
			
			
			return ResponseEntity.ok(stocks);	
		
	}
	
	@PostMapping("/customer/account/investment/bonds/{aid}")
	public ResponseEntity<?> applyForBonds(@PathVariable int aid,@RequestBody Bonds bondsDet,Investment investment,Bonds bonds,ResponseMessageDto dto) throws ResourceNotFoundException{
		
		
			Account account = accountService.validate(aid);
			
			
			investment.setAccount(account);
			investment.setType(InvestmentType.BONDS);
			investment.setStatus(InvestmentStatus.PENDING);
			investment.setPurchase_date(LocalDate.now());
			
			Investment savedInvestment = investmentService.insert(investment);
		
			
			bonds.setBondType(bondsDet.getBondType());
			bonds.setFaceValue(bondsDet.getFaceValue());
			bonds.setInvestment(savedInvestment);
//			bonds.setMaturityDate(LocalDate.now().plusYears(15));
			
			bonds = bondsService.insert(bonds);
			
			
			
			return ResponseEntity.ok(bonds);	
		
	}
	
	
	@GetMapping("/customer/account/investment/{aid}")
	public ResponseEntity<?> getInvestmentDetails(@PathVariable int aid,ResponseMessageDto dto) throws ResourceNotFoundException {
		
		accountService.validate(aid);
		
		List<Investment> list = investmentService.getInvestmentDetails(aid);
		
		return ResponseEntity.ok(list);
		
		
		
	}
	
	
	
	// specific api's for front - end;
	
	@GetMapping("/customer/get/details")
	public Customer getCustomerDetailsByUsername(@RequestParam String username) {
		
		Customer customer = customerService.getCustomerDetailByUsername(username);
		
		return customer;
		
	}
	
	@GetMapping("/customer/accounts/get")
	public ResponseEntity<?> getAllAccountsByUsername(@RequestParam String username,ResponseMessageDto dto) {
		
		List<Account> list = customerService.getAllAccountsByUsername(username);
		return ResponseEntity.ok(list);
		
	}
	
	
	@PostMapping("/customer/details/update")
	public Customer updateCustomerDetails(@RequestParam String username,@RequestBody CustomerDto newCustomer) {
		Customer customer = customerService.findCustomerByUsername(username);
		
		return customerService.updateCustomerDetails(customer,newCustomer);
	
		
	}
	
//	@GetMapping("/customer/account-id")
//	public ResponseEntity<?> getAccountUsername(@RequestParam String username,ResponseMessageDto dto){
//		Account account  = accountService.getAccountByUsername(username);
//		return ResponseEntity.ok(account);
//		
//	}
	
	@GetMapping("/customer/transaction-history/get/{aid}")
	public ResponseEntity<?> getTransactionHistoryByAccountId(@PathVariable int aid,ResponseMessageDto dto) {
		
		List<Transaction> transactions = accountService.getTransactionHistoryByAccountId(aid);
		
		return ResponseEntity.ok(transactions);
		
		
	}
	
	@GetMapping("/customer/account/get/{aid}")
	public Account getAccountById(@PathVariable int aid) {
		return accountService.getAccountById(aid);
		
	}
	

	@GetMapping("/customer/account")
	public Customer getCustomerDetailsByAccount(@RequestParam String accountNumber) {
		return customerService.getCustomerDetailsByAccount(accountNumber);
		
	}
	
	@PostMapping("/customer/account/transaction/transfer")
	public Transaction postTransaction(@RequestBody TransactionDto dto) {
		return accountService.postTransaction(dto);
		
	}
	
	
	@PostMapping("/customer/account/transaction/deposit")
	public Transaction depositMoney(@RequestBody DepositDto dto) {
		return accountService.depositMoney(dto);
		
	}
	
	
	@PostMapping("/customer/account/transaction/withdraw")
	public Transaction withdrawMoney(@RequestBody WithdrawDto dto) {
		return accountService.withdrawMoney(dto);
		
	}
	
	
	@GetMapping("/customer/loan-details/get/{aid}")
	public ResponseEntity<?> getLoanDetailsByAccountId(@PathVariable int aid,ResponseMessageDto dto) {
		
		List<Loan> loans = accountService.getLoanDetailsByAccountId(aid);
		
		return ResponseEntity.ok(loans);
		
		
	}
	
	
	@GetMapping("/customer/card-details/get/{aid}")
	public ResponseEntity<?> getCardDetailsByAccountId(@PathVariable int aid,ResponseMessageDto dto) {
		
		List<Card> cards = accountService.getCardDetailsByAccountId(aid);
		
		return ResponseEntity.ok(cards);
		
		
	}
	
	@PostMapping("/customer/account/loan-apply/{aid}")
	public ResponseEntity<?> applyForLoan(@PathVariable int aid,@RequestBody Loan loan,ResponseMessageDto dto) throws ResourceNotFoundException {
		loan = accountService.applyForLoan(loan,aid);
		return ResponseEntity.ok(loan);
		
		
		
	}
	
	@PostMapping("/customer/account/card-apply/{aid}")
	public ResponseEntity<?> applyForCard(@PathVariable int aid,@RequestBody Card card,ResponseMessageDto dto) throws ResourceNotFoundException{
		card = accountService.applyForCard(card,aid);
		return ResponseEntity.ok(card);
		
	}
	
	@PostMapping("/customer/investment/FD/{aid}")
	public ResponseEntity<?> applyForFD(@PathVariable int aid,@RequestBody FixedDeposit fd,ResponseMessageDto dto){
		fd = accountService.insertFD(fd,aid);
		return ResponseEntity.ok(fd);
		
		
	}
	
	@PostMapping("/customer/investment/mutual-fund/{aid}")
	public ResponseEntity<?> applyForMutualFund(@PathVariable int aid,@RequestBody MutualFunds mutualFund,ResponseMessageDto dto){
		mutualFund = accountService.insertMutualFund(mutualFund,aid);
		return ResponseEntity.ok(mutualFund);
		
	}
	
	@PostMapping("/customer/investment/bonds/{aid}")
	public ResponseEntity<?> applyForBond(@PathVariable int aid, @RequestBody Bonds bonds,ResponseMessageDto dto){
		bonds = accountService.insertBonds(bonds,aid);
		return ResponseEntity.ok(bonds);
		
	}
}





	
	
	
	
	
	
	
	
	
	
	
	
	
	
	





























