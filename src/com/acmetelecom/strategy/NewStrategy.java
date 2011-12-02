package com.acmetelecom.strategy;

import java.math.BigDecimal;

import com.acmetelecom.bill.Strategy;
import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

public class NewStrategy implements Strategy {

	@Override
	public BigDecimal getCost(Tariff tariff, Call call) {
		return null;
	}

}
