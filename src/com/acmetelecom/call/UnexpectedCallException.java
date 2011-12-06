package com.acmetelecom.call;

//Exception for the purpose of erroneous events happening within CallLog
@SuppressWarnings("serial")
public class UnexpectedCallException extends RuntimeException {

	public UnexpectedCallException(String message) {
		super(message);
	}

}
