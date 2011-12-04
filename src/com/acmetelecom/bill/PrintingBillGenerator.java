package com.acmetelecom.bill;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.printer.Printer;
import com.google.inject.Inject;

import java.util.List;

public class PrintingBillGenerator implements BillGenerator {
    private Printer printer;

    @Inject
    public PrintingBillGenerator(Printer printer) {
        this.printer = printer;
    }

    public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill) {
        printer.printHeading(customer.getFullName(), customer.getPhoneNumber(),
                customer.getPricePlan());

        for (BillingSystem.LineItem call : calls) {
            printer.printItem(call.date(), call.callee(), call.durationMinutes(),
                    MoneyFormatter.penceToPounds(call.cost()));
        }

        printer.printTotal(totalBill);
    }

}
