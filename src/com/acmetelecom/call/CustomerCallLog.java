package com.acmetelecom.call;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
	public void callInitiated(Customer caller, Customer callee) {
		CallEvent currentCall = currentCalls.get(caller.getPhoneNumber());
		if(currentCall != null){
			throw new UnexpectedCallException("Call initiated twice for same number.");
		}
		//store an event marking the initialisation of the phone call
		currentCalls.put(caller.getPhoneNumber(), 
				new CallEvent(caller,callee, clock.getCurrentTime()));
	}

	@Override
	public void callCompleted(Customer caller, Customer callee) {
		String key = caller.getPhoneNumber();
		CallEvent begin = currentCalls.get(key);
		
		//Check for error cases
		if(begin == null){
		    return;
			//throw new UnexpectedCallException("Call completed event received without initialization.");
		}
		if(!begin.getCallee().equals(callee.getPhoneNumber())){
			throw new UnexpectedCallException("Call completed event for different call than initialised.");
		}
		
		//remove the beginning event stored
		currentCalls.remove(key);
		CallEvent end = new CallEvent(caller, callee, clock.getCurrentTime());
		List<Call> calls = userCalls.get(key);
		//setup initial list of calls
		if(calls == null){
			calls = new ArrayList<Call>(10);
			userCalls.put(key, calls);
		}
		//add new call to the list
		Call newCall = new Call(begin, end);
		calls.add(newCall);
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
