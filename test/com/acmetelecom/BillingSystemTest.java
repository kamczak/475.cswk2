package com.acmetelecom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.time.Clock;

import static com.acmetelecom.ListSizeMatcher.aListOfSize;

public class BillingSystemTest {
    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    // mocked BillGenerator
    BillGenerator billGenerator = context.mock(BillGenerator.class);

    // mocked Clock, epoch calculator: http://tinyurl.com/u85od
    Clock clock = context.mock(Clock.class);

    // mocked TariffLibrary
    TariffLibrary tariffDB = context.mock(TariffLibrary.class);

    // mocked CustomerDatabase
    CustomerDatabase customerDB = context.mock(CustomerDatabase.class);

    // real BillingSystem, initialised with the mocked objects
    BillingSystem billingSystem = new BillingSystem(customerDB, tariffDB, billGenerator, clock);

    // fake customers
    private static final String FIRST_CUSTOMER_NAME = "Fred Bloggs";
    private static final String FIRST_CUSTOMER_NUMBER = "447711232343";
    private static final Tariff FIRST_CUSTOMER_TARIFF = Tariff.Standard;
    private static final Customer firstCustomer = new Customer(FIRST_CUSTOMER_NAME,
            FIRST_CUSTOMER_NUMBER, FIRST_CUSTOMER_TARIFF.name());

    private static final String SECOND_CUSTOMER_NAME = "Joe Doe";
    private static final String SECOND_CUSTOMER_NUMBER = "447722232355";
    private static final Tariff SECOND_CUSTOMER_TARIFF = Tariff.Leisure;
    private static final Customer secondCustomer = new Customer(SECOND_CUSTOMER_NAME,
            SECOND_CUSTOMER_NUMBER, SECOND_CUSTOMER_TARIFF.name());

    @SuppressWarnings("serial")
    private static final List<Customer> ONE_CUSTOMER_LIST = new ArrayList<Customer>(1) {
        {
            add(firstCustomer);
        }
    };

    @SuppressWarnings("serial")
    private static final List<Customer> TWO_CUSTOMERS_LIST = new ArrayList<Customer>(2) {
        {
            add(firstCustomer);
            add(secondCustomer);
        }
    };

    private static final String OTHER_NUMBER = "447722113434";

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesOffPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = start.plusMinutes(1);

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                        will(onConsecutiveCalls(returnValue(start.getMillis()),
                                                returnValue(end.getMillis())
                                                ));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(ONE_CUSTOMER_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aNonNull(List.class)),
                                           with(equal("0.12"))
                                           );
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesWholeInPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T08:00:00");
        final DateTime end   = start.plusMinutes(1);

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                        will(onConsecutiveCalls(returnValue(start.getMillis()),
                                                returnValue(end.getMillis())
                                               ));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(ONE_CUSTOMER_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aNonNull(List.class)),
                                           with(equal("0.30"))
                                           );
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesOverlappingLeftPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = start.plusHours(2);

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                        will(onConsecutiveCalls(returnValue(start.getMillis()),
                                                returnValue(end.getMillis())
                                                ));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(ONE_CUSTOMER_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aNonNull(List.class)),
                                           with(equal("36.00"))
                                           );
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesOverlappingRightPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T18:00:00");
        final DateTime end   = start.plusHours(2);

        context.checking(new Expectations() {
            {
                allowing(clock).getCurrentTime();
                will(onConsecutiveCalls(returnValue(start.getMillis()),
                                        returnValue(end.getMillis())));

                allowing(tariffDB).tarriffFor(firstCustomer);
                will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing(customerDB).getCustomers();
                will(returnValue(ONE_CUSTOMER_LIST));

                oneOf(billGenerator).send(with(same(firstCustomer)), with(aNonNull(List.class)),
                        with(equal("36.00")));
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesOverlappingBothPeakCallCorrectly() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = new DateTime("2011-01-01T20:00:00");

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                        will(onConsecutiveCalls(returnValue(start.getMillis()),
                                                returnValue(end.getMillis())
                                                ));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(ONE_CUSTOMER_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aNonNull(List.class)),
                                           with(equal("252.00"))
                                           );
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void oneCallIsAddedCorrectlyToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = new DateTime("2011-01-01T20:00:00");

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                        will(onConsecutiveCalls(returnValue(start.getMillis()),
                                                returnValue(end.getMillis())
                                                ));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(ONE_CUSTOMER_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aListOfSize(1)),
                                           with(any(String.class))
                                           );
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void onlyCallStartIsNotAddedToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                        will(returnValue(start.getMillis()));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(ONE_CUSTOMER_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aListOfSize(0)),
                                           with(any(String.class))
                                           );
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void onlyCallEndIsNotAddedToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                        will(returnValue(start.getMillis()));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(ONE_CUSTOMER_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aListOfSize(0)),
                                           with(any(String.class))
                                           );
            }
        });

        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void twoAndHalfCallAreAddedCorrectlyToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = new DateTime("2011-01-01T20:00:00");

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                        will(onConsecutiveCalls(returnValue(start.getMillis()),
                                                returnValue(end.getMillis()),
                                                returnValue(start.getMillis()),
                                                returnValue(end.getMillis()),
                                                returnValue(start.getMillis())));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(ONE_CUSTOMER_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aListOfSize(2)),
                                           with(any(String.class))
                                           );
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void threeCallsTwoCustomersAddedCorrectlyToLog() {
        final DateTime start = new DateTime("2011-01-01T06:00:00");
        final DateTime end   = new DateTime("2011-01-01T20:00:00");

        context.checking(new Expectations() {
            {
                allowing (clock).getCurrentTime();
                will(onConsecutiveCalls(returnValue(start.getMillis()),
                                        returnValue(end.getMillis()),
                                        returnValue(start.getMillis()),
                                        returnValue(end.getMillis()),
                                        returnValue(start.getMillis()),
                                        returnValue(end.getMillis())
                                        ));

                allowing (tariffDB).tarriffFor(firstCustomer);
                        will(returnValue(FIRST_CUSTOMER_TARIFF));
                allowing (tariffDB).tarriffFor(secondCustomer);
                        will(returnValue(SECOND_CUSTOMER_TARIFF));

                allowing (customerDB).getCustomers();
                        will(returnValue(TWO_CUSTOMERS_LIST));

                oneOf (billGenerator).send(with(same(firstCustomer)),
                                           with(aListOfSize(1)),
                                           with(any(String.class)));
                oneOf (billGenerator).send(with(same(secondCustomer)),
                                           with(aListOfSize(2)),
                                           with(any(String.class)));
            }
        });

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.callInitiated(SECOND_CUSTOMER_NUMBER, FIRST_CUSTOMER_NUMBER);
        billingSystem.callCompleted(SECOND_CUSTOMER_NUMBER, FIRST_CUSTOMER_NUMBER);

        billingSystem.callInitiated(SECOND_CUSTOMER_NUMBER, OTHER_NUMBER);
        billingSystem.callCompleted(SECOND_CUSTOMER_NUMBER, OTHER_NUMBER);

        billingSystem.createCustomerBills();
    }

}
