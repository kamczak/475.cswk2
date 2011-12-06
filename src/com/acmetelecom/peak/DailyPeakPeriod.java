package com.acmetelecom.peak;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Period;


public class DailyPeakPeriod implements PeakPeriod {
    private LocalTime end;
    private Period period;

    public DailyPeakPeriod(LocalTime start, LocalTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End cannot come before start!");
        }

        this.end = end;
        this.period = new Period(start, end);
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
