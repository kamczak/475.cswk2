package com.acmetelecom.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

public class NewStrategy implements Strategy {

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		Interval callInterval = new Interval(call.startTime(), call.endTime());

		Duration combinedPeakDuration = new Duration(0);
		for (Interval peakInterval : PeakPeriod.PEAK_7_TO_19.getOverlappingIntervals(callInterval)) {
            combinedPeakDuration = combinedPeakDuration.plus(peakInterval.toDuration());
        }

		Duration combinedOffPeakDuration = callInterval.toDuration().minus(combinedPeakDuration);
		
		BigDecimal peakCost    = new BigDecimal(combinedPeakDuration.getStandardSeconds())
		                                 .multiply(tariff.peakRate());
		BigDecimal offPeakCost = new BigDecimal(combinedOffPeakDuration.getStandardSeconds())
		                                 .multiply(tariff.offPeakRate());

		return peakCost.add(offPeakCost).setScale(0, RoundingMode.HALF_UP);
	}

}
