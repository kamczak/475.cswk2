package com.acmetelecom.bill;

import java.util.Date;

import org.joda.time.LocalTime;
import org.joda.time.Period;

public class DaytimePeakPeriod {

	private LocalTime peakStartTime = new LocalTime(7,0);
	private LocalTime peakEndTime = new LocalTime(19,0);
	
    public Period getPeakTimeInterval() {
		return new Period(peakStartTime, peakEndTime);
	}

	public boolean offPeak(Date time) {
        LocalTime in = LocalTime.fromDateFields(time);
        return in.isBefore(peakStartTime) || !in.isBefore(peakEndTime);
    }    
}
