package com.acmetelecom.call;

public class UnexpectedCallException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnexpectedCallException(String message){
		super(message);
	}

}
