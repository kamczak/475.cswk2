package com.acmetelecom.call;

import org.joda.time.DateTime;

import com.acmetelecom.time.DateUtils;

public class Call {
	private CallEvent start;
	private CallEvent end;

	public Call(CallEvent start, CallEvent end) {
		this.start = start;
		this.end = end;
	}

	public String callee() {
		return start.getCallee();
	}

	public int durationSeconds() { 
		return  (int) (end.dateTime().toDate().getTime()-start.dateTime().toDate().getTime())/1000;
	}

	public String date() {
		return DateUtils.dateToBillingFormat(start.dateTime());
	}

	public DateTime startTime() {
		return start.dateTime();
	}

	public DateTime endTime() {
		return end.dateTime();
	}
}
