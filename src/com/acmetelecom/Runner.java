package com.acmetelecom;


public class Runner
{
	public static void main(String[] args) throws Exception
	{
//		runSimpleSetOfCalls();
		runOneStandardCall();
	}


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
		BillingSystem billingSystem = new BillingSystem();
		billingSystem.callInitiated("447722113434", "447766814143");
		sleepSeconds(20);
		billingSystem.callCompleted("447722113434", "447766814143");
		billingSystem.createCustomerBills();
	}
	
	
	@SuppressWarnings("unused")
	private static void runSimpleSetOfCalls() throws InterruptedException
	{
		System.out.println("Running...");
		BillingSystem billingSystem = new BillingSystem();
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
