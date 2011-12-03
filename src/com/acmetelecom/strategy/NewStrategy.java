package com.acmetelecom.strategy;

import java.math.BigDecimal;

import org.joda.time.Period;

import com.acmetelecom.bill.DaytimePeakPeriod;
import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

public class NewStrategy implements Strategy {

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		BigDecimal cost = null;
//		Period peakPeriod = new DaytimePeakPeriod().getPeakTimePeriod();
		
		return cost;
	}

}
