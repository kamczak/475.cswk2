package com.acmetelecom.peak;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import com.acmetelecom.strategy.PeakPeriod;

public class DailyPeakPeriod implements PeakPeriod {
    private LocalTime start;
    private LocalTime end;
    private Period period;

    public DailyPeakPeriod(LocalTime start, LocalTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end cannot come before start!");
        }

        this.start = start;
        this.end = end;
        this.period = new Period(start, end);
    }
    
    /* (non-Javadoc)
     * @see com.acmetelecom.strategy.IPeakPeriod#getOverlappingIntervals(org.joda.time.Interval)
     */
    @Override
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
    
    public List<Interval> getRelevantPeakIntervals(Interval timeInterval) {
        List<Interval> relevantPeakIntervals = new LinkedList<Interval>();

        DateTime currentStart = timeInterval.getStart();
        DateTime calculationEnd = timeInterval.getEnd().plusDays(1);
        DateTime intervalEnd;
        while (currentStart.isBefore(calculationEnd)) {
            // peak interval of the length as this.period ending on the same day
            // as the currently being considered.
            intervalEnd = currentStart.withHourOfDay(this.end.getHourOfDay())
                                      .withMinuteOfHour(this.end.getMinuteOfHour())
                                      .withSecondOfMinute(this.end.getSecondOfMinute())
                                      .withMillisOfSecond(this.end.getMillisOfSecond());
            relevantPeakIntervals.add(new Interval(this.period, intervalEnd));

            currentStart = currentStart.plusDays(1);
        }
        
        return relevantPeakIntervals;
    }
}
