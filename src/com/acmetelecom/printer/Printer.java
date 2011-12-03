package com.acmetelecom.printer;

import org.joda.time.DateTime;

import org.joda.time.DateTime;

public interface Printer {

    void printHeading(String name, String phoneNumber, String pricePlan);

    void printItem(DateTime time, String callee, String duration, String cost);

    void printTotal(String total);
}
