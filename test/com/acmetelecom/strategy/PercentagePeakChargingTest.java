package com.acmetelecom.strategy;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.acmetelecom.strategy.PercentagePeakCharging;

public class PercentagePeakChargingTest extends ChargingStrategyTestScenarios {

    @Before
    public void initialiseStrategy() {
        mStrategy = new PercentagePeakCharging(peakPeriod);
    }

    @Test
    public void calculatesOffPeakCallCorrectly() {
        calculatesOffPeakCallCorrectly(new BigDecimal(12));
    }

    @Test
    public void calculatesWholeInPeakCallCorrectly() {
        calculatesWholeInPeakCallCorrectly(new BigDecimal(30));
    }

    @Test
    public void calculatesOverlappingLeftPeakCallCorrectly() {
        calculatesOverlappingLeftPeakCallCorrectly(new BigDecimal(2652));
    }

    @Test
    public void calculatesOverlappingRightPeakCallCorrectly() {
        calculatesOverlappingRightPeakCallCorrectly(new BigDecimal(2322));
    }

    @Test
    public void calculatesOverlappingBothPeakCallCorrectly() {
        calculatesOverlappingBothPeakCallCorrectly(new BigDecimal(23040));
    }
    
    @Test
    public void calculatesOverlappingTwoPeaksCallCorrectly() {
        calculatesOverlappingTwoPeaksCallCorrectly(new BigDecimal(15840));
    }
    
    @Test
    public void calculatesOffPeakDuringForwardTimeChange() {
        calculatesOffPeakDuringForwardTimeChange(new BigDecimal(240));
    }
    
    @Test
    public void calculatesOffPeakDuringBackwardTimeChange() {
        calculatesOffPeakDuringBackwardTimeChange(new BigDecimal(240));
    }
}
