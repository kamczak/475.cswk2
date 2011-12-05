package com.acmetelecom.bill;

import java.util.List;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.printer.Printer;
import com.google.inject.Inject;

public class PrintingBillGenerator implements BillGenerator {
    private Printer printer;

    @Inject
    public PrintingBillGenerator(Printer printer) {
        this.printer = printer;
    }

    public void send(Customer customer, List<BillLineItem> calls, String totalBill) {
        printer.printHeading(customer.getFullName(), customer.getPhoneNumber(),
                customer.getPricePlan());

        for (BillLineItem call : calls) {
            printer.printItem(call.date(), call.callee(), call.durationMinutes(),
                    MoneyFormatter.penceToPounds(call.cost()));
        }

        printer.printTotal(totalBill);
    }

}
