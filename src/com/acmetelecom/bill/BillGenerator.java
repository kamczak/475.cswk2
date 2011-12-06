package com.acmetelecom.bill;

import java.util.List;

import com.acmetelecom.customer.Customer;

/**
 * An interfaces for classes able to generate bills
 */
public interface BillGenerator {

    /**
     * Generate a bill amounting to the given total for the given customer,
     * containing the given calls
     * 
     * @param customer
     * @param calls
     * @param totalBill
     */
    public void generateBill(Customer customer, List<BillLineItem> calls, String totalBill);

}