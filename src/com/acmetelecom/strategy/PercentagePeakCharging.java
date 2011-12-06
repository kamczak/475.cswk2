package com.acmetelecom.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.Duration;
import org.joda.time.Interval;

import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.peak.PeakPeriod;
import com.google.inject.Inject;

/**
 * Charges the call at a peak rate only for the part of it that happened
 * during the peak period. The rest is charged at off-peak rate.
 */
public class PercentagePeakCharging implements ChargingStrategy {
    private PeakPeriod peakPeriod;

    @Inject
    public PercentagePeakCharging(PeakPeriod peakPeriod) {
        this.peakPeriod = peakPeriod;
    }

    @Override
    public BigDecimal getCost(Tariff tariff, Call call) {
        Interval callInterval = new Interval(call.getStartDateTime(), call.getEndDateTime());

        // Calculate the combined duration of the parts of the call that
        // happened during the peak period
        Duration combinedPeakDuration = Duration.ZERO;
        Interval overlap;
        for (Interval peakInterval : peakPeriod.getRelevantPeakIntervals(callInterval)) {
            overlap = peakInterval.overlap(callInterval);
            if (overlap != null) {
                combinedPeakDuration = combinedPeakDuration.plus(overlap.toDuration());
            }
        }

        Duration combinedOffPeakDuration = callInterval.toDuration().minus(combinedPeakDuration);

        BigDecimal peakCost    = new BigDecimal(combinedPeakDuration.getStandardSeconds())
                                    .multiply(tariff.peakRate());
        BigDecimal offPeakCost = new BigDecimal(combinedOffPeakDuration.getStandardSeconds())
                                    .multiply(tariff.offPeakRate());

        return peakCost.add(offPeakCost).setScale(0, RoundingMode.HALF_UP);
    }

}
