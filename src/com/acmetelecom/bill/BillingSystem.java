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
import com.acmetelecom.strategy.ChargingStrategy;
import com.google.inject.Inject;

/**
 * Main component of the system that coordinates all of the parts
 * together and that acts as an API to the system.
 */
public class BillingSystem {
    private CustomerDatabase customerDatabase;
    private TariffLibrary tariffLibrary;
    private CallLog callLog;
    private BillGenerator billGenerator;
    private ChargingStrategy strategy;

    @Inject
    public BillingSystem(CustomerDatabase customerDatabase,
            TariffLibrary tariffLibrary,
            CallLog callLog,
            ChargingStrategy strategy,
            BillGenerator billGenerator) {
        this.customerDatabase = customerDatabase;
        this.tariffLibrary = tariffLibrary;
        this.callLog = callLog;
        this.billGenerator = billGenerator;
        this.strategy = strategy;
    }

    /**
     * Notifies the system that the new call has been started between
     * the given numbers.
     *
     * @param callerNumber
     *          A phone number of the caller
     * @param calleeNumber
     *          A phone number of the callee
     */
    public void callInitiated(String callerNumber, String calleeNumber) {
        callLog.callInitiated(callerNumber, calleeNumber);
    }

    /**
     * Notifies the system that the call has been terminated between
     * the given numbers.
     *
     * @param callerNumber
     *          A phone number of the caller
     * @param calleeNumber
     *          A phone number of the callee
     */
    public void callCompleted(String callerNumber, String calleeNumber) {
        callLog.callCompleted(callerNumber, calleeNumber);
    }

    /**
     * Tells the system to generate a bill for each customer
     */
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
            Tariff tariff = tariffLibrary.tarriffFor(customer);
            BigDecimal callCost = strategy.getCost(tariff, call);
            totalBill = totalBill.add(callCost);
            items.add(new BillLineItem(call, callCost));
        }
        billGenerator.generateBill(customer, items, MoneyFormatter.penceToPounds(totalBill));
    }
}
