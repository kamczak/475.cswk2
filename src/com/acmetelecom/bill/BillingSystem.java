package com.acmetelecom.bill;

import com.acmetelecom.call.Call;
import com.acmetelecom.call.CallEvent;
import com.acmetelecom.call.CallLog;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.CustomerDatabase;
import com.acmetelecom.customer.Tariff;
import com.acmetelecom.customer.TariffLibrary;
import com.acmetelecom.time.Clock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillingSystem {
    private CustomerDatabase _customerDatabase;
    private TariffLibrary _tariffDatabase;
    private CallLog _callLog;
    private BillGenerator _billGenerator;
    private Clock _clock;

    public BillingSystem(CustomerDatabase customerDatabase, TariffLibrary tariffDatabase,
            CallLog callLog, BillGenerator billGenerator, Clock clock) {
        _customerDatabase = customerDatabase;
        _tariffDatabase = tariffDatabase;
        _callLog = callLog;
        _clock = clock;
        _billGenerator = billGenerator;
    }

    public void callInitiated(Customer caller, Customer callee) {
        _callLog.callInitiated(caller, callee);
    }

    public void callCompleted(Customer caller, Customer callee) {
        _callLog.callCompleted(caller, callee);
    }

    public void createCustomerBills() {
        List<Customer> customers = _customerDatabase.getCustomers();
        for (Customer customer : customers) {
            createBillFor(customer);
        }
    }

    public void createCustomerBills(List<Customer> customers_) {
        for (Customer customer : customers_) {
            createBillFor(customer);
        }
    }

    private void createBillFor(Customer customer) {
        List<Call> calls = _callLog.getCalls(customer);

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
