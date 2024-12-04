package com.assignment.application.exception;

public class MortgageServiceException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

	public MortgageServiceException(String message) {
        super(message);
    }

    public MortgageServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}