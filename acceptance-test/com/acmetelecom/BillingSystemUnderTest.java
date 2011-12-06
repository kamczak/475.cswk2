package com.acmetelecom;

import org.joda.time.LocalTime;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.bill.PrintingBillGenerator;
import com.acmetelecom.call.CallLog;
import com.acmetelecom.call.CustomerCallLog;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.FakeCustomerDatabase;
import com.acmetelecom.peak.DailyPeakPeriod;
import com.acmetelecom.peak.PeakPeriod;
import com.acmetelecom.printer.FakePrinter;
import com.acmetelecom.strategy.PercentagePeakCharging;
import com.acmetelecom.strategy.AggresivePeakCharging;
import com.acmetelecom.strategy.ChargingStrategy;

public class BillingSystemUnderTest {

	public static final FakePrinter printer = new FakePrinter();
	public static final FakeCustomerDatabase customerDatabase = new FakeCustomerDatabase();
	public static final BillGenerator billGenerator = new PrintingBillGenerator(printer);
	public static final FakeClock clock = new FakeClock();
	public static CallLog callLog;
	public static PeakPeriod peakPeriod = new DailyPeakPeriod(new LocalTime(7, 0), new LocalTime(19, 0));
	public static final ChargingStrategy oldStrategy = new AggresivePeakCharging(peakPeriod);
	public static final ChargingStrategy newStrategy = new PercentagePeakCharging(peakPeriod);
	public static ChargingStrategy currentStrategy;
	public static BillingSystem billingSystem;

	public static void resetCustomerDatabase() {
		customerDatabase.clear();
	}
	
	public static void useOldStrategy() {
		currentStrategy = oldStrategy;
	}
	
	public static void useNewStrategy() {
		currentStrategy = newStrategy;
	}
	
	public static void initialiseBillingSystem() {
		printer.clear();
		customerDatabase.clear();
		// billGenerator can stay as it is
		clock.reset();
		callLog = new CustomerCallLog(clock);
		billingSystem = new BillingSystem(
				customerDatabase,
				CentralTariffDatabase.getInstance(),
				callLog,
				currentStrategy,
				billGenerator);
	}
}
