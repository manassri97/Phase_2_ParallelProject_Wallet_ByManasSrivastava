package com.capgemini.payment.repo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.bean.Wallet;
import com.capgemini.payment.exceptions.ConnectionNotEstablished;
import com.capgemini.payment.util.Util;

public class WalletRepoImpl implements WalletRepo 
{
	Connection con = Util.getConnection();
	
	@Override
	public boolean save(Customer customer) throws SQLException 
	{
		if(findOne(customer.getMobileno())==null)
		{
		PreparedStatement pstmt = con.prepareStatement("insert into w_customer values(?,?)");
		PreparedStatement pstmt1 = con.prepareStatement("insert into w_wallet values(?,?)");
		pstmt.setLong(1, Long.parseLong(customer.getMobileno()));
		pstmt.setString(2, customer.getName());
		pstmt1.setLong(1, Long.parseLong(customer.getMobileno()));
		pstmt1.setBigDecimal(2, customer.getWallet().getBalance());
		pstmt.executeUpdate();
		pstmt1.executeUpdate();
		return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Customer findOne(String mobileNo) throws SQLException
	{
		Customer customer = new Customer();
		Wallet wallet = new Wallet();
		PreparedStatement pstmt = con.prepareStatement("select name from w_customer where phone_number = ?");
		pstmt.setString(1, mobileNo);
		PreparedStatement pstmt1 = con.prepareStatement("select balance from w_wallet where number1 = ?");
		pstmt1.setString(1, mobileNo);
		ResultSet rs = pstmt.executeQuery();
		ResultSet rs1 = pstmt1.executeQuery();
		if(rs.next() && rs1.next()) {
		customer.setName(rs.getString(1));
		customer.setMobileno(mobileNo);
		rs.close();
		wallet.setBalance(new BigDecimal(rs1.getInt(1)));
		customer.setWallet(wallet);
		rs1.close();
		return customer;
		}
		else
		{
			rs.close();
			rs1.close();
			return null;
		}
	}

	@Override
	public boolean createTable() throws ClassNotFoundException, SQLException, ConnectionNotEstablished
	{
			
			if(con!=null)
			{
				try {
				Statement stmt = con.createStatement();
				stmt.executeQuery("Create Table w_customer(phone_number number(10) primary key, name varchar2(20) NOT NULL)");
				stmt.executeQuery("Create Table w_wallet (number1 number(10) NOT NULL, balance number(10) NOT NULL, foreign key(number1) references w_customer(phone_number))");
				return true;
				}
				catch(SQLException e)
				{
					return true;
				}
			}
			else
			{
				throw new ConnectionNotEstablished("Cannot connect to server");
			}
	}
	
	@Override
	public boolean update(Customer customer) throws SQLException
	{
		con.setAutoCommit(false);
		PreparedStatement pstmt = con.prepareStatement("update w_wallet set balance = ? where number1=?");
		pstmt.setBigDecimal(1, customer.getWallet().getBalance());
		pstmt.setLong(2, Long.parseLong(customer.getMobileno()));
		if(pstmt.executeUpdate()>0) {
			con.commit();
			return true;
		}
		else
			return false;
	}
	
	@Override
	public boolean update(Customer customer, BigDecimal amount) throws SQLException
	{
		con.setAutoCommit(false);
		PreparedStatement pstmt = con.prepareStatement("update w_wallet set balance = ? where number1=?");
		pstmt.setBigDecimal(1, amount);
		pstmt.setLong(2, Long.parseLong(customer.getMobileno()));
		if(pstmt.executeUpdate()>0)
			return true;
		else
			return false;
	}

	@Override
	public void commit() throws SQLException {
		con.commit();
	}
}