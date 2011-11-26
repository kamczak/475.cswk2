package com.acmetelecom.bill;

import com.acmetelecom.customer.Customer;
import com.acmetelecom.printer.HtmlPrinter;
import com.acmetelecom.printer.Printer;

import java.util.List;

public class PrintingBillGenerator implements BillGenerator
{
	private Printer _printer;

	public PrintingBillGenerator(Printer printer_)
	{
		_printer = printer_;
	}

	public void send(Customer customer, List<BillingSystem.LineItem> calls, String totalBill)
	{
		_printer.printHeading(customer.getFullName(), customer.getPhoneNumber(), customer.getPricePlan());
		for (BillingSystem.LineItem call : calls)
		{
			_printer.printItem(call.date(), call.callee(), call.durationMinutes(), MoneyFormatter.penceToPounds(call.cost()));
		}
		_printer.printTotal(totalBill);
	}

}
