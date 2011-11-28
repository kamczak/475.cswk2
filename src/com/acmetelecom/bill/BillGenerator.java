package com.acmetelecom.bill;

import java.util.List;

import com.acmetelecom.customer.Customer;

public interface BillGenerator {

    public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill);

}