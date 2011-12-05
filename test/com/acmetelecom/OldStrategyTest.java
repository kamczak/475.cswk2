package com.acmetelecom;

import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER_NUMBER;
import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER_TARIFF;
import static com.acmetelecom.FakeCustomers.OTHER_CUSTOMER_NUMBER;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.strategy.OldStrategy;

/**
 * This is a set of regressions tests for the functionality of the BillingSystem.
 */
public class OldStrategyTest {

    private Strategy mStrategy = new OldStrategy();

    @Test
    public void calculatesOffPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = start.plusMinutes(1);
        
        final Call call = new Call(
        		new CallEvent(FIRST_CUSTOMER_NUMBER,
        				OTHER_CUSTOMER_NUMBER,
            			start),
            	new CallEvent(FIRST_CUSTOMER_NUMBER,
                		OTHER_CUSTOMER_NUMBER,
                		end));

        BigDecimal cost = mStrategy.getCost(FIRST_CUSTOMER_TARIFF, call);
        Assert.assertEquals(new BigDecimal(12), cost);
    }

    @Test
    public void calculatesWholeInPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T08:00:00");
        final DateTime end   = start.plusMinutes(1);
        
        final Call call = new Call(
        		new CallEvent(FIRST_CUSTOMER_NUMBER,
        				OTHER_CUSTOMER_NUMBER,
            			start),
            	new CallEvent(FIRST_CUSTOMER_NUMBER,
                		OTHER_CUSTOMER_NUMBER,
                		end));

        BigDecimal cost = mStrategy.getCost(FIRST_CUSTOMER_TARIFF, call);
        Assert.assertEquals(cost, new BigDecimal(30));
    }

    @Test
    public void calculatesOverlappingLeftPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = start.plusHours(2);
        
        final Call call = new Call(
        		new CallEvent(FIRST_CUSTOMER_NUMBER,
        				OTHER_CUSTOMER_NUMBER,
            			start),
            	new CallEvent(FIRST_CUSTOMER_NUMBER,
                		OTHER_CUSTOMER_NUMBER,
                		end));

        BigDecimal cost = mStrategy.getCost(FIRST_CUSTOMER_TARIFF, call);
        Assert.assertEquals(new BigDecimal(3600), cost);
    }

    @Test
    public void calculatesOverlappingRightPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T18:00:00");
        final DateTime end   = start.plusHours(2);
        
        final Call call = new Call(
        		new CallEvent(FIRST_CUSTOMER_NUMBER,
        				OTHER_CUSTOMER_NUMBER,
            			start),
            	new CallEvent(FIRST_CUSTOMER_NUMBER,
                		OTHER_CUSTOMER_NUMBER,
                		end));

        BigDecimal cost = mStrategy.getCost(FIRST_CUSTOMER_TARIFF, call);
        Assert.assertEquals(new BigDecimal(3600), cost);
    }

    @Test
    public void calculatesOverlappingBothPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = new DateTime("2011-01-01T20:00:00");
        
        final Call call = new Call(
        		new CallEvent(FIRST_CUSTOMER_NUMBER,
        				OTHER_CUSTOMER_NUMBER,
            			start),
            	new CallEvent(FIRST_CUSTOMER_NUMBER,
                		OTHER_CUSTOMER_NUMBER,
                		end));

        BigDecimal cost = mStrategy.getCost(FIRST_CUSTOMER_TARIFF, call);
        Assert.assertEquals(new BigDecimal(25200), cost);
    }
}
