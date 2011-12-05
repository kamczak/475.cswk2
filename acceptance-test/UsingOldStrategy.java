import com.acmetelecom.BillingSystemUnderTest;

import fit.Fixture;
import fit.Parse;


public class UsingOldStrategy extends Fixture {

	@Override
	public void doTable(Parse p) {
		BillingSystemUnderTest.useOldStrategy();
	}
}
