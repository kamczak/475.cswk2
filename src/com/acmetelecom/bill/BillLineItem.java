package com.acmetelecom.bill;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.acmetelecom.call.Call;

class BillLineItem {
	private Call call;
	private BigDecimal callCost;

	public BillLineItem(Call call, BigDecimal callCost) {
		this.call = call;
		this.callCost = callCost;
	}

	public DateTime getDate() {
		return call.getStartDateTime();
	}

	public String getCallee() {
		return call.getCallee();
	}

	public Duration getDuration() {
	    return call.getDuration();
	}

	public BigDecimal cost() {
		return callCost;
	}
}