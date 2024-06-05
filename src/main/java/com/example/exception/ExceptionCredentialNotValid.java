package com.example.exception;

public class ExceptionCredentialNotValid extends RuntimeException {

	private static final long serialVersionUID = -409743470779314218L;
	
	public ExceptionCredentialNotValid(String  exception) {
		super(exception);
	}

}