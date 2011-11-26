package com.acmetelecom.time;

public class SystemClock implements Clock
{

	@Override
	public long getCurrentTime()
	{
		return System.currentTimeMillis();
	}

}
