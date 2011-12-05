import com.acmetelecom.BillingSystemUnderTest;

import fit.Fixture;
import fit.Parse;


public class UsingNewStrategy extends Fixture {

	@Override
	public void doTable(Parse p) {
		BillingSystemUnderTest.useNewStrategy();
	}
}
