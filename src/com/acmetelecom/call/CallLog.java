package com.acmetelecom.call;

import java.util.List;

import com.acmetelecom.customer.Customer;

public interface CallLog {
	
	/**
	 * Event to notify that the call has been initiated between
	 * the caller to the callee
	 * 
	 * @param caller
     *          String object representing phone number of the caller
     * @param callee
     *          String object representing phone number of the callee
     *          
	 * @throws UnexpectedCallException
	 * 			When caller has already initiated another call
	 */
	public void callInitiated(String caller, String callee) throws UnexpectedCallException;

	/** 
	 * Event to notify that the call has been completed between
	 * the caller to the callee
	 * 
	 * @param caller
     *          String object representing phone number of the caller
     * @param callee
     *          String object representing phone number of the callee
     *          
	 * @throws UnexpectedCallException
	 * 			When attempting to complete a call between a caller and a non-matching callee
	 */
	public void callCompleted(String caller, String callee) throws UnexpectedCallException;
	
	/**
	 * Returns list of completed calls given a customer
	 * 
	 * @param customer
	 * 			Customer object for whom we want to retrieve the list of completed calls
	 * @return
	 *			List of Call objects representing completed calls initiated by customer
	 **/	
	public List<Call> getCalls(Customer customer);

}
