package com.haulmont.addon.currency.listener;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component("curraddon_FirstCurrencyAsDefaultEntityListener")
public class FirstCurrencyAsDefaultEntityListener implements BeforeInsertEntityListener<Currency> {

    @Inject
    private CurrencyService currencyService;

    @Override
    public void onBeforeInsert(Currency currency, EntityManager entityManager) {
        List<Currency> availableCurrencies = currencyService.getAvailableCurrencies();
        if (availableCurrencies.isEmpty()) {
            currency.setIsDefault(true);
        }
    }


}