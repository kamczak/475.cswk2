package com.acmetelecom.call;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

//Stores information about the beginning and end event of each call
public class Call {
	private CallEvent start;
	private CallEvent end;
	private Interval callInterval;

	public Call(CallEvent start, CallEvent end) {
		this.start = start;
		this.end = end;
		callInterval = new Interval(start.dateTime(), end.dateTime());
	}

	public String callee() {
		return start.getCallee();
	}
	
	public Duration getDuration() {
	    return callInterval.toDuration();
	}
	
	public DateTime startDateTime() {
		return start.dateTime();
	}

	public DateTime endTime() {
		return end.dateTime();
	}
}
