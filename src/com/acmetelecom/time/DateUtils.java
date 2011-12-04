package com.acmetelecom.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

	private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	private static DateTimeFormatter billingFormat = DateTimeFormat.forPattern("dd.MM.yy HH:mm");

	public static DateTime parseStringToDateTime(String s) {
		DateTime dateTime = null;
		try {
			dateTime = dtf.parseDateTime(s);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	public static String dateToString(DateTime d) {
	    return d.toString(dtf);
	}

	public static String dateToString(long l) {
		return dateToString(new DateTime(l));
	}

	public static String dateToBillingFormat(DateTime d) {
		return d.toString(billingFormat);
	}
}
