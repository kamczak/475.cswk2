package com.acmetelecom.time;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

	private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat billingFormat = new SimpleDateFormat("dd.MM.yy HH:mm");

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

	public static String dateToString(Date d) {
		return df.format(d);
	}

	public static String dateToString(long l) {
		return dateToString(new Date(l));
	}

	public static String dateToBillingFormat(Date d) {
		return billingFormat.format(d);
	}
}
