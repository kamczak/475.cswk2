package com.acmetelecom.bill;

import java.math.BigDecimal;

/**
 * Small utility converting pounds to pence in a fixed format
 */
public class MoneyFormatter {
    public static String penceToPounds(BigDecimal pence) {
        BigDecimal pounds = pence.divide(new BigDecimal(100));
        return String.format("%.2f", pounds.doubleValue());
    }
}
