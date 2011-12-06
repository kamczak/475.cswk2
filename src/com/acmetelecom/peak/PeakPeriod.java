package com.acmetelecom.peak;

import java.util.List;

import org.joda.time.Interval;

/**
 * A interface for classes describing peak periods
 */
public interface PeakPeriod {

    /**
     * Returns a list of the peak periods as concrete intervals on the timeline.
     * 
     * The method guarantees to return a list that contains at least the intervals
     * that overlap the supplied |timeInterval|. However, there is no guarantee that
     * it will not contain more intervals than that.
     * 
     * @param timeInterval
     *          an Interval for which we are interested in peak periods
     *          overlapping it
     * @return
     *          A list of the peak periods as concrete intervals of the time continuum
     *          that are relevant to the supplied |timeInterval|
     */
    public List<Interval> getRelevantPeakIntervals(Interval timeInterval);
}