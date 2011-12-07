import com.acmetelecom.BillingSystemUnderTest;

import fit.Fixture;
import fit.Parse;


public class UsingPercentagePeakCharging extends Fixture {

	@Override
	public void doTable(Parse p) {
		BillingSystemUnderTest.usePercentagePeakCharging();
	}
}
