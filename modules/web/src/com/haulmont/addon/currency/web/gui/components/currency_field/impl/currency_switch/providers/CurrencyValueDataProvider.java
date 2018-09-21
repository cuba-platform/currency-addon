package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;

import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyValueDataProvider {

    CurrencyDescriptor getCurrency();

    void setCurrency(CurrencyDescriptor currency);

    Date getDate();

    void setDate(Date newDate);

    BigDecimal getAmount();

    void setAmount(BigDecimal newAmount);
}
