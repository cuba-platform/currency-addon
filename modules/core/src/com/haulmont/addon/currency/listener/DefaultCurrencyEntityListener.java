package com.haulmont.addon.currency.listener;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("curraddon_DefaultCurrencyEntityListener")
public class DefaultCurrencyEntityListener implements BeforeInsertEntityListener<CurrencyDescriptor>, BeforeUpdateEntityListener<CurrencyDescriptor> {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCurrencyEntityListener.class);

    @Inject
    private CurrencyService currencyService;


    @Override
    public void onBeforeInsert(CurrencyDescriptor currency, EntityManager entityManager) {
        handleChange(currency, entityManager);
    }


    @Override
    public void onBeforeUpdate(CurrencyDescriptor currency, EntityManager entityManager) {
        handleChange(currency, entityManager);
    }


    private void handleChange(CurrencyDescriptor currency, EntityManager entityManager) {
        if (currency.getIsDefault()) {
            CurrencyDescriptor oldDefaultCurrency = currencyService.getDefaultCurrency();
            if (oldDefaultCurrency != null && !currency.equals(oldDefaultCurrency)) {
                LOG.info("Replace default currency {} by {}", oldDefaultCurrency.getCode(), currency.getCode());
                oldDefaultCurrency.setIsDefault(false);
                entityManager.merge(oldDefaultCurrency);
            }
        }
    }


}