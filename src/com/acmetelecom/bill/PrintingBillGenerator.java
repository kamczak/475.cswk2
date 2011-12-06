package com.acmetelecom.bill;

import java.util.List;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.printer.Printer;
import com.acmetelecom.time.DateStringUtils;
import com.google.inject.Inject;

public class PrintingBillGenerator implements BillGenerator {
    private Printer printer;

    @Inject
    public PrintingBillGenerator(Printer printer) {
        this.printer = printer;
    }

    public void generateBill(Customer customer, List<BillLineItem> calls, String totalBill) {
        printer.printHeading(customer.getFullName(), customer.getPhoneNumber(),
                customer.getPricePlan());

        for (BillLineItem call : calls) {
            printer.printItem(call.getDate(), call.getCallee(), 
            		DateStringUtils.durationToFormattedString(call.getDuration()),
                    MoneyFormatter.penceToPounds(call.cost()));
        }

        printer.printTotal(totalBill);
    }

}
