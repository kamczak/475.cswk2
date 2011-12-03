package com.acmetelecom;

import org.joda.time.DateTime;

import com.acmetelecom.time.Clock;

public class FakeClock implements Clock {

	DateTime[] nextTimes;
	int nextTime;

	public FakeClock() {
		nextTimes = new DateTime[2];
		nextTime = -1;
	}

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

	public void setNextTwoTimes(DateTime first, DateTime second) {
		nextTimes[0] = first;
		nextTimes[1] = second;
		nextTime = 0;
	}

	public void reset() {
		nextTime = -1;
	}

}
