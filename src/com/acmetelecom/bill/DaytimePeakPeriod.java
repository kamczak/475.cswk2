package com.acmetelecom.bill;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Period;

public class DaytimePeakPeriod {

	private LocalTime peakStartTime = new LocalTime(7,0);
	private LocalTime peakEndTime = new LocalTime(19,0);
	
    public List<Interval> getPeakTimeIntervals(Interval callInterval) {
    	DateTime callStart = callInterval.getStart();
		List<Interval> peakIntervals = new LinkedList<Interval>();
		
		for (int i = 0; i <= callInterval.toPeriod().getDays(); i++) {
			peakIntervals.add(new Interval(new Period(peakStartTime, peakEndTime), callStart.withHourOfDay(peakEndTime.getHourOfDay()).plusDays(i)));
		}
		return peakIntervals;
	}

//	public boolean offPeak(Date time) {
//        LocalTime in = LocalTime.fromDateFields(time);
//        return in.isBefore(peakStartTime) || !in.isBefore(peakEndTime);
//    }    
}
