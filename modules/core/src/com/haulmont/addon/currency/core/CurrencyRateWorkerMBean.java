package com.haulmont.addon.currency.core;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Date;

@ManagedResource
public interface CurrencyRateWorkerMBean {

    String NAME = "cubawebsiteback_CurrencyRateWorkerMBean";

    @ManagedOperation(description = "Update currency rates")
    void updateCurrencies();

    @ManagedOperation
    @ManagedOperationParameters(
            {@ManagedOperationParameter(name = "from", description = "From date"),
                    @ManagedOperationParameter(name = "to", description = "To date")})
    void updateCurrencies(Date from, Date to);
}
