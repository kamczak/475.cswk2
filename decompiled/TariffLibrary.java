// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 2011-11-26 15:16:26
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package com.acmetelecom.customer;


// Referenced classes of package com.acmetelecom.customer:
//            Customer, Tariff

public interface TariffLibrary
{

    public abstract Tariff tarriffFor(Customer customer);
}