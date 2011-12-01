package com.acmetelecom;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.bill.PrintingBillGenerator;
import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.CallLog;
import com.acmetelecom.call.CustomerCallLog;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.FakeCustomerDatabase;
import com.acmetelecom.printer.FakePrinter;

public class BillingSystemUnderTest {

    public static final FakePrinter printer = new FakePrinter();
    public static final FakeCustomerDatabase customerDatabase = new FakeCustomerDatabase();
    public static final BillGenerator billGenerator = new PrintingBillGenerator(printer);
    public static final FakeClock clock = new FakeClock();
    public static CallLog callLog;
    public static Strategy strategy;
    public static BillingSystem billingSystem;
    
    public static void resetCustomerDatabase() {
	customerDatabase.clear();
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
	            strategy,
	            billGenerator);
    }
}
