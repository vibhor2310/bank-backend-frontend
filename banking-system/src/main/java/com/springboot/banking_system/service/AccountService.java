package com.springboot.banking_system.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.banking_system.dto.DepositDto;
import com.springboot.banking_system.dto.TransactionDto;
import com.springboot.banking_system.dto.WithdrawDto;
import com.springboot.banking_system.enums.InvestmentStatus;
import com.springboot.banking_system.enums.InvestmentType;
import com.springboot.banking_system.enums.TransactionType;
import com.springboot.banking_system.exception.ResourceNotFoundException;
import com.springboot.banking_system.model.Account;
import com.springboot.banking_system.model.Bonds;
import com.springboot.banking_system.model.Card;
import com.springboot.banking_system.model.Customer;
import com.springboot.banking_system.model.FixedDeposit;
import com.springboot.banking_system.model.Investment;
import com.springboot.banking_system.model.Loan;
import com.springboot.banking_system.model.MutualFunds;
import com.springboot.banking_system.model.Transaction;
import com.springboot.banking_system.repository.AccountRepository;
import com.springboot.banking_system.repository.BondsRepository;
import com.springboot.banking_system.repository.CardRepository;
import com.springboot.banking_system.repository.CustomerRepository;
import com.springboot.banking_system.repository.FixedDepositRepository;
import com.springboot.banking_system.repository.InvestmentRepository;
import com.springboot.banking_system.repository.LoanRepository;
import com.springboot.banking_system.repository.MutualFundsRepository;
import com.springboot.banking_system.repository.TransactionRepository;



@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private CardRepository cardRepository;
	
	@Autowired
	private InvestmentRepository investmentRepository;
	
	@Autowired
	private FixedDepositRepository fixedDepositRepository;
	
	@Autowired
	private MutualFundsRepository mutualFundRepository;
	
	@Autowired
	private BondsRepository bondsRepository;

	public Account insert(Account account) {
		account.setAccountNumber(generateUniqueAccountNumber());
		Optional<Customer> optional = customerRepository.findById(account.getCustomer().getId());
		Customer customer = optional.get();
		 if (customer != null) {
	            customer.setAadharNumber(account.getAadharNumber());
	            customer.setPanNumber(account.getPanNumber());
	            customerRepository.save(customer);
	        }
		return accountRepository.save(account);
		
		
	}
	

	 private String generateUniqueAccountNumber() {
	        String accountNumber;
	        do {
//	             Generate a UUID as the account number
	            accountNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
	        } while (accountRepository.findByAccountNumber(accountNumber) != null);

	        return accountNumber;
	    }
	 

	public List<Account> getAccountDetails(int cid) {
		return accountRepository.getAccountDetails(cid);
	}


	public Account validateIdAndAmount(int aid,double amount) throws ResourceNotFoundException {
		
		Optional<Account>optional = accountRepository.findById(aid);

		if(optional.isEmpty()) {
			throw new ResourceNotFoundException("Given id is invalid try again...");
		}
		if(amount<=0)
			throw new ResourceNotFoundException("Amount cannot be negative or zero");
		
		Account account = optional.get();
		
		if(account.getStatus().equalsIgnoreCase("Not Approved"))
			throw new ResourceNotFoundException("Account is not approved yet...");
		
		return account;
		
	}
	
public Account validateIdAndAmountAndBalance(int aid,double amount,String reaccno) throws ResourceNotFoundException {
		
		Optional<Account>optional = accountRepository.findById(aid);
		
		Account acc= accountRepository.findByAccountNumber(reaccno);

		if(optional.isEmpty()||acc==null) {
			throw new ResourceNotFoundException("Given id is invalid try again...");
		}
		if(amount<=0)
			throw new ResourceNotFoundException("Amount cannot be negative or zero");
		
		
		Account account = optional.get();
		
		double balance = account.getBalance();
		
		if(balance<amount)
			throw new ResourceNotFoundException("Insufficient Balance...");
		
		if(account.getStatus().equalsIgnoreCase("Not Approved"))
			throw new ResourceNotFoundException("Account is not approved yet...");
		
		return account;
		
	}


   public Account validate(int aid) throws ResourceNotFoundException {
	Optional<Account>optional = accountRepository.findById(aid);

	if(optional.isEmpty()) {
		throw new ResourceNotFoundException("Given id is invalid try again...");
	}
	
	Account account = optional.get();
	
	if(account.getStatus().equalsIgnoreCase("Not Approved"));
		
	
	return account;
}


   public Account insertUpdatedData(Account account) {
	return accountRepository.save(account);
}


   public List<Transaction> getTransactionHistory(int aid) {
	return transactionRepository.getTransactionHistory(aid);
  }


public List<Transaction> getTransactionHistoryByAccountId(int aid) {
	return transactionRepository.getTransactionHistoryByAccountId(aid);
}


public Account getAccountById(int aid) {
	return accountRepository.getAccountById(aid);
}


public Transaction postTransaction(TransactionDto dto) {
	
	Account senderAccount = accountRepository.findByAccountNumber(dto.getSenderAccountNumber());
	
	Account receiverAccount = accountRepository.findByAccountNumber(dto.getReceiverAccountNumber());
	
	senderAccount.setBalance(senderAccount.getBalance()-dto.getAmount());
	accountRepository.save(senderAccount);
	
	receiverAccount.setBalance(receiverAccount.getBalance()+dto.getAmount());
	accountRepository.save(receiverAccount);
	
	Transaction transaction = new Transaction();
	transaction.setAccount(senderAccount);
	transaction.setAmount(dto.getAmount());
	transaction.setTransactionDate(LocalDate.now());
	transaction.setTransactionType(TransactionType.TRANSFER);
	transaction.setAccountNumber(dto.getReceiverAccountNumber());
	return transactionRepository.save(transaction);
	
	
	
}


