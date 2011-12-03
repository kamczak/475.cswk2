package com.acmetelecom.time;

import org.joda.time.DateTime;

public class SystemClock implements Clock
{

	@Override
	public DateTime getCurrentDateTime()
	{
		return DateTime.now();
	}

}
