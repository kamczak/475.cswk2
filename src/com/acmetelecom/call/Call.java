package com.acmetelecom.call;

import java.util.Date;

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
		return DateUtils.dateToBillingFormat(start.dateTime().toDate());
	}

	public Date startTime() {
		return start.dateTime().toDate();
	}

	public Date endTime() {
		return end.dateTime().toDate();
	}
}
