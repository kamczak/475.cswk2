package com.acmetelecom;

import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER_NUMBER;
import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER_TARIFF;
import static com.acmetelecom.FakeCustomers.OTHER_CUSTOMER_NUMBER;

import java.math.BigDecimal;

import javax.jws.soap.InitParam;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.peak.DailyPeakPeriod;
import com.acmetelecom.strategy.NewStrategy;

public abstract class StrategyTestBase {

    protected Strategy mStrategy;

    public void calculatesOffPeakCallCorrectly(BigDecimal expected) {
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
        Assert.assertEquals(expected, cost);
    }

    public void calculatesWholeInPeakCallCorrectly(BigDecimal expected) {
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
        Assert.assertEquals(expected, cost);
    }

    public void calculatesOverlappingLeftPeakCallCorrectly(BigDecimal expected) {
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
        Assert.assertEquals(expected, cost);
    }

    public void calculatesOverlappingRightPeakCallCorrectly(BigDecimal expected) {
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
        Assert.assertEquals(expected, cost);
    }

    public void calculatesOverlappingBothPeakCallCorrectly(BigDecimal expected) {
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
        Assert.assertEquals(expected, cost);
    }

}