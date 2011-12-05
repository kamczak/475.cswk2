package com.acmetelecom.call;

//Exception for the purpose of erroneous events happening within CallLog
public class UnexpectedCallException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnexpectedCallException(String message){
		super(message);
	}

}
