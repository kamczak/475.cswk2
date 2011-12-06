package com.acmetelecom.call;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.time.Clock;
import com.google.inject.Inject;

public class CustomerCallLog implements CallLog {
	
	//Stores calls of each phone number
	private Map<String, List<Call>> userCalls;
	//Stores partial call began by each user
	private Map<String, CallEvent> initiatedCalls;
	//System clock
	private Clock clock;
	
	@Inject
	public CustomerCallLog(Clock clock){
		//We use concurrent hash map to make sure the system works in
		// distributed environment
		userCalls = new ConcurrentHashMap<String, List<Call>>();
		initiatedCalls = new ConcurrentHashMap<String, CallEvent>();
		this.clock = clock;
	}
	
	@Override
	public void callInitiated(String caller, String callee) throws UnexpectedCallException{
		CallEvent currentCall = initiatedCalls.get(caller);
		if (currentCall != null) {
			throw new UnexpectedCallException("Call initiated twice for same number.");
		}
		// Store an event marking the initialisation of the phone call
		initiatedCalls.put(caller, new CallEvent(caller,callee, clock.getCurrentDateTime()));
	}

	@Override
	public void callCompleted(String caller, String callee) throws UnexpectedCallException {
		CallEvent begin = initiatedCalls.get(caller);
		if (begin == null) {
			//In the previous version we skipped endings for all uniti
		    return;
		}

		if (!begin.getCallee().equals(callee)){
			throw new UnexpectedCallException("Call completed event for different call than initialised.");
		}
		// Remove the beginning event stored
		initiatedCalls.remove(caller);
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
