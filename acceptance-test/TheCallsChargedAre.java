

import java.util.ArrayList;
import java.util.List;

import com.acmetelecom.BillingSystemUnderTest;
import com.acmetelecom.printer.FakePrinter;

import fit.RowFixture;

public class TheCallsChargedAre extends RowFixture {

    public static class Row {
	public String Time, Caller, Callee, Duration, Cost;
	
	public Row(FakePrinter.Call c) {
	    Time = c.getTime();
	    Caller = c.getCaller();
	    Callee = c.getCallee();
	    Duration = c.getDuration();
	    Cost = c.getCost();
	}
	
    }
    
    @Override
    public Class<?> getTargetClass() {
	return Row.class;
    }

    @Override
    public Object[] query() throws Exception {
	List<FakePrinter.Call> calls = BillingSystemUnderTest.printer.getCalls();
	List<Row> rows = new ArrayList<Row>();
	for(FakePrinter.Call c : calls) {
	    rows.add(new Row(c));
	}
	return rows.toArray();
    }

}
