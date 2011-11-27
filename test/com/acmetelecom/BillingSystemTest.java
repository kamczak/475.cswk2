package com.acmetelecom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.time.Clock;

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
	    						       FIRST_CUSTOMER_NUMBER,
	    						       FIRST_CUSTOMER_TARIFF.name());
    
    @SuppressWarnings("serial")
    private static final List<Customer> CUSTOMERS = new ArrayList<Customer>(1) {{
	add(firstCustomer);
    }};

    private static final String OTHER_NUMBER = "447722113434";

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesOffPeakCallCorrectly() {
	final Calendar start = Calendar.getInstance();
	start.clear();
	start.set(2011, 0, 1, 6, 0, 0);

	final Calendar end = Calendar.getInstance();
	end.clear();
	end.set(2011, 0, 1, 6, 1, 0);

	context.checking(new Expectations() {{
	    allowing (clock).getCurrentTime();
	    		will(onConsecutiveCalls(returnValue(start.getTimeInMillis()),
		    				returnValue(end.getTimeInMillis())
		    				));

	    allowing (tariffDB).tarriffFor(firstCustomer); will(returnValue(Tariff.Standard));
	    
	    allowing (customerDB).getCustomers(); will(returnValue(CUSTOMERS));

	    oneOf (billGenerator).send(with(same(firstCustomer)),
		    		       with(aNonNull(List.class)),
		    		       with(equal("0.12")));
	}});

	billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
	billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

	billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesPeakCallCorrectly() {
	final Calendar start = Calendar.getInstance();
	start.clear();
	start.set(2011, 0, 1, 8, 0, 0);

	final Calendar end = Calendar.getInstance();
	end.clear();
	end.set(2011, 0, 1, 8, 1, 0);

	context.checking(new Expectations() {{
	    allowing (clock).getCurrentTime();
	    		will(onConsecutiveCalls(returnValue(start.getTimeInMillis()),
		    				returnValue(end.getTimeInMillis())
		    				));

	    allowing (tariffDB).tarriffFor(firstCustomer); will(returnValue(Tariff.Standard));
	    
	    allowing (customerDB).getCustomers(); will(returnValue(CUSTOMERS));

	    oneOf (billGenerator).send(with(same(firstCustomer)),
		    		       with(aNonNull(List.class)),
		    		       with(equal("0.30")));
	}});

	billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
	billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

	billingSystem.createCustomerBills();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void calculatesOverlappingPeakCallCorrectly() {
	final Calendar start1 = Calendar.getInstance();
	start1.clear();
	start1.set(2011, 0, 1, 6, 0, 0);

	final Calendar end1 = Calendar.getInstance();
	end1.clear();
	end1.set(2011, 0, 1, 8, 0, 0);

	final Calendar start2 = Calendar.getInstance();
	start2.clear();
	start2.set(2011, 0, 1, 18, 0, 0);

	final Calendar end2 = Calendar.getInstance();
	end2.clear();
	end2.set(2011, 0, 1, 20, 0, 0);

	context.checking(new Expectations() {{
	    allowing (clock).getCurrentTime();
	    		will(onConsecutiveCalls(returnValue(start1.getTimeInMillis()),
		    				returnValue(end1.getTimeInMillis()),
		    				returnValue(start2.getTimeInMillis()),
		    				returnValue(end2.getTimeInMillis())
		    				));

	    allowing (tariffDB).tarriffFor(firstCustomer); will(returnValue(Tariff.Standard));
	    
	    allowing (customerDB).getCustomers(); will(returnValue(CUSTOMERS));
	    
	    oneOf (billGenerator).send(with(same(firstCustomer)),
		    		       with(aNonNull(List.class)),
		    		       with(equal("72.00")));
	}});

	billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
	billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

	billingSystem.callInitiated(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);
	billingSystem.callCompleted(FIRST_CUSTOMER_NUMBER, OTHER_NUMBER);

	billingSystem.createCustomerBills();
    }

}
