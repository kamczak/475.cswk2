package com.acmetelecom.bill;

import java.util.List;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.printer.Printer;
import com.acmetelecom.time.DateStringUtils;
import com.google.inject.Inject;

/**
 * Generates the bill using the supplied Printer
 */
public class PrintingBillGenerator implements BillGenerator {
    private Printer printer;

    @Inject
    public PrintingBillGenerator(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void generateBill(Customer customer, List<BillLineItem> calls, String totalBill) {
        printer.printHeading(customer.getFullName(), customer.getPhoneNumber(),
                customer.getPricePlan());

        for (BillLineItem call : calls) {
            printer.printItem(call.getStartDateTime(), call.getCallee(), 
            		DateStringUtils.durationToBillFormattedString(call.getDuration()),
                    MoneyFormatter.penceToPounds(call.getCost()));
        }

        printer.printTotal(totalBill);
    }

}
