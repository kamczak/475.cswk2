package com.acmetelecom;

import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER_NUMBER;
import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER_TARIFF;
import static com.acmetelecom.FakeCustomers.OTHER_CUSTOMER_NUMBER;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Rule;

import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.strategy.PeakPeriod;

/**
 * Acts as a base class and implements the actual logic of all the tests for strategies.
 * As the test scenarios are the same for all the strategies, and only the expected
 * answers differ, so we abstract this logic out in this class.
 */
public abstract class StrategyTestScenarios {
    private static final DateTime FIRST_INTERVAL_START = new DateTime("2011-01-01T07:00:00");
    private static final DateTime FIRST_INTERVAL_END   = new DateTime("2011-01-01T19:00:00");

    @SuppressWarnings("serial")
    private static final List<Interval> FAKE_PEAK_INTERVALS = new LinkedList<Interval>() {{
        int NO_OF_INTERVALS = 10;
        for(int i = 0; i < NO_OF_INTERVALS; i++) {
            add(new Interval(FIRST_INTERVAL_START.plusDays(i), FIRST_INTERVAL_END.plusDays(i)));
        }
    }};
    
    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();
    
    PeakPeriod peakPeriod = context.mock(PeakPeriod.class);

    protected Strategy mStrategy;
    
    private void executeAndTestOneCall(DateTime start, Duration duration, BigDecimal expectedCost) {
        final DateTime end   = start.plus(duration);
        
        final Call call = new Call(
                new CallEvent(FIRST_CUSTOMER_NUMBER,
                        OTHER_CUSTOMER_NUMBER,
                        start),
                new CallEvent(FIRST_CUSTOMER_NUMBER,
                        OTHER_CUSTOMER_NUMBER,
                        end));
        
        final Interval callInterval = new Interval(call.getStartDateTime(), call.getEndDateTime());
        
        context.checking(new Expectations() {{
            oneOf (peakPeriod).getRelevantPeakIntervals(callInterval);
                    will(returnValue(FAKE_PEAK_INTERVALS));
        }});
    
        BigDecimal actualCost = mStrategy.getCost(FIRST_CUSTOMER_TARIFF, call);
        Assert.assertEquals(expectedCost, actualCost);
    }

    protected void calculatesOffPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T06:00:00"), Duration.standardMinutes(1), expected);
    }

    protected void calculatesWholeInPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T08:00:00"), Duration.standardMinutes(1), expected);
    }

    protected void calculatesOverlappingLeftPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T06:00:00"), Duration.standardHours(2), expected);
    }

    protected void calculatesOverlappingRightPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T18:00:00"), Duration.standardHours(2), expected);
    }

    protected void calculatesOverlappingBothPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T06:00:00"), Duration.standardHours(14), expected);
    }
    
    protected void calculatesOverlappingTwoPeaksCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T17:00:00"), Duration.standardHours(16), expected);
    }

}