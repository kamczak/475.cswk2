package com.acmetelecom.strategy;

import java.math.BigDecimal;

import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

/**
 * 
 */
public interface ChargingStrategy {
	
	public BigDecimal getCost(Tariff tariff, Call call);

}
