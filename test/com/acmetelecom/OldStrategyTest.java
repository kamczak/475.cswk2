package com.acmetelecom;

import java.math.BigDecimal;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import com.acmetelecom.peak.DailyPeakPeriod;
import com.acmetelecom.strategy.OldStrategy;

/**
 * This is a set of regressions tests for the functionality of the BillingSystem.
 */
public class OldStrategyTest extends StrategyTestBase {

    @Before
    public void initialiseStrategy() {
        // Lazy initialization
        if (mStrategy == null) {
            mStrategy = new OldStrategy(new DailyPeakPeriod(new LocalTime(7, 0), new LocalTime(19, 0)));
        }
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
}

