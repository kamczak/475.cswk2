package com.acmetelecom;

import org.joda.time.DateTime;

import com.acmetelecom.time.Clock;

/**
 * Fit-specific Clock mockery. Stores two next times that are used
 * for call start and call end, so that the BillingSystem can register
 * a call at any time that is given in the fixtures. 
 */
public class FakeClock implements Clock {

	DateTime[] nextTimes;
	int nextTime;

	/**
	 * Creates a Clock mockery, but does <b>not</b> initialise it automatically
	 */
	public FakeClock() {
		nextTimes = new DateTime[2];
		nextTime = -1;
	}

	public void setNextTwoTimes(DateTime first, DateTime second) {
		nextTimes[0] = first;
		nextTimes[1] = second;
		nextTime = 0;
	}
	
	/**
	 * Returns the DateTimes set before.
	 * 
	 * Remember to initialise the FakeClock with method setNextTwoTimes()
	 * before calling this method. Otherwise it will throw {@link IllegalStateException}.
	 */
	@Override
	public DateTime getCurrentDateTime() {
		if(nextTime < 0) {
			throw new IllegalStateException(
					"Fake clock used without initialisation");
		}
		if(nextTime > 1) {
			throw new IllegalStateException(
					"Fake clock used more than twice after last initialisation.");
		}
		return nextTimes[nextTime++];
	}

	/**
	 * Removes any remaining times that were set to be returned in the next calls to
	 * getCurrentTime() 
	 */
	public void reset() {
		nextTime = -1;
	}

}
