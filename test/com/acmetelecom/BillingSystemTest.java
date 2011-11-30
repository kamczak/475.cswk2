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
import com.acmetelecom.call.CallLog;
import com.acmetelecom.call.CustomerCallLog;
import com.acmetelecom.customer.Customer;
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
    BillGenerator    billGenerator = context.mock(BillGenerator.class);
    TariffLibrary    tariffLibrary = context.mock(TariffLibrary.class);
    CustomerDatabase customerDB    = context.mock(CustomerDatabase.class);
    Clock            clock         = context.mock(Clock.class);
    
    // real CallLog
    CallLog callLog = new CustomerCallLog(clock);

    // real BillingSystem, initialised with the mocked objects
    BillingSystem billingSystem = new BillingSystem(customerDB, tariffLibrary, callLog, billGenerator);

    @SuppressWarnings("serial")
    private static final List<Customer> ONE_CUSTOMER_LIST = new ArrayList<Customer>(1) {
        {
            add(FIRST_CUSTOMER);
        }
    };

    @SuppressWarnings("serial")
    private static final List<Customer> TWO_CUSTOMERS_LIST = new ArrayList<Customer>(2) {
        {
            add(FIRST_CUSTOMER);
            add(SECOND_CUSTOMER);
        }
    };

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesOffPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = start.plusMinutes(1);

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

        context.checking(new Expectations() {{
            allowing(clock).getCurrentTime();
            will(onConsecutiveCalls(returnValue(start.getMillis()),
                                    returnValue(end.getMillis())));

            allowing(tariffLibrary).tarriffFor(FIRST_CUSTOMER);
            will(returnValue(FIRST_CUSTOMER_TARIFF));

            allowing(customerDB).getCustomers();
            will(returnValue(ONE_CUSTOMER_LIST));

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
