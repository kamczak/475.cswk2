package com.acmetelecom.bill;

import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEnd;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.call.CallStart;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.time.Clock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillingSystem {
    private TariffLibrary _tariffDatabase;
    private BillGenerator _billGenerator;
    private Clock _clock;

    public BillingSystem(TariffLibrary tariffDatabase, BillGenerator billGenerator, Clock clock) {
	_tariffDatabase = tariffDatabase;
	_clock = clock;
	_billGenerator = billGenerator;
    }

    private List<CallEvent> callLog = new ArrayList<CallEvent>();

    public void callInitiated(String caller, String callee) {
	callLog.add(new CallStart(caller, callee, _clock.getCurrentTime()));
    }

    public void callCompleted(String caller, String callee) {
	callLog.add(new CallEnd(caller, callee, _clock.getCurrentTime()));
    }

    public void createCustomerBills(List<Customer> customers) {
	for (Customer customer : customers) {
	    createBillFor(customer);
	}
	callLog.clear();
    }

    private void createBillFor(Customer customer) {
	List<CallEvent> customerEvents = new ArrayList<CallEvent>();
	for (CallEvent callEvent : callLog) {
	    if (callEvent.getCaller().equals(customer.getPhoneNumber())) {
		customerEvents.add(callEvent);
	    }
	}

	List<Call> calls = new ArrayList<Call>();

	CallEvent start = null;
	for (CallEvent event : customerEvents) {
	    if (event instanceof CallStart) {
		start = event;
	    }
	    if (event instanceof CallEnd && start != null) {
		calls.add(new Call(start, event));
		start = null;
	    }
	}

	BigDecimal totalBill = new BigDecimal(0);
	List<LineItem> items = new ArrayList<LineItem>();

	for (Call call : calls) {

	    Tariff tariff = _tariffDatabase.tarriffFor(customer);

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

	_billGenerator.send(customer, items, MoneyFormatter.penceToPounds(totalBill));
    }

    static class LineItem {
	private Call call;
	private BigDecimal callCost;

	public LineItem(Call call, BigDecimal callCost) {
	    this.call = call;
	    this.callCost = callCost;
	}

	public String date() {
	    return call.date();
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