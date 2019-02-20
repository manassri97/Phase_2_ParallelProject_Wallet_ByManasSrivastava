package com.capgemini.payment.exceptions;

public class WalletAccountDoesNotExist extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WalletAccountDoesNotExist(String string) {
		super(string);
	}

}
