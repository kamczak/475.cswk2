package com.acmetelecom.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.joda.time.Interval;

import com.acmetelecom.bill.DaytimePeakPeriod;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

public class OldStrategy implements Strategy {

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		Interval callInterval = new Interval(call.startTime(), call.endTime());
		
		BigDecimal multiplyRate;
		if (PeakPeriod.PEAK_7_TO_19.getOverlappingIntervals(callInterval).isEmpty()) {
		    multiplyRate = tariff.offPeakRate();
		}
		else {
		    multiplyRate = tariff.peakRate();
		}

		return new BigDecimal(call.durationSeconds())
		        .multiply(multiplyRate)
		        .setScale(0, RoundingMode.HALF_UP);
	}

}
