


import com.acmetelecom.BillingSystemUnderTest;
import com.acmetelecom.customer.Customer;

import fit.ColumnFixture;

public class GivenTheFollowingCustomers extends ColumnFixture {
	public String Name, PhoneNumber, PricePlan;

	public void execute() {
		BillingSystemUnderTest.customerDatabase.addCustomer(new Customer(Name, PhoneNumber, PricePlan));
	}

}
