

import java.util.ArrayList;
import java.util.List;

import com.acmetelecom.BillingSystemUnderTest;
import com.acmetelecom.printer.FakePrinter;

import fit.RowFixture;

public class TheCustomersChargedAre extends RowFixture {

	public static class Row {
		public String Name, PhoneNumber, PricePlan, Total;

		public Row(FakePrinter.Heading h) {
			Name = h.getName();
			PhoneNumber = h.getPhoneNumber();
			PricePlan = h.getPricePlan();
			Total = h.getTotal();
		}

	}

	@Override
	public Class<?> getTargetClass() {
		return Row.class;
	}

	@Override
	public Object[] query() throws Exception {
		List<FakePrinter.Heading> headings = BillingSystemUnderTest.printer.getCustomers();
		List<Row> rows = new ArrayList<Row>();
		for(FakePrinter.Heading h : headings) {
			rows.add(new Row(h));
		}
		return rows.toArray();
	}
}
