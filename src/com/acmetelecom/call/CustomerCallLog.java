package com.acmetelecom.call;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.time.Clock;

public class CustomerCallLog implements CallLog {

	private Map<String, List<Call>> userCalls;
	private Map<String, CallEvent> currentCalls;
	private Clock clock;
	
	public CustomerCallLog(Clock clock){
		userCalls = new ConcurrentHashMap<String, List<Call>>();
		currentCalls = new ConcurrentHashMap<String, CallEvent>();
		this.clock = clock;
	}
	
	@Override
	public void callInitiated(String caller, String callee) {
		CallEvent currentCall = currentCalls.get(caller);
		if (currentCall != null) {
			throw new UnexpectedCallException("Call initiated twice for same number.");
		}

		// Store an event marking the initialisation of the phone call
		currentCalls.put(caller, new CallEvent(caller,callee, clock.getCurrentDateTime()));
	}

	@Override
	public void callCompleted(String caller, String callee) {
		CallEvent begin = currentCalls.get(caller);
		if (begin == null) {
		    return;
		}

		if (!begin.getCallee().equals(callee)){
			throw new UnexpectedCallException("Call completed event for different call than initialised.");
		}

		// Remove the beginning event stored
		currentCalls.remove(caller);
		CallEvent end = new CallEvent(caller, callee, clock.getCurrentDateTime());
		List<Call> calls = userCalls.get(caller);

		// Setup initial list of calls
		if(calls == null){
			calls = new ArrayList<Call>(10);
			userCalls.put(caller, calls);
		}

		// Add new call to the list
		calls.add(new Call(begin, end));
	}

	@Override
	public List<Call> getCalls(Customer c) {
		List<Call> result = userCalls.get(c.getPhoneNumber());
		if (result == null) {
		    result = new ArrayList<Call>();
		}
		return result;
	}

}