public Transaction depositMoney(DepositDto dto) {

	Account account = accountRepository.findByAccountNumber(dto.getAccountNumber());
	account.setBalance(account.getBalance()+dto.getAmount());
	accountRepository.save(account);
	Transaction transaction = new Transaction();
	transaction.setAccount(account);
	transaction.setAmount(dto.getAmount());
	transaction.setTransactionDate(LocalDate.now());
	transaction.setTransactionType(TransactionType.DEPOSIT);
	transaction.setAccountNumber(dto.getAccountNumber());
	return transactionRepository.save(transaction);
	
	
}


public Transaction withdrawMoney(WithdrawDto dto) {
	Account account = accountRepository.findByAccountNumber(dto.getAccountNumber());
	account.setBalance(account.getBalance()-dto.getAmount());
	accountRepository.save(account);
	Transaction transaction = new Transaction();
	transaction.setAccount(account);
	transaction.setAmount(dto.getAmount());
	transaction.setTransactionDate(LocalDate.now());
	transaction.setTransactionType(TransactionType.WITHDRAW);
	transaction.setAccountNumber(dto.getAccountNumber());
	return transactionRepository.save(transaction);
}


public List<Loan> getLoanDetailsByAccountId(int aid) {
	return loanRepository.getLoanDetailsByAccountId(aid);
}


public List<Card> getCardDetailsByAccountId(int aid) {
	return cardRepository.getCardDetailsByAccountId(aid);
}


public Loan applyForLoan(Loan loan, int aid) throws ResourceNotFoundException {
	Optional<Account> optional = accountRepository.findById(aid);
	if(optional.isEmpty()) {
		throw new ResourceNotFoundException("Given Account Id is not valid");
	}
	Account account = optional.get();
	
//	Loan loan = new Loan();
	loan.setAccount(account);
	loan.setAmount(loan.getAmount());
	loan.setDateCreated(LocalDate.now());
	loan.setLoanType(loan.getLoanType());
	loan.setPurpose(loan.getPurpose());
	loan.setStatus("NOT_APPROVED");
	return loanRepository.save(loan);
	
}


public Card applyForCard(Card card, int aid) throws ResourceNotFoundException {
	Optional<Account> optional = accountRepository.findById(aid);
	if(optional.isEmpty()) {
		throw new ResourceNotFoundException("Given Account Id is not valid");
	}
	Account account = optional.get();
	
	card.setAccount(account);
	card.setBalance(account.getBalance());
	card.setCardNumber(generateUniqueCardNumber());
	card.setCvv(generateUniqueCVV());
	card.setCardType(card.getCardType());
	card.setExpiryDate(LocalDate.now().plusYears(5));
	card.setStatus("APPROVED");
	return cardRepository.save(card);
	
	
	
}


private String generateUniqueCardNumber() {
    String cardNumber;
    Random random = new Random();
    do {
    	// Generate a random 16-digit number
        cardNumber = String.format("%016d", random.nextLong() & Long.MAX_VALUE);
    } while (cardRepository.findByCardNumber(cardNumber) != null);

    return cardNumber;
}

private String generateUniqueCVV() {
    String cvv;
    Random random = new Random();

    do {
        // Generate a random 3-digit number
        cvv = String.format("%03d", random.nextInt(1000)); // generates a number between 0 and 999
    } while (cardRepository.findByCvv(cvv) != null); // Check for uniqueness

    return cvv;
}


public FixedDeposit insertFD(FixedDeposit fd, int aid) {
	Account account = accountRepository.findById(aid).get();
	Investment investment = new Investment();
	investment.setAccount(account);
	investment.setPurchase_date(LocalDate.now());
	investment.setType(InvestmentType.FIXEDDEPOSITS);
	investment.setStatus(InvestmentStatus.APPROVED);
	investment = investmentRepository.save(investment);
	fd.setInvestment(investment);
	fd.setDepositAmount(fd.getDepositAmount());
	fd.setYears(fd.getYears());
	fd.setMaturityDate(LocalDate.now().plusYears(fd.getYears()));
	return fixedDepositRepository.save(fd);
	
}


public MutualFunds insertMutualFund(MutualFunds mutualFund, int aid) {
	Account account = accountRepository.findById(aid).get();
	Investment investment = new Investment();
	investment.setAccount(account);
	investment.setPurchase_date(LocalDate.now());
	investment.setType(InvestmentType.MUTUALFUNDS);
	investment.setStatus(InvestmentStatus.APPROVED);
	investment = investmentRepository.save(investment);
	mutualFund.setFundName("Hexaware Index Fund");
	mutualFund.setInvestment(investment);
	mutualFund.setPurchasePrice(mutualFund.getPurchasePrice());
	mutualFund.setUnitsPurchased(mutualFund.getUnitsPurchased());
	mutualFund.setTotalPrice(mutualFund.getTotalPrice());
	return mutualFundRepository.save(mutualFund);
	
}


public Bonds insertBonds(Bonds bonds, int aid) {
	Account account = accountRepository.findById(aid).get();
	Investment investment = new Investment();
	investment.setAccount(account);
	investment.setPurchase_date(LocalDate.now());
	investment.setType(InvestmentType.BONDS);
	investment.setStatus(InvestmentStatus.APPROVED);
	investment = investmentRepository.save(investment);
	bonds.setBondType(bonds.getBondType());
	bonds.setFaceValue(bonds.getFaceValue());
	bonds.setInterestRate(bonds.getInterestRate());
	bonds.setInvestment(investment);
	return bondsRepository.save(bonds);
}


//public Account getAccountByUsername(String username) {
//	return accountRepository.getAccountByUsername(username);
//}


//public Account addAccount(Account account) {
//	
//}



}
