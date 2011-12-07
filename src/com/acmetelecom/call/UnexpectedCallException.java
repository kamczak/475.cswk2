package com.acmetelecom.call;

/**
 * Exception for the purpose of raising awareness of 
 * erroneous call events
 */
@SuppressWarnings("serial")
public class UnexpectedCallException extends RuntimeException {
	static final String MORE_THAN_ONE_CALL_FOR_SAME_NUMBER
            = "Cannot initiate more than one call for same caller number";

    static final String CALL_FOR_DIFFERENT_CALEE_THAN_INITIATED
            ="Cannot complete call for different callee than initialised";

    public UnexpectedCallException(String message) {
		super(message);
	}
}
