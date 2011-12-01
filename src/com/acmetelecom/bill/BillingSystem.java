package com.acmetelecom.bill;

import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallLog;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillingSystem {
    private CustomerDatabase customerDatabase;
    private TariffLibrary tariffDatabase;
    private CallLog callLog;
    private BillGenerator billGenerator;
    private Strategy strategy;

    //tralalala
    public BillingSystem(CustomerDatabase customerDatabase, TariffLibrary tariffDatabase,
            CallLog callLog, Strategy strategy, BillGenerator billGenerator) {
        this.customerDatabase = customerDatabase;
        this.tariffDatabase = tariffDatabase;
        this.callLog = callLog;
        this.billGenerator = billGenerator;
        this.strategy = strategy;
    }

    public void callInitiated(String caller, String callee) {
        callLog.callInitiated(caller, callee);
    }

    public void callCompleted(String caller, String callee) {
        callLog.callCompleted(caller, callee);
    }

    public void createCustomerBills() {
        List<Customer> customers = customerDatabase.getCustomers();
        for (Customer customer : customers) {
            createBillFor(customer);
        }
    }

    public void createCustomerBills(List<Customer> customers) {
        for (Customer customer : customers) {
            createBillFor(customer);
        }
    }

    private void createBillFor(Customer customer) {
        List<Call> calls = callLog.getCalls(customer);

        BigDecimal totalBill = new BigDecimal(0);
        List<LineItem> items = new ArrayList<LineItem>();

        for (Call call : calls) {

            Tariff tariff = tariffDatabase.tarriffFor(customer);

            BigDecimal cost;

            DaytimePeakPeriod peakPeriod = new DaytimePeakPeriod();
            if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime())
                    && call.durationSeconds() < 12 * 60 * 60) {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.offPeakRate());
            } else {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.peakRate());
            }

            cost = cost.setScale(0, RoundingMode.HALF_UP);
            BigDecimal callCost = cost;
            totalBill = totalBill.add(callCost);
            items.add(new LineItem(call, callCost));
        }

        billGenerator.send(customer, items, MoneyFormatter.penceToPounds(totalBill));
    }

    static class LineItem {
	private Call call;
	private BigDecimal callCost;

	public LineItem(Call call, BigDecimal callCost) {
	    this.call = call;
	    this.callCost = callCost;
	}

	public Date date() {
	    return call.startTime();
	}

	public String callee() {
	    return call.callee();
	}

	public String durationMinutes() {
	    return "" + call.durationSeconds() / 60 + ":"
		    + String.format("%02d", call.durationSeconds() % 60);
	}

	public BigDecimal cost() {
	    return callCost;
	}
    }
}
