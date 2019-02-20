package com.capgemini.payment.UI;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.capgemini.payment.bean.Customer;
import com.capgemini.payment.exceptions.ConnectionNotEstablished;
import com.capgemini.payment.exceptions.InsufficientWalletBalanceException;
import com.capgemini.payment.exceptions.PhoneNumberAlreadyExist;
import com.capgemini.payment.exceptions.TransactionFailedException;
import com.capgemini.payment.exceptions.WalletAccountDoesNotExist;
import com.capgemini.payment.repo.WalletRepo;
import com.capgemini.payment.repo.WalletRepoImpl;
import com.capgemini.payment.service.WalletService;
import com.capgemini.payment.service.WalletServiceImpl;

public class UserInterface {
	private static Scanner sc = new Scanner(System.in);
	static WalletRepo walletRepo = new WalletRepoImpl();
	static WalletService walletService = new WalletServiceImpl(walletRepo);

	public static void main(String[] args) {

		showMenu();
	}

	private static void showMenu() {
		int choice;
		Customer customer = new Customer();
		String mobileNo,mobileNo2,name;
		BigDecimal balance;
		try {
			walletService.createTable();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConnectionNotEstablished e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true)
		{
			System.out.println("1. Create Account \n"
					+"2. Show Wallet Balance \n"
					+"3. Fund Transfer to other Wallet \n"
					+"4. Withdraw from wallet \n"
					+"5. Deposit to wallet \n"
					+"6. Print Transactions \n"
					+"7. Exit \n");
			choice = sc.nextInt();
			sc.nextLine();
			switch(choice)
			{
			case 1 :System.out.println("enter the name of customer");
			name=sc.nextLine();
			if(validateName(name))
			{
				System.out.println("enter the phone number of customer");
				mobileNo=sc.nextLine();
				if(validateMobileNo(mobileNo))
				{
					System.out.println("enter the balance of customer");
					balance=sc.nextBigDecimal();

					try {
						customer = walletService.createAccount(name, mobileNo, balance);
						System.out.println("Wallet Successfully Created \n"
								+ "Name :"+customer.getName()+"\n"
								+ "Mobile Number :"+customer.getMobileno()+"\n"
								+ "Wallet Balance :"+customer.getWallet().getBalance()+"\n");
					} catch (PhoneNumberAlreadyExist e) {
						System.out.println(e.getMessage());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				else
				{
					System.out.println("enter phone number correctly");
				}
			}
			else
			{
				System.out.println("enter name correctly");
			}
			break;
			case 2 :System.out.println("enter the phone number of your wallet");
			mobileNo=sc.nextLine();
			if(validateMobileNo(mobileNo))
			{
				try {
					customer = walletService.showBalance(mobileNo);
					System.out.println("Your Wallet balance :"+customer.getWallet().getBalance()+"\n");
				} catch (WalletAccountDoesNotExist e) {
					System.out.println(e.getMessage());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("enter phone number correctly");
			}
			break;
			case 3 :System.out.println("enter the phone number of your wallet");
			mobileNo=sc.nextLine();
			if(validateMobileNo(mobileNo))
			{

				System.out.println("enter the phone number of wallet in which amount is to be transferred");
				mobileNo2=sc.nextLine();
				if(validateMobileNo(mobileNo2))
				{
					System.out.println("enter the amount to be transferred");
					balance = sc.nextBigDecimal();
					List<Customer> list = new ArrayList<>();
					try {
						list = walletService.fundTransfer(mobileNo, mobileNo2, balance);
						System.out.println("Your Wallet balance :"+list+"\n");
					} catch (InsufficientWalletBalanceException e){
						System.out.println(e.getMessage());
					} catch (WalletAccountDoesNotExist e) {
						System.out.println(e.getMessage());
					} catch (TransactionFailedException e) {
						System.out.println(e.getMessage());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					System.out.println("enter phone number correctly");
				}
			}
			else
			{
				System.out.println("enter phone number correctly");
			}
			break;
			case 4 :System.out.println("enter the phone number of your wallet");
			mobileNo=sc.nextLine();
			if(validateMobileNo(mobileNo))
			{
				System.out.println("enter the amount to be deducted");
				balance = sc.nextBigDecimal();
				try {
					customer = walletService.withdrawAmount(mobileNo, balance);
					System.out.println("Your Wallet balance :"+customer.getWallet().getBalance()+"\n");
				} catch (InsufficientWalletBalanceException e) {
					System.out.println(e.getMessage());
				} catch (WalletAccountDoesNotExist e) {
					System.out.println(e.getMessage());
				} catch (TransactionFailedException e) {
					System.out.println(e.getMessage());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else
			{
				System.out.println("enter phone number correctly");
			}
			break;
			case 5 :System.out.println("enter the phone number of your wallet");
					mobileNo=sc.nextLine();
					if(validateMobileNo(mobileNo))
					{
						System.out.println("enter the amount to be added");
						balance = sc.nextBigDecimal();
						try {
							customer = walletService.depositAmount(mobileNo, balance);
							System.out.println("Your Wallet balance :"+customer.getWallet().getBalance()+"\n");
						} catch (WalletAccountDoesNotExist e) {
							System.out.println(e.getMessage());
						} catch (TransactionFailedException e) {
							System.out.println(e.getMessage());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		
					}
					else
					{
						System.out.println("enter phone number correctly");
					}
					break;
			case 7 :System.exit(0);
			
			default :System.out.println("Wrong choice selection");
			}
		}
	}
	private static boolean validateMobileNo(String mobileNo) {
		char[] ch = mobileNo.toCharArray();
		if(mobileNo.length()!=10)
		{
			return false;
		}
		for(int i=0;i<ch.length;i++)
		{
			if(ch[i]<'0' || ch[i]>'9' || ch[i]==' ')
			{
				return false;
			}
		}
		return true;
	}

	private static boolean validateName(String name) 
	{
		name=name.toLowerCase();
		char[] ch = name.toCharArray();
		if(name.length()==0)
		{
			return false;
		}
		for(int i=0;i<ch.length;i++)
		{
			if(!((ch[i]>='a' && ch[i]<='z') || ch[i]==' '))
			{
				return false;
			}
		}
		return true;
	}
}