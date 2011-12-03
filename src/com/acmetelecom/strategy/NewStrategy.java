package com.acmetelecom.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.joda.time.Interval;

import com.acmetelecom.bill.DaytimePeakPeriod;
import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

public class NewStrategy implements Strategy {

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		BigDecimal cost = null;

		Interval callInterval = new Interval(call.startDateTime(), call.endDateTime());
		List<Interval> peakIntervals = new DaytimePeakPeriod().getPeakTimeIntervals(callInterval);
		
		int peakDuration = 0;
		for (Interval peakTime : peakIntervals) {
			peakDuration += (int) callInterval.overlap(peakTime).toDuration().getStandardSeconds();
		}
		
		int offPeakDuration = (int) callInterval.toDuration().getStandardSeconds() - peakDuration;
		
		cost = new BigDecimal(peakDuration).multiply(tariff.peakRate()).add(new BigDecimal(offPeakDuration).multiply(tariff.offPeakRate()));
		cost = cost.setScale(0, RoundingMode.HALF_UP);
		return cost;
	}

}
