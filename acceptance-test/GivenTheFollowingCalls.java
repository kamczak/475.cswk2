


import com.acmetelecom.BillingSystemUnderTest;
import com.acmetelecom.time.DateStringUtils;

import fit.ColumnFixture;

public class GivenTheFollowingCalls extends ColumnFixture {

	public String Caller, Callee, Start, End;

	public void execute() {
		BillingSystemUnderTest.clock.setNextTwoTimes(
				DateStringUtils.parseStringToDateTime(Start),
				DateStringUtils.parseStringToDateTime(End));
		BillingSystemUnderTest.billingSystem.callInitiated(Caller, Callee);
		BillingSystemUnderTest.billingSystem.callCompleted(Caller, Callee);
	}
}
