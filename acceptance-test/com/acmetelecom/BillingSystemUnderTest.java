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
import com.acmetelecom.strategy.AggressivePeakCharging;
import com.acmetelecom.strategy.ChargingStrategy;

/**
 * The main point of the FIT tests, that allows all classes to communicate with each other. 
 *
 */
public class BillingSystemUnderTest {

	public static final FakePrinter printer = new FakePrinter();
	public static final FakeCustomerDatabase customerDatabase = new FakeCustomerDatabase();
	public static final BillGenerator billGenerator = new PrintingBillGenerator(printer);
	public static final FakeClock clock = new FakeClock();
	public static CallLog callLog;
	public static PeakPeriod peakPeriod = new DailyPeakPeriod(new LocalTime(7, 0), new LocalTime(19, 0));
	public static final ChargingStrategy aggressiveStrategy = new AggressivePeakCharging(peakPeriod);
	public static final ChargingStrategy percentageStrategy = new PercentagePeakCharging(peakPeriod);
	public static ChargingStrategy currentStrategy;
	public static BillingSystem billingSystem;

	public static void resetCustomerDatabase() {
		customerDatabase.clear();
	}
	
	public static void useAggressivePeakCharging() {
		currentStrategy = aggressiveStrategy;
	}
	
	public static void usePercentagePeakCharging() {
		currentStrategy = percentageStrategy;
	}
	
	public static void initialiseBillingSystem() {
		// Clear all items from the previous fixtures:
		printer.clear();
		customerDatabase.clear();
		// BillGenerator can stay as it is.
		// Make sure no old times remain in the clock buffer:
		clock.reset();
		// Because the CustomerCallLog class has no clearing method,
		// a new object has to be created in here:
		callLog = new CustomerCallLog(clock);
		billingSystem = new BillingSystem(
				customerDatabase,
				CentralTariffDatabase.getInstance(),
				callLog,
				currentStrategy,
				billGenerator);
	}
}
