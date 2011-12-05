package com.acmetelecom.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.Interval;

import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;
import com.google.inject.Inject;

public class OldStrategy implements Strategy {
    private PeakPeriod peakPeriod;
    
    @Inject
    public OldStrategy(PeakPeriod peakPeriod) {
        this.peakPeriod = peakPeriod;
    }

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		Interval callInterval = new Interval(call.startTime(), call.endTime());
		
		BigDecimal multiplyRate = tariff.offPeakRate();;
		for (Interval peakInterval : peakPeriod.getRelevantPeakIntervals(callInterval)) {
            if (peakInterval.overlaps(callInterval)) {
                multiplyRate = tariff.peakRate();
                break;
            }
        }

		return new BigDecimal(call.getDuration().getStandardSeconds())
		        .multiply(multiplyRate)
		        .setScale(0, RoundingMode.HALF_UP);
	}

}
