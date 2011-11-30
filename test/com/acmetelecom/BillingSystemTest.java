package com.acmetelecom;

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
import com.acmetelecom.time.Clock;

import static com.acmetelecom.ListSizeMatcher.aListOfSize;
import static com.acmetelecom.FakeCustomers.*;

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

    // real BillingSystem, initialised with the mocked objects
    BillingSystem billingSystem = new BillingSystem(customerDB, tariffLibrary, mCallLog, billGenerator);

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesOffPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = start.plusMinutes(1);
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(new Call(new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           start.getMillis()),
                             new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           end.getMillis())));
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
                    will(onConsecutiveCalls(returnValue(start.getMillis()),
                                            returnValue(end.getMillis())
                                            ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));

            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will (returnValue(callList));

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

        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(new Call(new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           start.getMillis()),
                             new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           end.getMillis())));
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
                    will(onConsecutiveCalls(returnValue(start.getMillis()),
                                            returnValue(end.getMillis())
                                           ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));

            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will (returnValue(callList));            

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
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(new Call(new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           start.getMillis()),
                             new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           end.getMillis())));
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
                    will(onConsecutiveCalls(returnValue(start.getMillis()),
                                            returnValue(end.getMillis())
                                            ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));
                    
            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will (returnValue(callList));

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
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(new Call(new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           start.getMillis()),
                             new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           end.getMillis())));
            }
        };

        context.checking(new Expectations() {{
            allowing(clock).getCurrentTime();
            will(onConsecutiveCalls(returnValue(start.getMillis()),
                                    returnValue(end.getMillis())));

            allowing(tariffLibrary).tarriffFor(FIRST_CUSTOMER);
            will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing(customerDB).getCustomers();
            will(returnValue(ONE_CUSTOMER_LIST));
            
            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will (returnValue(callList));

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
        
        final List<Call> callList = new ArrayList<Call>(1) {
            {
                add(new Call(new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           start.getMillis()),
                             new CallEvent(FIRST_CUSTOMER_NUMBER,
                                           OTHER_CUSTOMER_NUMBER,
                                           end.getMillis())));
            }
        };

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
                    will(onConsecutiveCalls(returnValue(start.getMillis()),
                                            returnValue(end.getMillis())
                                            ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));
                    
            oneOf (mCallLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
            oneOf (mCallLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

            oneOf (mCallLog).getCalls(FIRST_CUSTOMER);
                    will (returnValue(callList));

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aNonNull(List.class)),
                                       with(equal("252.00"))
                                       );
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void oneCallIsAddedCorrectlyToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = new DateTime("2011-01-01T20:00:00");

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
                    will(onConsecutiveCalls(returnValue(start.getMillis()),
                                            returnValue(end.getMillis())
                                            ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aListOfSize(1)),
                                       with(any(String.class))
                                       );
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void onlyCallStartIsNotAddedToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
                    will(returnValue(start.getMillis()));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aListOfSize(0)),
                                       with(any(String.class))
                                       );
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void onlyCallEndIsNotAddedToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
                    will(returnValue(start.getMillis()));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aListOfSize(0)),
                                       with(any(String.class))
                                       );
        }});

        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void twoAndHalfCallAreAddedCorrectlyToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = new DateTime("2011-01-01T20:00:00");

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
                    will(onConsecutiveCalls(returnValue(start.getMillis()),
                                            returnValue(end.getMillis()),
                                            returnValue(start.getMillis()),
                                            returnValue(end.getMillis()),
                                            returnValue(start.getMillis())));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(ONE_CUSTOMER_LIST));

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aListOfSize(2)),
                                       with(any(String.class))
                                       );
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void threeCallsTwoCustomersAddedCorrectlyToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = new DateTime("2011-01-01T20:00:00");

        context.checking(new Expectations() {{
            allowing (clock).getCurrentTime();
            will(onConsecutiveCalls(returnValue(start.getMillis()),
                                    returnValue(end.getMillis()),
                                    returnValue(start.getMillis()),
                                    returnValue(end.getMillis()),
                                    returnValue(start.getMillis()),
                                    returnValue(end.getMillis())
                                    ));

            allowing (tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(FIRST_CUSTOMER_TARIFF));
            allowing (tariffLibrary).tarriffFor(SECOND_CUSTOMER);
                    will(returnValue(SECOND_CUSTOMER_TARIFF));

            allowing (customerDB).getCustomers();
                    will(returnValue(TWO_CUSTOMERS_LIST));

            oneOf (billGenerator).send(with(same(FIRST_CUSTOMER)),
                                       with(aListOfSize(1)),
                                       with(any(String.class)));
            oneOf (billGenerator).send(with(same(SECOND_CUSTOMER)),
                                       with(aListOfSize(2)),
                                       with(any(String.class)));
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.callInitiated(SECOND_CUSTOMER_NUMBER, FIRST_CUSTOMER_NUMBER);
        billingSystem.callCompleted(SECOND_CUSTOMER_NUMBER, FIRST_CUSTOMER_NUMBER);

        billingSystem.callInitiated(SECOND_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        billingSystem.callCompleted(SECOND_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);

        billingSystem.createCustomerBills();
    }

}
