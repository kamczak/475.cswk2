package com.acmetelecom.bill;

import static com.acmetelecom.common.FakeCustomers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.call.CallLog;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.strategy.ChargingStrategy;

/**
 * This is a set of integration tests for the functionality of the
 * BillingSystem.
 */
@SuppressWarnings("unchecked")
public class BillingSystemTest {
    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();

    // mocks
    private BillGenerator billGenerator = context.mock(BillGenerator.class);
    private TariffLibrary tariffLibrary = context.mock(TariffLibrary.class);
    private CustomerDatabase customerDB = context.mock(CustomerDatabase.class);
    private CallLog callLog = context.mock(CallLog.class);
    private ChargingStrategy strategy = context.mock(ChargingStrategy.class);

    // real BillingSystem, initialised with the mocked objects
    BillingSystem billingSystem
            = new BillingSystem(customerDB, tariffLibrary, callLog, strategy, billGenerator);

    @Test
    public void callInitiatedCallsCallLog() {
        context.checking(new Expectations() {{
            oneOf (callLog).callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        }});

        billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
    }

    @Test
    public void callEndedCallsCallLog() {
        context.checking(new Expectations() {{
            oneOf (callLog).callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
        }});

        billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
    }

    @Test
    public void noBillGeneratedForNoCustomers() {
        final List<Customer> fakeCustomers = new ArrayList<Customer>();

        context.checking(new Expectations() {{
            oneOf (customerDB).getCustomers();
                    will(returnValue(fakeCustomers));

            never (billGenerator).generateBill(with(any(Customer.class)),
                                               with(any(List.class)),
                                               with(any(String.class)));
        }});

        billingSystem.createCustomerBills();
    }

    @Test
    public void oneCustomerGeneratesOneBill() {
        final List<Customer> fakeCustomers = new ArrayList<Customer>();
        fakeCustomers.add(FIRST_CUSTOMER);

        context.checking(new Expectations() {{
            oneOf (customerDB).getCustomers();
                    will(returnValue(fakeCustomers));

            oneOf (callLog).getCalls(FIRST_CUSTOMER);

            oneOf (billGenerator).generateBill(with(FIRST_CUSTOMER),
                                               with(any(List.class)),
                                               with(any(String.class)));
        }});

        billingSystem.createCustomerBills();
    }
    
    @Test
    public void threeCustomersGenerateThreeBills() {
        final List<Customer> fakeCustomers = new ArrayList<Customer>();
        fakeCustomers.add(FIRST_CUSTOMER);
        fakeCustomers.add(SECOND_CUSTOMER);
        fakeCustomers.add(OTHER_CUSTOMER);

        context.checking(new Expectations() {{
            oneOf (customerDB).getCustomers();
                    will(returnValue(fakeCustomers));

            allowing (callLog).getCalls(with(any(Customer.class)));

            exactly(3).of (billGenerator).generateBill(with(any(Customer.class)),
                                                       with(any(List.class)),
                                                       with(any(String.class)));
        }});

        billingSystem.createCustomerBills();
    }

    @Test
    public void billingSystemCallsAllTheComponents() {
        final List<Customer> fakeCustomers = new ArrayList<Customer>();
        fakeCustomers.add(FIRST_CUSTOMER);

        final List<Call> fakeCalls = new ArrayList<Call>();
        final Call fakeCall = new Call(new CallEvent(null, null, null),
                                       new CallEvent(null, null, null));
        fakeCalls.add(fakeCall);

        context.checking(new Expectations() {{
            oneOf (customerDB).getCustomers();
                    will(returnValue(fakeCustomers));

            oneOf (callLog).getCalls(FIRST_CUSTOMER);
                    will(returnValue(fakeCalls));

            allowing (billGenerator).generateBill(with(any(Customer.class)),
                                                  with(any(List.class)),
                                                  with(any(String.class))
                                                  );

            oneOf(tariffLibrary).tarriffFor(FIRST_CUSTOMER);
                    will(returnValue(Tariff.Business));

            oneOf(strategy).getCost(Tariff.Business, fakeCall);
                    will(returnValue(BigDecimal.ZERO));
        }});

        billingSystem.createCustomerBills();
    }

}
