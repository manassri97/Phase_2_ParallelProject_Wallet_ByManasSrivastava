package com.capgemini.payment.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Wallet;
import com.capgemini.payment.exceptions.InsufficientWalletBalanceException;
import com.capgemini.payment.exceptions.PhoneNumberAlreadyExist;
import com.capgemini.payment.exceptions.TransactionFailedException;
import com.capgemini.payment.exceptions.WalletAccountDoesNotExist;
import com.capgemini.payment.repo.WalletRepo;
import com.capgemini.payment.repo.WalletRepoImpl;
import com.capgemini.payment.service.WalletService;
import com.capgemini.payment.service.WalletServiceImpl;

public class TestCases {
	
	WalletRepo walletRepo;
	WalletService walletService;
	Connection con;
	@Before
	public void setUp() throws Exception {
		walletRepo = new WalletRepoImpl();
		walletService = new WalletServiceImpl(walletRepo);
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "Capgemini123");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		walletService.createTable();
	}
	
	/*
	 * case : Wallet Creation
	 * 
	 */
	@Test
	public void whenWalletAccountIsCreatedSuccessfully() throws PhoneNumberAlreadyExist, SQLException { 
		
		Customer customer = new Customer();
		Wallet wallet = new Wallet();
		wallet.setBalance(new BigDecimal(500));
		customer.setName("manas");
		customer.setMobileno("9795716444");
		customer.setWallet(wallet);
		
		assertEquals(customer, walletService.createAccount("manas", "9795716444", new BigDecimal(500)));
	}
	
	@Test(expected=com.capgemini.payment.exceptions.PhoneNumberAlreadyExist.class)
	public void whenWalletMoblileNumberAlreadyExist() throws PhoneNumberAlreadyExist, SQLException { 
		
		walletService.createAccount("manas", "9795716305", new BigDecimal(500));
		walletService.createAccount("manas", "9795716305", new BigDecimal(500));
	}
	
	/*
	 * case : Withdraw From Wallet
	 * 
	 */
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenWalletAccountDoesNotExistWhileWithdrawing() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		
		walletService.createAccount("manas", "9795716325", new BigDecimal(500));
		walletService.withdrawAmount("9795716335",new BigDecimal(500));
	}
	
	@Test(expected=com.capgemini.payment.exceptions.InsufficientWalletBalanceException.class)
	public void whenWalletHaveInsufficientBalanceWhileWithdrawing() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		
		walletService.createAccount("manas", "9795716345", new BigDecimal(500));
		walletService.withdrawAmount("9795716345",new BigDecimal(600));
	}
	
	@Test
	public void whenWithdrawFromWalletSuccessfullWhileWithdrawing() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		
		walletService.createAccount("manas", "9795716315", new BigDecimal(500));
		walletService.withdrawAmount("9795716315",new BigDecimal(400));
	}
	
	/*
	 * case : Deposit In Wallet
	 * 
	 */
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenWalletAccountDoesNotExistWhileDepositing() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		
		walletService.createAccount("manas", "9795516345", new BigDecimal(500));
		walletService.depositAmount("9795516335",new BigDecimal(500));
	}
	
	@Test
	public void whenDepositFromWalletSuccessfull() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		
		walletService.createAccount("manas", "9795716355", new BigDecimal(500));
		walletService.depositAmount("9795716355",new BigDecimal(400));
	}
	
	/*
	 * case : Show Wallet Balance
	 * 
	 */
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenShowWalletBalanceIsCalledThenWalletDoesNotExist() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		
		walletService.createAccount("manas", "9795716365", new BigDecimal(500));
		walletService.showBalance("9795716335");
	}
	
	@Test
	public void whenShowWalletBalanceIs() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
	
		walletService.createAccount("manas", "9795716375", new BigDecimal(500));
		walletService.showBalance("9795716375");
	}
	
	/*
	 * case : Fund Transfer
	 * 
	 */
	
	@Test(expected=com.capgemini.payment.exceptions.InsufficientWalletBalanceException.class)
	public void whenSourceWalletBalanceIsInsufficient() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		
		walletService.createAccount("manas", "9795716385", new BigDecimal(500));
		walletService.createAccount("baba", "9120760423", new BigDecimal(500));
		walletService.withdrawAmount("9795716385",new BigDecimal(600));
		walletService.depositAmount("9120760423", new BigDecimal(600));
	}
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenSourceWalletAccountDoesNotExist() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		walletService.createAccount("manas", "9795716395", new BigDecimal(500));
		walletService.createAccount("baba", "9120760410", new BigDecimal(500));
		walletService.withdrawAmount("9795716335",new BigDecimal(400));
		walletService.depositAmount("9120760410", new BigDecimal(400));
	}
	
	@Test(expected=com.capgemini.payment.exceptions.WalletAccountDoesNotExist.class)
	public void whenTargetWalletAccountDoesNotExist() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		walletService.createAccount("manas", "8795716325", new BigDecimal(500));
		walletService.createAccount("baba", "9120760400", new BigDecimal(500));
		walletService.withdrawAmount("8795716325",new BigDecimal(400));
		walletService.depositAmount("9120760412", new BigDecimal(400));
	}
	
	@Test
	public void whenFundTransferIsSuccessfull() throws WalletAccountDoesNotExist, InsufficientWalletBalanceException, TransactionFailedException, PhoneNumberAlreadyExist, SQLException { 
		walletService.createAccount("manas", "9795716312", new BigDecimal(500));
		walletService.createAccount("baba", "9120760413", new BigDecimal(500));
		walletService.withdrawAmount("9795716312",new BigDecimal(400));
		walletService.depositAmount("9120760413", new BigDecimal(400));
	}
}