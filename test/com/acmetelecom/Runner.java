package com.acmetelecom;

import com.acmetelecom.bill.BillGenerator;
import com.acmetelecom.bill.PrintingBillGenerator;
import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.printer.HtmlPrinter;
import com.acmetelecom.time.Clock;
import com.acmetelecom.time.SystemClock;

public class Runner {
    public static void main(String[] args) throws Exception {
        runOneStandardCall();
    }

    private static void sleepSeconds(int n) throws InterruptedException {
        while (n > 0) {
            System.out.println("Sleeping for " + n + " seconds more.");
            Thread.sleep(1000);
            n--;
        }
    }

    private static void runOneStandardCall() throws InterruptedException {
        BillGenerator bg = new PrintingBillGenerator(HtmlPrinter.getInstance());
        Clock ck = new SystemClock();
        BillingSystem billingSystem = new BillingSystem(CentralCustomerDatabase.getInstance(),
                CentralTariffDatabase.getInstance(), bg, ck);
        billingSystem.callInitiated("447722113434", "447766814143");
        sleepSeconds(20);
        billingSystem.callCompleted("447722113434", "447766814143");
        billingSystem.createCustomerBills();
    }

<<<<<<< HEAD
    @SuppressWarnings("unused")
    private static void runSimpleSetOfCalls() throws InterruptedException {
        System.out.println("Running...");
        BillGenerator bg = new PrintingBillGenerator(HtmlPrinter.getInstance());
        Clock ck = new SystemClock();
        BillingSystem billingSystem = new BillingSystem(CentralCustomerDatabase.getInstance(),
                CentralTariffDatabase.getInstance(), bg, ck);
        billingSystem.callInitiated("447722113434", "447766814143");
        sleepSeconds(20);
        billingSystem.callCompleted("447722113434", "447766814143");
        billingSystem.callInitiated("447722113434", "447711111111");
        sleepSeconds(30);
        billingSystem.callCompleted("447722113434", "447711111111");
        billingSystem.callInitiated("447777765432", "447711111111");
        sleepSeconds(60);
        billingSystem.callCompleted("447777765432", "447711111111");
        billingSystem.createCustomerBills();
    }
=======
	private static void sleepSeconds(int n) throws InterruptedException
	{
		while(n>0)
		{
			System.out.println("Sleeping for " + n + " seconds more.");
			Thread.sleep(1000);
			n--;
		}
	}
	
	
	@SuppressWarnings("unused")
	private static void runOneStandardCall() throws InterruptedException
	{
		BillGenerator bg = new PrintingBillGenerator(HtmlPrinter.getInstance());
		Clock ck = new SystemClock();
		BillingSystem billingSystem = new BillingSystem(bg, ck);
//		billingSystem.callInitiated("447722113434", "447766814143");
//		sleepSeconds(20);
//		billingSystem.callCompleted("447722113434", "447766814143");
		List<Customer> customers = CentralCustomerDatabase.getInstance().getCustomers();
		billingSystem.createCustomerBills(customers);
	}
	
	
	@SuppressWarnings("unused")
	private static void runSimpleSetOfCalls() throws InterruptedException
	{
		System.out.println("Running...");
		BillGenerator bg = new PrintingBillGenerator(HtmlPrinter.getInstance());
		Clock ck = new SystemClock();
		BillingSystem billingSystem = new BillingSystem(bg, ck);
//		billingSystem.callInitiated("447722113434", "447766814143");
//		sleepSeconds(20);
//		billingSystem.callCompleted("447722113434", "447766814143");
//		billingSystem.callInitiated("447722113434", "447711111111");
//		sleepSeconds(30);
//		billingSystem.callCompleted("447722113434", "447711111111");
//		billingSystem.callInitiated("447777765432", "447711111111");
//		sleepSeconds(60);
//		billingSystem.callCompleted("447777765432", "447711111111");
		List<Customer> customers = CentralCustomerDatabase.getInstance().getCustomers();
		billingSystem.createCustomerBills(customers);
	}
>>>>>>> remotes/origin/bober
}
