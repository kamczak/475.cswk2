package com.acmetelecom.customer;

import java.util.ArrayList;
import java.util.List;

public class FakeCustomerDatabase implements CustomerDatabase {

    private List<Customer> customers;
    
    public FakeCustomerDatabase() {
	customers = new ArrayList<Customer>();
    }
    
    @Override
    public List<Customer> getCustomers() {
	return customers;
    }
    
    public void addCustomer(Customer c) {
	customers.add(c);
    }

    public void clear() {
	customers.clear();
	
    }

}
