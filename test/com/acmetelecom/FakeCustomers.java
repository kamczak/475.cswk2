package com.acmetelecom;

import java.util.ArrayList;
import java.util.List;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;

public class FakeCustomers {
    public static final String FIRST_CUSTOMER_NAME = "Fred Bloggs";
    public static final String FIRST_CUSTOMER_NUMBER = "447711232343";
    public static final Tariff FIRST_CUSTOMER_TARIFF = Tariff.Standard;
    public static final Customer FIRST_CUSTOMER = new Customer(FIRST_CUSTOMER_NAME,
            FIRST_CUSTOMER_NUMBER, FIRST_CUSTOMER_TARIFF.name());

    public static final String SECOND_CUSTOMER_NAME = "Joe Doe";
    public static final String SECOND_CUSTOMER_NUMBER = "447722232355";
    public static final Tariff SECOND_CUSTOMER_TARIFF = Tariff.Leisure;
    public static final Customer SECOND_CUSTOMER = new Customer(SECOND_CUSTOMER_NAME,
            SECOND_CUSTOMER_NUMBER, SECOND_CUSTOMER_TARIFF.name());
    
    public static final String OTHER_CUSTOMER_NAME = "Otter Joenes";
    public static final String OTHER_CUSTOMER_NUMBER = "447722232366";
    public static final Tariff OTHER_CUSTOMER_TARIFF = Tariff.Leisure;
    public static final Customer OTHER_CUSTOMER = new Customer(OTHER_CUSTOMER_NAME,
            OTHER_CUSTOMER_NUMBER, OTHER_CUSTOMER_TARIFF.name());

    @SuppressWarnings("serial")
    public static final List<Customer> ONE_CUSTOMER_LIST = new ArrayList<Customer>(1) {
        {
            add(FIRST_CUSTOMER);
        }
    };

    @SuppressWarnings("serial")
    public static final List<Customer> TWO_CUSTOMERS_LIST = new ArrayList<Customer>(2) {
        {
            add(FIRST_CUSTOMER);
            add(SECOND_CUSTOMER);
        }
    };
}
