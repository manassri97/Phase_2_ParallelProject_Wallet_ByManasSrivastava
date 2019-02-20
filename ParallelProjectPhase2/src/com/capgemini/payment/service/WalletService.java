package com.capgemini.payment.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.exceptions.ConnectionNotEstablished;
import com.capgemini.payment.exceptions.InsufficientWalletBalanceException;
import com.capgemini.payment.exceptions.PhoneNumberAlreadyExist;
import com.capgemini.payment.exceptions.TransactionFailedException;
import com.capgemini.payment.exceptions.WalletAccountDoesNotExist;

public interface WalletService {
	public boolean createTable() throws ClassNotFoundException, SQLException, ConnectionNotEstablished;
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) throws PhoneNumberAlreadyExist, SQLException;
	public Customer showBalance(String mobileNo) throws WalletAccountDoesNotExist, SQLException;
	public List<Customer> fundTransfer(String sourceMobile, String targetMobileNo, BigDecimal amount) throws InsufficientWalletBalanceException, WalletAccountDoesNotExist, TransactionFailedException, SQLException;
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws WalletAccountDoesNotExist, TransactionFailedException, SQLException;
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) throws InsufficientWalletBalanceException, WalletAccountDoesNotExist, TransactionFailedException, SQLException;
}
