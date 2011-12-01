

import com.acmetelecom.BillingSystemUnderTest;

import fit.Fixture;
import fit.Parse;

public class AfterBillGeneration extends Fixture {

    @Override
    public void doTable(Parse p) {
	BillingSystemUnderTest.billingSystem.createCustomerBills();
    }
}
