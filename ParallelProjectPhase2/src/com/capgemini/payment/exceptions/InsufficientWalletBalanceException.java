package com.capgemini.payment.exceptions;

public class InsufficientWalletBalanceException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientWalletBalanceException(String string) {
		super(string);
	}

}
