package com.acmetelecom.DI;

import org.joda.time.LocalTime;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.PrintingBillGenerator;
import com.acmetelecom.call.CallLog;
import com.acmetelecom.call.CustomerCallLog;
import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.peak.DailyPeakPeriod;
import com.acmetelecom.peak.PeakPeriod;
import com.acmetelecom.printer.HtmlPrinter;
import com.acmetelecom.printer.Printer;
import com.acmetelecom.strategy.PercentagePeakCharging;
import com.acmetelecom.strategy.ChargingStrategy;
import com.acmetelecom.time.Clock;
import com.acmetelecom.time.SystemClock;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Sets up dependency injection container for the main system
 */
public class BillingSystemModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CallLog.class).to(CustomerCallLog.class);
		bind(Clock.class).to(SystemClock.class);
		bind(ChargingStrategy.class).to(PercentagePeakCharging.class);
		bind(BillGenerator.class).to(PrintingBillGenerator.class);
		bind(Printer.class).to(HtmlPrinter.class);
	}
	
	@Provides
	CustomerDatabase provideCentralCustomerDatabase() {
		return CentralCustomerDatabase.getInstance();
	}
	
	@Provides
	TariffLibrary provideCentralTariffDatabase() {
		return CentralTariffDatabase.getInstance();
	}
	
	@Provides
	PeakPeriod provideDailyPeakPeriod() {
	    return new DailyPeakPeriod(new LocalTime(7, 0), new LocalTime(19, 0));
	}

}
