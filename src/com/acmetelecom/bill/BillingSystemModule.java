package com.acmetelecom.bill;

import com.acmetelecom.call.CallLog;
import com.acmetelecom.call.CustomerCallLog;
import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.printer.HtmlPrinter;
import com.acmetelecom.printer.Printer;
import com.acmetelecom.strategy.NewStrategy;
import com.acmetelecom.strategy.OldStrategy;
import com.acmetelecom.time.Clock;
import com.acmetelecom.time.SystemClock;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class BillingSystemModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CallLog.class).to(CustomerCallLog.class);
		bind(Clock.class).to(SystemClock.class);
		bind(Strategy.class).to(OldStrategy.class);
//		to be exchanged with
//		bind(Strategy.class).to(NewStrategy.class);
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

}
