package com.acmetelecom;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.acmetelecom.strategy.AggresivePeakCharging;

public class OldStrategyTest extends StrategyTestScenarios {

    @Before
    public void initialiseStrategy() {
        mStrategy = new AggresivePeakCharging(peakPeriod);
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
}
