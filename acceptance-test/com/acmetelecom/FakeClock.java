package com.acmetelecom;

import com.acmetelecom.time.Clock;

public class FakeClock implements Clock {

    long[] nextTimes;
    int nextTime;
    
    public FakeClock() {
	nextTimes = new long[2];
	nextTime = -1;
    }
    
    @Override
    public long getCurrentTime() {
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
    
    public void setNextTwoTimes(long first, long second) {
	nextTimes[0] = first;
	nextTimes[1] = second;
	nextTime = 0;
    }
    
    public void reset() {
	nextTimes[0] = -1;
	nextTimes[1] = -1;
	nextTime = -1;
    }

}
