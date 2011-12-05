package com.acmetelecom.bill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallLog;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.strategy.Strategy;
import com.google.inject.Inject;


public class BillingSystem {
	private CustomerDatabase customerDatabase;
	private TariffLibrary tariffDatabase;
	private CallLog callLog;
	private BillGenerator billGenerator;
	private Strategy strategy;

	@Inject
	public BillingSystem(CustomerDatabase customerDatabase, TariffLibrary tariffDatabase,
			CallLog callLog, Strategy strategy, BillGenerator billGenerator) {
		this.customerDatabase = customerDatabase;
		this.tariffDatabase = tariffDatabase;
		this.callLog = callLog;
		this.billGenerator = billGenerator;
		this.strategy = strategy;
	}

	public void callInitiated(String callerNumber, String calleeNumber) {
		callLog.callInitiated(callerNumber, calleeNumber);
	}

	public void callCompleted(String callerNumber, String calleeNumber) {
		callLog.callCompleted(callerNumber, calleeNumber);
	}

	public void createCustomerBills() {
		List<Customer> customers = customerDatabase.getCustomers();
		for (Customer customer : customers) {
			createBillFor(customer);
		}
	}

	private void createBillFor(Customer customer) {
		List<Call> calls = callLog.getCalls(customer);

		BigDecimal totalBill = new BigDecimal(0);
		List<BillLineItem> items = new ArrayList<BillLineItem>();

		for (Call call : calls) {
			Tariff tariff = tariffDatabase.tarriffFor(customer);
			BigDecimal callCost = strategy.getCost(tariff, call);
			totalBill = totalBill.add(callCost);
			items.add(new BillLineItem(call, callCost));
		}

		billGenerator.send(customer, items, MoneyFormatter.penceToPounds(totalBill));
	}
}
