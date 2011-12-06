package com.acmetelecom.bill;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.acmetelecom.call.Call;

/**
 * A thin wrapper around call that also contains its cost
 */
class BillLineItem {
	private Call call;
	private BigDecimal callCost;

	public BillLineItem(Call call, BigDecimal callCost) {
		this.call = call;
		this.callCost = callCost;
	}

	public DateTime getStartDateTime() {
		return call.getStartDateTime();
	}

	public String getCallee() {
		return call.getCallee();
	}

	public Duration getDuration() {
	    return call.getDuration();
	}

	public BigDecimal getCost() {
		return callCost;
	}
}