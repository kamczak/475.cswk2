package com.acmetelecom.peak;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.Period;

/**
 * PeakPeriods happening once per day, between fixed hours.
 */
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

    // TODO: Currently this method would not work properly if the DailyPeakPeriod
    //       would be initialised with the hours overlapping hours affected by
    //       the time change related to Daylight Saving Time.
    public List<Interval> getRelevantPeakIntervals(Interval timeInterval) {
        List<Interval> relevantPeakIntervals = new LinkedList<Interval>();

        DateTime currentStart = timeInterval.getStart();
        DateTime calculationEnd = timeInterval.getEnd().plusDays(1);
        DateTime intervalEnd;
        while (currentStart.isBefore(calculationEnd)) {
            // Peak interval of the length as |this.period| ending on the same day
            // as the one currently being considered.
            intervalEnd = currentStart.withTime(this.end.getHourOfDay(),
                                                this.end.getMinuteOfHour(),
                                                this.end.getSecondOfMinute(),
                                                this.end.getMillisOfSecond());
            relevantPeakIntervals.add(new Interval(this.period, intervalEnd));

            currentStart = currentStart.plusDays(1);
        }
        
        return relevantPeakIntervals;
    }
}
