package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.cuba.gui.components.TextField;

import java.math.BigDecimal;
import java.util.Date;

public class SimpleSameEntityCurrencyValueDataProvider implements CurrencyValueDataProvider {

    private final Date date = new Date();

    private final TextField amountUiField;

    private Currency currency;


    public SimpleSameEntityCurrencyValueDataProvider(TextField amountUiField) {
        this.amountUiField = amountUiField;
    }


    @Override
    public Date getDate() {
        return date;
    }


    @Override
    public Currency getCurrency() {
        return currency;
    }


    @Override
    public BigDecimal getAmount() {
        return amountUiField.getValue();
    }


    @Override
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
