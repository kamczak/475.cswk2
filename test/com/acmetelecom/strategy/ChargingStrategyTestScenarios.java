package com.acmetelecom.strategy;

import static com.acmetelecom.common.FakeCustomers.FIRST_CUSTOMER_NUMBER;
import static com.acmetelecom.common.FakeCustomers.FIRST_CUSTOMER_TARIFF;
import static com.acmetelecom.common.FakeCustomers.OTHER_CUSTOMER_NUMBER;

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

import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.peak.PeakPeriod;
import com.acmetelecom.strategy.ChargingStrategy;

/**
 * Acts as a base class and implements the actual logic of all the tests for
 * strategies. As the test scenarios are the same for all the strategies (only
 * the expected answers differ) we abstract this logic out in this class.
 */
public abstract class ChargingStrategyTestScenarios {
    private static final DateTime FIRST_INTERVAL_START = new DateTime("2011-01-01T07:00:00");
    private static final DateTime FIRST_INTERVAL_END = new DateTime("2011-01-01T19:00:00");

    @SuppressWarnings("serial")
    private static final List<Interval> FAKE_PEAK_INTERVALS = new LinkedList<Interval>() {
        {
            int NO_OF_INTERVALS = 10;
            for (int i = 0; i < NO_OF_INTERVALS; i++) {
                add(new Interval(FIRST_INTERVAL_START.plusDays(i), FIRST_INTERVAL_END.plusDays(i)));
            }
        }
    };

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    PeakPeriod peakPeriod = context.mock(PeakPeriod.class);

    protected ChargingStrategy mStrategy;

    private void executeAndTestOneCall(DateTime start, Duration duration, BigDecimal expectedCost) {
        final DateTime end = start.plus(duration);
        executeAndTestOneCall(start, end, expectedCost);
    }

    private void executeAndTestOneCall(DateTime start, DateTime end, BigDecimal expectedCost) {
        final Call call = new Call(
                new CallEvent(FIRST_CUSTOMER_NUMBER,
                        OTHER_CUSTOMER_NUMBER,
                        start),
                new CallEvent(FIRST_CUSTOMER_NUMBER,
                        OTHER_CUSTOMER_NUMBER,
                        end));

        final Interval callInterval = new Interval(call.getStartDateTime(), call.getEndDateTime());

        context.checking(new Expectations() {
            {
                oneOf(peakPeriod).getRelevantPeakIntervals(callInterval);
                will(returnValue(FAKE_PEAK_INTERVALS));
            }
        });

        BigDecimal actualCost = mStrategy.getCost(FIRST_CUSTOMER_TARIFF, call);
        Assert.assertEquals(expectedCost, actualCost);
    }

    protected void calculatesOffPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T06:33:27"), Duration.standardMinutes(1),
                expected);
    }

    protected void calculatesWholeInPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T08:34:21"), Duration.standardMinutes(1),
                expected);
    }

    protected void calculatesOverlappingLeftPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T06:07:20"), Duration.standardHours(2),
                expected);
    }

    protected void calculatesOverlappingRightPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T18:11:01"), Duration.standardHours(2),
                expected);
    }

    protected void calculatesOverlappingBothPeakCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T06:03:50"), Duration.standardHours(14),
                expected);
    }

    protected void calculatesOverlappingTwoPeaksCallCorrectly(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-01-01T17:12:05"), Duration.standardHours(16),
                expected);
    }

    protected void calculatesOffPeakDuringForwardTimeChange(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-03-27T00:53:23Z"), // GMT
                new DateTime("2011-03-27T02:13:23+01"), // BST
                expected);
    }

    protected void calculatesOffPeakDuringBackwardTimeChange(BigDecimal expected) {
        executeAndTestOneCall(new DateTime("2011-10-30T01:53:23+01"), // BST
                new DateTime("2011-10-30T01:13:23"), // GMT
                expected);
    }

}