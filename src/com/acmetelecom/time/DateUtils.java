package com.acmetelecom.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat billingFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
    
    public static Calendar parseStringToCalendar(String s) {
	Calendar cal=Calendar.getInstance();
	Date d1;
	try {
	    d1 = df.parse(s);
	    cal.setTime(d1);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return cal;
    }
    
    public static long parseStringToLong(String s) {
	Calendar cal = parseStringToCalendar(s);
	return cal.getTimeInMillis();
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
