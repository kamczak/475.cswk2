package com.acmetelecom.customer;

import java.util.ArrayList;
import java.util.List;

public class FakeCustomerDatabase implements CustomerDatabase {

    private List<Customer> customers;
    
    /**
     * Creates a database with no customers inside
     */
    public FakeCustomerDatabase() {
    	customers = new ArrayList<Customer>();
    }
    
    @Override
    public List<Customer> getCustomers() {
    	return customers;
    }
    
    /**
     * Add one customer to the database. If the same customer is added twice,
     * it will be stored twice. 
     */
    public void addCustomer(Customer c) {
    	customers.add(c);
    }

    /**
     * Remove any customers that have been added
     */
    public void clear() {
    	customers.clear();
    }

}
