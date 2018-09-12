package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.cuba.gui.components.TextField;

import java.math.BigDecimal;
import java.util.Date;

public class SimpleSameEntityCurrencyValueDataProvider implements CurrencyValueDataProvider {

    private final Date date = new Date();

    private final TextField amountUiField;

    private CurrencyDescriptor currency;


    public SimpleSameEntityCurrencyValueDataProvider(TextField amountUiField) {
        this.amountUiField = amountUiField;
    }


    @Override
    public Date getDate() {
        return date;
    }


    @Override
    public CurrencyDescriptor getCurrency() {
        return currency;
    }


    @Override
    public BigDecimal getAmount() {
        return amountUiField.getValue();
    }


    @Override
    public void setCurrency(CurrencyDescriptor currency) {
        this.currency = currency;
    }
}
