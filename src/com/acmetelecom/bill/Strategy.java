package com.acmetelecom.bill;

import java.math.BigDecimal;

import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;


public interface Strategy {
	
	public BigDecimal getCost(Tariff tariff, Call call);

}
