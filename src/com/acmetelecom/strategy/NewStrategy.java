package com.acmetelecom.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;
import com.google.inject.Inject;

public class NewStrategy implements Strategy {
    private PeakPeriod peakPeriod;
    
    @Inject
    public NewStrategy(PeakPeriod peakPeriod) {
        this.peakPeriod = peakPeriod;
    }

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		Interval callInterval = new Interval(call.getStartDateTime(), call.getEndDateTime());

		Duration combinedPeakDuration = Duration.ZERO;
		Interval overlap;
		for (Interval peakInterval : peakPeriod.getRelevantPeakIntervals(callInterval)) {
		    overlap = peakInterval.overlap(callInterval);
		    if (overlap != null) {
		        combinedPeakDuration = combinedPeakDuration.plus(overlap.toDuration());
		    }
        }

		Duration combinedOffPeakDuration = callInterval.toDuration().minus(combinedPeakDuration);

		BigDecimal peakCost    = new BigDecimal(combinedPeakDuration.getStandardSeconds())
		                                 .multiply(tariff.peakRate());
		BigDecimal offPeakCost = new BigDecimal(combinedOffPeakDuration.getStandardSeconds())
		                                 .multiply(tariff.offPeakRate());

		return peakCost.add(offPeakCost).setScale(0, RoundingMode.HALF_UP);
	}

}
