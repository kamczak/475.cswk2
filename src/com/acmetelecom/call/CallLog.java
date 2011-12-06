package com.acmetelecom.call;

import java.util.List;

import com.acmetelecom.customer.Customer;

public interface CallLog {
	
	/**
	 * Event to notify that the call has been initiated between
	 * the caller to the callee
	 */
	public void callInitiated(String caller, String callee) throws UnexpectedCallException;

	/** 
	 * Event to notify that the call has been completed between
	 * the caller to the callee
	 */
	public void callCompleted(String caller, String callee) throws UnexpectedCallException;
	
	/**
	 * Returns list of calls given a customer
	 */
	public List<Call> getCalls(Customer c);

}
