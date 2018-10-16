package com.haulmont.addon.currency.entity;

import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyRateAware {

    String VALUE_PATH = "value";

    Date getDate();

    CurrencyDescriptor getCurrency();

    BigDecimal getValue();

    void setDate(Date date);

    void setCurrency(CurrencyDescriptor currency);

    void setValue(BigDecimal value);
}
