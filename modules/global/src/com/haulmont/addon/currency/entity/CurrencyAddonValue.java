package com.haulmont.addon.currency.entity;

import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyAddonValue {

    Date getDate();

    Currency getCurrency();

    BigDecimal getValue();

    void setDate(Date date);

    void setCurrency(Currency currency);

    void setValue(BigDecimal value);
}
