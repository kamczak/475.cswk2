package com.acmetelecom.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Set of utility classes for printing DateTimes
 */
public class DateStringUtils {

    private static DateTimeFormatter STANDARD_FORMATTER
            = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter BILLING_FORMATTER
            = DateTimeFormat.forPattern("dd.MM.yy HH:mm");

    public static DateTime parseStringToDateTime(String s) {
        DateTime dateTime = null;
        try {
            dateTime = STANDARD_FORMATTER.parseDateTime(s);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
        return dateTime;
    }

    public static String dateToString(DateTime dateTime) {
        return dateTime.toString(STANDARD_FORMATTER);
    }

    public static String dateToBillingFormat(DateTime dateTime) {
        return dateTime.toString(BILLING_FORMATTER);
    }

    public static String durationToBillFormattedString(Duration duration) {
        long seconds = duration.getStandardSeconds();
        StringBuilder minutes = new StringBuilder();
        minutes.append(seconds / 60);
        minutes.append(":");
        minutes.append(String.format("%02d", seconds % 60));
        return minutes.toString();
    }
}
