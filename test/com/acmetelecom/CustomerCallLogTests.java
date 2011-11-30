package com.acmetelecom;

import java.util.List;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;

import com.acmetelecom.call.Call;
import com.acmetelecom.call.CustomerCallLog;
import com.acmetelecom.call.UnexpectedCallException;
import com.acmetelecom.time.Clock;

import static com.acmetelecom.FakeCustomers.*;

public class CustomerCallLogTests {
    private static final DateTime SAT_NOV_26_1700 = new DateTime("2011-11-26T17:00:00");
	@Rule
	public final JUnitRuleMockery m = new JUnitRuleMockery();

	private Clock clock = m.mock(Clock.class);
	private CustomerCallLog log = new CustomerCallLog(clock);

	@Test
	public void CallInitiatedCallsOnSystemClock() {
		// Arrange
		setUpClock(new long[] { SAT_NOV_26_1700.getMillis() });
		
		// Act
		log.callInitiated(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		
		// Assert
		m.assertIsSatisfied();
	}

	@Test
	public void CallEndedCallsOnSystemClock() {
		// Arrange
		setUpClock(new long[] { SAT_NOV_26_1700.getMillis(), SAT_NOV_26_1700.getMillis() });
		log.callInitiated(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		
		// Act
		log.callCompleted(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		
		// Assert
		m.assertIsSatisfied();
	}

	@Test
	public void TwoCallsInitiatedForSameCustomerThrowsException() {
		// Arrange
		setUpClock(new long[] { SAT_NOV_26_1700.getMillis() });

		log.callInitiated(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		UnexpectedCallException exception = null;
		
		// Act
		try {
			log.callInitiated(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		} catch (UnexpectedCallException e) {
			exception = e;
		}
		
		// Assert
		Assert.assertNotNull(exception);
		Assert.assertEquals(exception.getMessage(),
				"Call initiated twice for same number.");
	}

	@Test
	public void EndedCallWithouthCallInitializedIsIgnored() {
		// Arrange
		log = new CustomerCallLog(clock);

		// Act
		log.callCompleted(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);

		// Assert
		Assert.assertTrue(log.getCalls(FIRST_CUSTOMER).isEmpty());
	}

	@Test
	public void EndedCallEventForDifferentCallThanInitiatedIsIgnored() {
		// Arrange
		setUpClock(new long[] { SAT_NOV_26_1700.getMillis() });
		UnexpectedCallException exception = null;
		log = new CustomerCallLog(clock);
		log.callInitiated(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		
		// Act
		log.callCompleted(SECOND_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
		//Assert
		Assert.assertTrue(log.getCalls(FIRST_CUSTOMER).isEmpty());
	}

	@Test
	public void EmptyLogForJustInitiatedCalls() {
		// Arrange
		setUpClock(new long[] { SAT_NOV_26_1700.getMillis() });
		log.callInitiated(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		
		// Act
		List<Call> calls = log.getCalls(FIRST_CUSTOMER);
		
		// Assert
		Assert.assertTrue(calls.isEmpty());
	}

	@Test
	public void LogReturnedForFinishedCall() {
		// Arrange
		setUpClock(new long[] { SAT_NOV_26_1700.getMillis(), SAT_NOV_26_1700.getMillis() });
		log.callInitiated(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		log.callCompleted(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		
		// Act
		List<Call> calls = log.getCalls(FIRST_CUSTOMER);
		
		// Assert
		Assert.assertEquals(1, calls.size());
	}
	
	@Test
	public void LogReturnsCallsJustForAGivenCustomer() {
		// Arrange
		m.checking(new Expectations() {{
		    allowing(clock).getCurrentTime();
		            will(returnValue(SAT_NOV_26_1700.getMillis()));
		}});
		log.callInitiated(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		log.callCompleted(FIRST_CUSTOMER_NUMBER, SECOND_CUSTOMER_NUMBER);
		log.callInitiated(SECOND_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
		log.callCompleted(SECOND_CUSTOMER_NUMBER, OTHER_CUSTOMER_NUMBER);
		// Act
		List<Call> calls = log.getCalls(SECOND_CUSTOMER);
		
		// Assert
		Assert.assertEquals(1, calls.size());
		Call c = calls.get(0);
		Assert.assertEquals(OTHER_CUSTOMER_NUMBER, c.callee());
	}

	private void setUpClock(long[] values) {
		final int n = values.length;
		final Action[] returnValues = new Action[n];

		for (int i = 0; i < n; i++)
			returnValues[i] = Expectations.returnValue(values[i]);

		m.checking(new Expectations() {{
		    exactly(n).of(clock).getCurrentTime();
		            will(onConsecutiveCalls(returnValues));
		}});
	}
}
