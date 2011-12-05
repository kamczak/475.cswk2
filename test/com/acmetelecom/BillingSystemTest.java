package com.acmetelecom;

import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER;
import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER_NUMBER;
import static com.acmetelecom.FakeCustomers.FIRST_CUSTOMER_TARIFF;
import static com.acmetelecom.FakeCustomers.ONE_CUSTOMER_LIST;
import static com.acmetelecom.FakeCustomers.OTHER_CUSTOMER_NUMBER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.call.CallLog;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.strategy.Strategy;
import com.acmetelecom.time.Clock;

/**
 * This is a set of regressions tests for the functionality of the BillingSystem.
 */
public class BillingSystemTest {
    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    // mocks
    private BillGenerator    billGenerator = context.mock(BillGenerator.class);
    private TariffLibrary    tariffLibrary = context.mock(TariffLibrary.class);
    private CustomerDatabase customerDB    = context.mock(CustomerDatabase.class);
    private Clock            clock         = context.mock(Clock.class);
    private CallLog          mCallLog      = context.mock(CallLog.class);
    private Strategy		 mStrategy	   = context.mock(Strategy.class);

    // real BillingSystem, initialised with the mocked objects
    BillingSystem billingSystem = new BillingSystem(customerDB, tariffLibrary, mCallLog, mStrategy, billGenerator);

    @SuppressWarnings("unchecked")
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
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(call);
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentDateTime();
                    will(onConsecutiveCalls(returnValue(start),
                                            returnValue(end)
                                            ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));

            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will(returnValue(callList));
                    
            oneOf (mStrategy).getCost(FIRST_CUSTOMER_TARIFF, call);
                    will(returnValue(new BigDecimal(12)));

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aNonNull(List.class)),
                                       with(equal("0.12"))
                                       );
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
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
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(call);
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentDateTime();
                    will(onConsecutiveCalls(returnValue(start),
                                            returnValue(end)
                                            ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));

            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will(returnValue(callList));            
                    
            oneOf (mStrategy).getCost(FIRST_CUSTOMER_TARIFF, call);
                    will(returnValue(new BigDecimal(30)));                    

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aNonNull(List.class)),
                                       with(equal("0.30"))
                                       );
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
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
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(call);
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentDateTime();
                    will(onConsecutiveCalls(returnValue(start),
                                            returnValue(end)
                                            ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));
                    
            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will(returnValue(callList));

            oneOf (mStrategy).getCost(FIRST_CUSTOMER_TARIFF, call);
                    will(returnValue(new BigDecimal(3600)));
                    
            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aNonNull(List.class)),
                                       with(equal("36.00"))
                                       );
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
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
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(call);
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentDateTime();
                    will(onConsecutiveCalls(returnValue(start),
                                            returnValue(end)
                                            ));

            allowing(tariffLibrary).tarriffFor(FIRST_CUSTOMER);
            will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing(customerDB).getCustomers();
            will(returnValue(ONE_CUSTOMER_LIST));
            
            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will(returnValue(callList));
                    
            oneOf (mStrategy).getCost(FIRST_CUSTOMER_TARIFF, call);
                    will(returnValue(new BigDecimal(3600)));
                    
            oneOf(billGenerator).send(with(same(FIRST_CUSTOMER)), with(aNonNull(List.class)),
                    with(equal("36.00")));
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
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
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(call);
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentDateTime();
                    will(onConsecutiveCalls(returnValue(start),
                                            returnValue(end)
                                            ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));
                    
            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will(returnValue(callList));
            
            oneOf (mStrategy).getCost(FIRST_CUSTOMER_TARIFF, call);
                    will(returnValue(new BigDecimal(25200)));        

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aNonNull(List.class)),
                                       with(equal("252.00"))
                                       );
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        
        billingSystem.createCustomerBills();
    }
}
