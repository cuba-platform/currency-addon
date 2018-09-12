package com.haulmont.addon.currency.entity;

import com.haulmont.cuba.core.entity.Entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public interface CurrencyRateAware extends Entity<UUID> {

    String VALUE_PATH = "value";
    String DATE_PATH = "date";

    Date getDate();

    CurrencyDescriptor getCurrency();

    BigDecimal getValue();

    void setDate(Date date);

    void setCurrency(CurrencyDescriptor currency);

    void setValue(BigDecimal value);
}
