package com.acmetelecom;

import com.acmetelecom.bill.BillingSystem;
import com.acmetelecom.bill.BillingSystemModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Runner {
	
	private static BillingSystem billingSystem;
	
	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(new BillingSystemModule());
		billingSystem = injector.getInstance(BillingSystem.class);
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
		billingSystem.callInitiated("447722113434", "447766814143");
		sleepSeconds(20);
		billingSystem.callCompleted("447722113434", "447766814143");
		billingSystem.createCustomerBills();
	}
	
	@SuppressWarnings("unused")
	private static void runSimpleSetOfCalls() throws InterruptedException {
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

}
