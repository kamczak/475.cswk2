package com.acmetelecom.strategy;

import java.util.List;

import org.joda.time.Interval;

public interface PeakPeriod {

    public List<Interval> getRelevantPeakIntervals(Interval timeInterval);
}