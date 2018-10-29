package com.haulmont.addon.currency.service;


import com.haulmont.addon.currency.entity.CurrencyDescriptor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CurrencyService {
    String NAME = "curraddon_CurrencyService";


    /**
     * @param sourceAmount amount int source currency
     * @param exchangeDate date for looking exchange rate
     * @param sourceCurrency currency for converting from
     * @param targetCurrency currency for converting to
     * @return result of exchange
     */
    ConvertResult convertAmountToRate(
            BigDecimal sourceAmount, Date exchangeDate, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency
    );


    /**
     * Exchange currency amount via now moment rates
     *
     * @param sourceAmount amount int source currency
     * @param sourceCurrency currency for converting from
     * @param targetCurrency currency for converting to
     * @return result of exchange
     */
    ConvertResult convertAmountToCurrentRate(BigDecimal sourceAmount, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency);


    /**
     * Looking currency by currency code
     * @param currencyCode code of currency e.g. USD, EUR, RUB, ...
     * @return currency wit passed code or exception
     */
    CurrencyDescriptor getCurrencyByCode(String currencyCode);


    /**
     * Fetch list of available currencies
     * @return not null list
     */
    List<CurrencyDescriptor> getAvailableCurrencies();


    /**
     * Get default application currency
     * @return currency or exception
     */
    CurrencyDescriptor getDefaultCurrency();

}