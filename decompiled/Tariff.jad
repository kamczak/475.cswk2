// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 2011-11-26 15:16:26
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package com.acmetelecom.customer;

import java.math.BigDecimal;

public final class Tariff extends Enum
{

    public static Tariff[] values()
    {
        return (Tariff[])$VALUES.clone();
    }

    public static Tariff valueOf(String s)
    {
        return (Tariff)Enum.valueOf(com/acmetelecom/customer/Tariff, s);
    }

    private Tariff(String s, int i, double d, double d1)
    {
        super(s, i);
        peakRate = new BigDecimal(d);
        offPeakRate = new BigDecimal(d1);
    }

    public BigDecimal peakRate()
    {
        return peakRate;
    }

    public BigDecimal offPeakRate()
    {
        return offPeakRate;
    }

    public static final Tariff Standard;
    public static final Tariff Business;
    public static final Tariff Leisure;
    private final BigDecimal peakRate;
    private final BigDecimal offPeakRate;
    private static final Tariff $VALUES[];

    static 
    {
        Standard = new Tariff("Standard", 0, 0.5D, 0.20000000000000001D);
        Business = new Tariff("Business", 1, 0.29999999999999999D, 0.29999999999999999D);
        Leisure = new Tariff("Leisure", 2, 0.80000000000000004D, 0.10000000000000001D);
        $VALUES = (new Tariff[] {
            Standard, Business, Leisure
        });
    }
}