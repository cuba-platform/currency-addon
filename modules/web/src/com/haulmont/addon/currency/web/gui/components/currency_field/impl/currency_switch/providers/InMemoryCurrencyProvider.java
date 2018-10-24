package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;

import java.math.BigDecimal;
import java.util.Date;

public class InMemoryCurrencyProvider implements CurrencyDataProvider {

    private final Metadata metaData = AppBeans.get(Metadata.class);
    private final CurrencyService currencyService = AppBeans.get(CurrencyService.class);
    private final TimeSource timeSource = AppBeans.get(TimeSource.class);

    private final CurrencyValueChangedEventSupplier changedEventSupplier;

    private CurrencyRateAware currencyRateAware;


    public InMemoryCurrencyProvider(CurrencyValueChangedEventSupplier changedEventSupplier) {
        this.changedEventSupplier = changedEventSupplier;
        currencyRateAware = getOrCreateCurrency();
    }


    @Override
    public CurrencyDescriptor getCurrency() {
        return currencyRateAware != null ? currencyRateAware.getCurrency() : null;
    }


    @Override
    public void setCurrency(CurrencyDescriptor currency) {
        getOrCreateCurrency().setCurrency(currency);
        changedEventSupplier.modelChanged();
    }


    @Override
    public Date getDate() {
        return currencyRateAware != null ? currencyRateAware.getDate() : null;
    }


    @Override
    public void setDate(Date newDate) {
        getOrCreateCurrency().setDate(newDate);
        changedEventSupplier.modelChanged();
    }


    private CurrencyRateAware getOrCreateCurrency() {
        return currencyRateAware != null ? currencyRateAware : createCurrencyValue();
    }


    private CurrencyRateAware createCurrencyValue() {
        Currency currency = metaData.create(Currency.class);
        currency.setCurrency(currencyService.getDefaultCurrency());
        currency.setDate(timeSource.currentTimestamp());
        currency.setValue(BigDecimal.ZERO);
        return currency;
    }


    @Override
    public BigDecimal getAmount() {
        return currencyRateAware != null ? currencyRateAware.getValue() : null;
    }


    @Override
    public void setAmount(BigDecimal newAmount) {
        getOrCreateCurrency().setValue(newAmount);
        changedEventSupplier.modelChanged();
    }


    @Override
    public CurrencyRateAware getItem() {
        return currencyRateAware;
    }


    @Override
    public void setItem(CurrencyRateAware currencyRateAware) {
        this.currencyRateAware = currencyRateAware;
        changedEventSupplier.modelChanged();
    }
}
