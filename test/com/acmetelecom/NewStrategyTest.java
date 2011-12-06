package com.acmetelecom;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.acmetelecom.strategy.PercentagePeakCharging;

public class NewStrategyTest extends StrategyTestScenarios {

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
        calculatesOverlappingLeftPeakCallCorrectly(new BigDecimal(2520));
    }

    @Test
    public void calculatesOverlappingRightPeakCallCorrectly() {
        calculatesOverlappingRightPeakCallCorrectly(new BigDecimal(2520));
    }

    @Test
    public void calculatesOverlappingBothPeakCallCorrectly() {
        calculatesOverlappingBothPeakCallCorrectly(new BigDecimal(23040));
    }
    
    @Test
    public void calculatesOverlappingTwoPeaksCallCorrectly() {
        calculatesOverlappingTwoPeaksCallCorrectly(new BigDecimal(15840));
    }
}
