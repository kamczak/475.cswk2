package com.acmetelecom.strategy;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Period;

public class PeakPeriod {
    private static final PeakPeriod PEAK_7_TO_19 = new PeakPeriod(new LocalTime(7,0), new LocalTime(19,0));
    private LocalTime start;
    private LocalTime end;
    private Period period;

    public PeakPeriod(LocalTime start, LocalTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end cannot come before start!");
        }

        this.start = start;
        this.end = end;
        this.period = new Period(start, end);
    }
    
    public List<Interval> getOverlappingIntervals(Interval timeInterval) {
        List<Interval> overlappingIntervals = new LinkedList<Interval>();
        
        /**
         * Overlap each relevant peak interval with the supplied timeInterval to
         * get the list of slices of timeInterval that fall into peak period.
         * We consider one more day to ensure that we don't terminate too early.
         */ 
        DateTime currentStart = timeInterval.getStart();
        Interval peakInterval;
        while (currentStart.isBefore(timeInterval.getEnd().plusDays(1))) {
            // peak interval of the length as this.period ending on the same day
            // as the currently being considered.
            peakInterval = new Interval(this.period, currentStart.withHourOfDay(this.end.getHourOfDay()));
            overlappingIntervals.add(peakInterval.overlap(timeInterval));
            currentStart.plusDays(1);
        }
        
        return overlappingIntervals;
    }
}
