package com.capgemini.payment.repo;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.exceptions.ConnectionNotEstablished;

public interface WalletRepo {
	public boolean createTable() throws ClassNotFoundException, SQLException, ConnectionNotEstablished;
	public boolean save(Customer customer) throws SQLException;
	public Customer findOne(String mobileNo) throws SQLException;
	public boolean update(Customer customer) throws SQLException;
	public boolean update(Customer customer, BigDecimal amount) throws SQLException;
	public void commit() throws SQLException;
}
