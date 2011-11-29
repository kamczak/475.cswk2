package com.acmetelecom.call;

import java.util.Collection;
import java.util.List;

import com.acmetelecom.customer.Customer;

public interface CallLog extends Callable {
	
	public List<Call> getCalls(Customer c);

}
