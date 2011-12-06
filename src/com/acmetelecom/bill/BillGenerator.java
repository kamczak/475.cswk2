package com.acmetelecom.bill;

import java.util.List;

import com.acmetelecom.customer.Customer;

public interface BillGenerator {

    public void generateBill(Customer customer, List<BillLineItem> calls, String totalBill);

}