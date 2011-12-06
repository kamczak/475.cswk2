package com.acmetelecom.strategy;

import java.math.BigDecimal;

import com.acmetelecom.call.Call;
import com.acmetelecom.customer.Tariff;

/**
 * Interfaces for classes that can calculate the cost of the call.
 */
public interface ChargingStrategy {

    /**
     * Returns the total cost of the given call using the supplied tariff
     * 
     * @param tariff
     *            A tariff to use for peak/off-peak rates
     * @param call
     *            A call to have its cost calculated
     * @return
     *         The total cost of the given call, using the supplied tariff
     */
    public BigDecimal getCost(Tariff tariff, Call call);

}
