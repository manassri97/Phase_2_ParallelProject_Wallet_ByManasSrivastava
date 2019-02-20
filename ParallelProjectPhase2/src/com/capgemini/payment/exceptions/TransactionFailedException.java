package com.capgemini.payment.exceptions;

public class TransactionFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionFailedException(String string) {
		super(string);
	}

}
