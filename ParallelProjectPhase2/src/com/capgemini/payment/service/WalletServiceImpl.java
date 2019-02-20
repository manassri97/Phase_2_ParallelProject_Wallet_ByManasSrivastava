package com.capgemini.payment.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Wallet;
import com.capgemini.payment.exceptions.ConnectionNotEstablished;
import com.capgemini.payment.exceptions.InsufficientWalletBalanceException;
import com.capgemini.payment.exceptions.PhoneNumberAlreadyExist;
import com.capgemini.payment.exceptions.TransactionFailedException;
import com.capgemini.payment.exceptions.WalletAccountDoesNotExist;
import com.capgemini.payment.repo.WalletRepo;

public class WalletServiceImpl implements WalletService {

	String id = "IGAFREF02145302";
	int counter=1;
	WalletRepo walletRepo;
	public WalletServiceImpl(WalletRepo walletRepo) {
		this.walletRepo=walletRepo;
	}

	@Override
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) throws PhoneNumberAlreadyExist, SQLException {
		Customer customer = new Customer();
		Wallet wallet = new Wallet();
		customer.setName(name);
		customer.setMobileno(mobileNo);
		wallet.setBalance(amount);
		customer.setWallet(wallet);
		if(walletRepo.save(customer))
		{
			return customer;
		}
		else
		{
			throw new PhoneNumberAlreadyExist("Wallet already exist \nTry with another mobile number");
		}
	}

	@Override
	public Customer showBalance(String mobileNo) throws WalletAccountDoesNotExist, SQLException {
		Customer customer = new Customer();
		
		customer = walletRepo.findOne(mobileNo);
		if(customer!=null)
			return customer;
		else
			throw new WalletAccountDoesNotExist("No Such Wallet Exists");
	}

	@Override
	public List<Customer> fundTransfer(String sourceMobile, String targetMobileNo, BigDecimal amount) throws InsufficientWalletBalanceException, WalletAccountDoesNotExist, TransactionFailedException, SQLException {
		Customer customer = new Customer();
		Customer customer1 = new Customer();
		List<Customer> list =new ArrayList<>();
		customer = walletRepo.findOne(sourceMobile);
		if(customer!=null)
		{
			list.add(withdrawAmount(sourceMobile, amount));
		}
		else
		{
			throw new WalletAccountDoesNotExist("Source Mobile Not Found");
		}
		customer1 = walletRepo.findOne(targetMobileNo);
		if(customer1!=null)
		{
			list.add(depositAmount(targetMobileNo, amount));
			if(walletRepo.update(customer,customer.getWallet().getBalance())) {
				if(walletRepo.update(customer1,customer1.getWallet().getBalance())) {
					walletRepo.commit();
					return list;
				}
			}
		}
		else
		{
			throw new WalletAccountDoesNotExist("Target Mobile Not Found");
		}
		return null;
		
	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws WalletAccountDoesNotExist, TransactionFailedException, SQLException {
		Customer customer = new Customer();
		
		customer = walletRepo.findOne(mobileNo);
		if(customer!=null)
		{
			customer.getWallet().setBalance(customer.getWallet().getBalance().add(amount));
			if(walletRepo.update(customer))
				return customer;
		}
		throw new WalletAccountDoesNotExist("Target Mobile Number Not Found");
	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) throws InsufficientWalletBalanceException, WalletAccountDoesNotExist, TransactionFailedException, SQLException {
		Customer customer = new Customer();
		
		customer = walletRepo.findOne(mobileNo);
		if(customer!=null)
		{
			int i=customer.getWallet().getBalance().compareTo(amount);
			if(i<0)
			{
				throw new InsufficientWalletBalanceException("Not Enough Balance");
			}
			else
			{
				customer.getWallet().setBalance(customer.getWallet().getBalance().subtract(amount));
				walletRepo.update(customer);
				return customer;
			}
 		}
		else
			throw new WalletAccountDoesNotExist("No Wallet Exist");
	}

	@Override
	public boolean createTable() throws ClassNotFoundException, SQLException, ConnectionNotEstablished {
		walletRepo.createTable();
		return false;
	}
}