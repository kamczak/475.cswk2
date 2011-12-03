package com.acmetelecom.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.acmetelecom.bill.DaytimePeakPeriod;
import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

public class OldStrategy implements Strategy {

	private static Strategy instance = new OldStrategy();
	
	public static Strategy getInstance() {
		return instance;
	}

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		BigDecimal cost;
		DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();
//
//		Interval peakPeriod2 = new DaytimePeakPeriod().getPeakTimePeriod();
//		Interval callPeriod = new Interval(new Period(new LocalTime(call.startTime()), new LocalTime(call.endTime())));
//
//		if (peakPeriod2.overlaps(callPeriod)) {
//			cost = new BigDecimal(call.durationSeconds()).multiply(tariff.offPeakRate());
//		}
		
		if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime())
				&& call.durationSeconds() < 12 * 60 * 60) {
			cost = new BigDecimal(call.durationSeconds()).multiply(tariff.offPeakRate());
		} 
		else {
			cost = new BigDecimal(call.durationSeconds()).multiply(tariff.peakRate());
		}
		
		cost = cost.setScale(0, RoundingMode.HALF_UP);
		return cost;
	}

}
