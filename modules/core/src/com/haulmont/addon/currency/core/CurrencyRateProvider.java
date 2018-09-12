package com.haulmont.addon.currency.core;


import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRate;

import java.util.Date;
import java.util.List;

public interface CurrencyRateProvider {

    String NAME = "curraddon_CurrencyRateProvider";

    /**
     * Fetch rates from external service
     * @param date date for fetching rates
     * @param currency convert rate from this currency
     * @param targetCurrencies target currencies for convert fetching
     * @return return not null list with rate values
     * @throws Exception in any exceptional case will be thrown exception
     */
    List<CurrencyRate> getRates(Date date, CurrencyDescriptor currency, List<CurrencyDescriptor> targetCurrencies) throws Exception;
}
