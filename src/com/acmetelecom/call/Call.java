package com.acmetelecom.call;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

/**
 * This class encapsulates all information about calls
 */
public class Call {
	private CallEvent start;
	private CallEvent end;
	// Records the call interval
	private Interval callInterval;

	public Call(CallEvent start, CallEvent end) {
		this.start = start;
		this.end = end;
		callInterval = new Interval(start.getDateTime(), end.getDateTime());
	}

	public String getCallee() {
		return start.getCallee();
	}
	
	public Duration getDuration() {
		// Returns call duration from which call length can be obtained
	    return callInterval.toDuration();
	}
	
	public DateTime getStartDateTime() {
		return start.getDateTime();
	}

	public DateTime getEndDateTime() {
		return end.getDateTime();
	}
}
