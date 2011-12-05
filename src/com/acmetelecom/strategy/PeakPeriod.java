package com.acmetelecom.strategy;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Period;

public class PeakPeriod {
    public static final PeakPeriod PEAK_7_TO_19 = new PeakPeriod(new LocalTime(7,0), new LocalTime(19,0));

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
        DateTime calculationEnd = timeInterval.getEnd().plusDays(1);
        Interval peakInterval;
        Interval overlap;
        while (currentStart.isBefore(calculationEnd)) {
            // peak interval of the length as this.period ending on the same day
            // as the currently being considered.
            peakInterval = new Interval(this.period, currentStart.withHourOfDay(this.end.getHourOfDay()));
            overlap = peakInterval.overlap(timeInterval);
            if (overlap != null) {
                overlappingIntervals.add(overlap);
            }
            currentStart = currentStart.plusDays(1);
        }
        
        return overlappingIntervals;
    }
}
