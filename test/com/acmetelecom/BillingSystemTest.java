package com.acmetelecom;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.time.Clock;

public class BillingSystemTest
{
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	// mocked BillGenerator
	BillGenerator billGenerator = context.mock(BillGenerator.class);
	// mocked Clock, epoch calculator: http://tinyurl.com/u85od
	Clock clock = context.mock(Clock.class);
	
	// real BillingSystem, initialise with the mocked BillGenerator
	BillingSystem billingSystem = new BillingSystem(billGenerator, clock);
	
	// own customer list, instead of calling CentralCustomerDatabase
	@SuppressWarnings("serial")
	List<Customer> customers = new ArrayList<Customer>(1)
	{{
		add(new Customer("Fred Bloggs", "447711232343", Tariff.Standard.name()));
	}};
	
	@Test
	public void calculatesPeakCallCorrectly()
	{
		context.checking(new Expectations()
		{{
			atLeast(2).of (clock).getCurrentTime();
			   will(onConsecutiveCalls(
			       returnValue(1322326800000L),		// 26 Nov 2011 17:00 = 1322326800
			       returnValue(1322326800000L)));	// 26 Nov 2011 17:01 = 1322326860
		}});
		
		billingSystem.callInitiated("447711232343", "447722113434");
		billingSystem.callCompleted("447711232343", "447722113434");
		
		billingSystem.createCustomerBills(customers);
	}

}
