package com.acmetelecom.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.joda.time.Interval;

import com.acmetelecom.bill.DaytimePeakPeriod;
import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

public class OldStrategy implements Strategy {

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		BigDecimal cost = null;

		Interval callInterval = new Interval(call.startTime(), call.endTime());
		List<Interval> peakIntervals = new DaytimePeakPeriod().getPeakTimeIntervals(callInterval);

		for (Interval peakTime : peakIntervals) {
			if (callInterval.overlaps(peakTime)) {
				cost = new BigDecimal(call.durationSeconds()).multiply(tariff.peakRate());
				break;
			}
		}
		if (cost == null) {
			cost = new BigDecimal(call.durationSeconds()).multiply(tariff.offPeakRate());
		}
		
		cost = cost.setScale(0, RoundingMode.HALF_UP);
		return cost;
	}

}
