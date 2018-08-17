package com.haulmont.addon.currency.core;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Date;

@ManagedResource
public interface CurrencyRateWorkerMBean {

    String NAME = "curraddon_CurrencyRateWorkerMBean";


    @ManagedOperation(description = "Update currency rates for active currencies at today")
    void updateCurrenciesRateForToday();


    @ManagedOperation(description = "Update currency rates for active currencies at passed days interval including borders")
    @ManagedOperationParameters(
            {@ManagedOperationParameter(name = "from", description = "From date"),
                    @ManagedOperationParameter(name = "to", description = "To date")})
    void updateCurrenciesRate(Date from, Date to);

}
