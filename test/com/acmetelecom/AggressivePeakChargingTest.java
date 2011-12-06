package com.acmetelecom;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.acmetelecom.strategy.AggressivePeakCharging;

public class AggressivePeakChargingTest extends ChargingStrategyTestScenarios {

    @Before
    public void initialiseStrategy() {
        mStrategy = new AggressivePeakCharging(peakPeriod);
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
        calculatesOverlappingLeftPeakCallCorrectly(new BigDecimal(3600));
    }

    @Test
    public void calculatesOverlappingRightPeakCallCorrectly() {
        calculatesOverlappingRightPeakCallCorrectly(new BigDecimal(3600));
    }

    @Test
    public void calculatesOverlappingBothPeakCallCorrectly() {
        calculatesOverlappingBothPeakCallCorrectly(new BigDecimal(25200));
    }
    
    @Test
    public void calculatesOverlappingTwoPeaksCallCorrectly() {
        calculatesOverlappingTwoPeaksCallCorrectly(new BigDecimal(28800));
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
