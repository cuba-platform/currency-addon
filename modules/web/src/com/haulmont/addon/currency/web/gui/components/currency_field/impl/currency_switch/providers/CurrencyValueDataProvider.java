package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.addon.currency.entity.Currency;

import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyValueDataProvider {

    Currency getCurrency();

    void setCurrency(Currency currency);

    Date getDate();

    BigDecimal getAmount();
}
