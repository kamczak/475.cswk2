


import com.acmetelecom.BillingSystemUnderTest;
import com.acmetelecom.time.DateUtils;

import fit.ColumnFixture;

public class GivenTheFollowingCalls extends ColumnFixture {

	public String Caller, Callee, Start, End;

	public void execute() {
		BillingSystemUnderTest.clock.setNextTwoTimes(
				DateUtils.parseStringToDateTime(Start),
				DateUtils.parseStringToDateTime(End));
		BillingSystemUnderTest.billingSystem.callInitiated(Caller, Callee);
		BillingSystemUnderTest.billingSystem.callCompleted(Caller, Callee);
	}
}
