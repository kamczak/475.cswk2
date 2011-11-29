package com.acmetelecom.call;

import com.acmetelecom.customer.Customer;

public interface Callable {

	public void callInitiated(Customer caller, Customer callee);

	public void callCompleted(Customer caller, Customer callee);
	
}
