package com.acmetelecom.call;

/**
 * Exception for the purpose of raising awareness of 
 * erroneous call events
 */

@SuppressWarnings("serial")
public class UnexpectedCallException extends RuntimeException {
	
	public UnexpectedCallException(String message) {
		super(message);
	}
}
