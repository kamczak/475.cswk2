package com.acmetelecom.bill;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.acmetelecom.call.Call;

class BillLineItem {
	private Call call;
	private BigDecimal callCost;

	public BillLineItem(Call call, BigDecimal callCost) {
		this.call = call;
		this.callCost = callCost;
	}

	public DateTime date() {
		return call.startTime();
	}

	public String callee() {
		return call.callee();
	}

	public String durationMinutes() {
	    long durationSeconds = call.getDuration().getStandardSeconds();
	    
	    StringBuilder minutes = new StringBuilder();
	    minutes.append(durationSeconds / 60);
		minutes.append(":");
		minutes.append(String.format("%02d", durationSeconds % 60));
		return minutes.toString();
	}

	public BigDecimal cost() {
		return callCost;
	}
}