package com.haulmont.addon.currency.web.gui.components;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;

import java.math.BigDecimal;

public class TransferCurrencyDoNothingAction extends AbstractAction {

    private final Currency currency;
    private final BigDecimal amountInNewCurrency;

    public TransferCurrencyDoNothingAction(Currency currency, BigDecimal amountInNewCurrency) {
        super(currency.getCode());

        this.currency = currency;
        this.amountInNewCurrency = amountInNewCurrency;
    }


    @Override
    public void actionPerform(Component component) {}


    @Override
    public String getCaption() {
        return currency.getCode() + " - " + amountInNewCurrency.stripTrailingZeros().toPlainString();
    }
}
