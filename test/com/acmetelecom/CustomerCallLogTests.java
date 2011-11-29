package com.acmetelecom;

import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.acmetelecom.call.Call;
import com.acmetelecom.call.CustomerCallLog;
import com.acmetelecom.call.UnexpectedCallException;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.time.Clock;

public class CustomerCallLogTests {
	@Rule
	public final JUnitRuleMockery m = new JUnitRuleMockery();
	private final Customer cust1 = new Customer("Customer1", "111111",
			Tariff.Standard.name());
	private final Customer cust2 = new Customer("Customer2", "222222",
			Tariff.Business.name());
	private final Customer cust3 = new Customer("Customer3", "333333",
			Tariff.Business.name());

	private Clock clock = m.mock(Clock.class);
	private CustomerCallLog log = new CustomerCallLog(clock);

	@Test
	public void CallInitiatedCallsOnSystemClock() {
		// Arrange
		setUpClock(new long[] { 1322326800000L });
		
		// Act
		log.callInitiated(cust1, cust2);
		
		// Assert
		m.assertIsSatisfied();
	}

	@Test
	public void CallEndedCallsOnSystemClock() {
		// Arrange
		setUpClock(new long[] { 1322326800000L, 1322326800000L });
		log.callInitiated(cust1, cust2);
		
		// Act
		log.callCompleted(cust1, cust2);
		
		// Assert
		m.assertIsSatisfied();
	}

	@Test
	public void TwoCallsInitiatedForSameCustomerThrowsException() {
		// Arrange
		setUpClock(new long[] { 1322326800000L });
		;
		log.callInitiated(cust1, cust2);
		UnexpectedCallException exception = null;
		
		// Act
		try {
			log.callInitiated(cust1, cust2);
		} catch (UnexpectedCallException e) {
			exception = e;
		}
		
		// Assert
		Assert.assertNotNull(exception);
		Assert.assertEquals(exception.getMessage(),
				"Call initiated twice for same number.");
	}

	@Test
	public void EndedCallEventWithouthCallInitializedThrowsException() {
		// Arrange
		UnexpectedCallException exception = null;
		log = new CustomerCallLog(clock);

		// Act
		try {
			log.callCompleted(cust1, cust2);
		} catch (UnexpectedCallException e) {
			exception = e;
		}

		// Assert
		Assert.assertNotNull(exception);
		Assert.assertEquals(exception.getMessage(),
				"Call completed event received without initialization.");
	}

	@Test
	public void EndedCallEventForDifferentCallThanInitiatedThrowsException() {
		// Arrange
		setUpClock(new long[] { 1322326800000L });
		UnexpectedCallException exception = null;
		log = new CustomerCallLog(clock);
		log.callInitiated(cust1, cust2);
		
		// Act
		try {
			log.callCompleted(cust1, cust3);
		} catch (UnexpectedCallException e) {
			exception = e;
		}
		
		// Assert
		Assert.assertNotNull(exception);
		Assert.assertEquals(exception.getMessage(),
				"Call completed event for different call than initialised.");
	}

	@Test
	public void NullLogForJustInitiatedCalls() {
		// Arrange
		setUpClock(new long[] { 1322326800000L });
		log.callInitiated(cust1, cust2);
		
		// Act
		List<Call> calls = log.getCalls(cust1);
		
		// Assert
		Assert.assertNull(calls);
	}

	@Test
	public void LogReturnedForFinishedCall() {
		// Arrange
		setUpClock(new long[] { 1322326800000L, 1322326800000L });
		log.callInitiated(cust1, cust2);
		log.callCompleted(cust1, cust2);
		
		// Act
		List<Call> calls = log.getCalls(cust1);
		
		// Assert
		Assert.assertEquals(1, calls.size());
	}

	private void setUpClock(long[] values) {
		final int n = values.length;
		final Action[] returnValues = new Action[n];
		for (int i = 0; i < n; i++)
			returnValues[i] = Expectations.returnValue(values[i]);
		m.checking(new Expectations() {
			{
				exactly(n).of(clock).getCurrentTime();
				will(onConsecutiveCalls(returnValues));
			}
		});
	}
}
