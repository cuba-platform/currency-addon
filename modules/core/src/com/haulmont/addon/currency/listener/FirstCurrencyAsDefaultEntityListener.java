package com.haulmont.addon.currency.listener;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component("curraddon_FirstCurrencyAsDefaultEntityListener")
public class FirstCurrencyAsDefaultEntityListener implements BeforeInsertEntityListener<CurrencyDescriptor> {

    @Inject
    private CurrencyService currencyService;

    @Override
    public void onBeforeInsert(CurrencyDescriptor currency, EntityManager entityManager) {
        List<CurrencyDescriptor> availableCurrencies = currencyService.getAvailableCurrencies();
        if (availableCurrencies.isEmpty()) {
            currency.setIsDefault(true);
        }
    }


}