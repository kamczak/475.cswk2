package com.acmetelecom.printer;

import java.util.Date;

public interface Printer {

    void printHeading(String name, String phoneNumber, String pricePlan);

    void printItem(Date time, String callee, String duration, String cost);

    void printTotal(String total);
}
