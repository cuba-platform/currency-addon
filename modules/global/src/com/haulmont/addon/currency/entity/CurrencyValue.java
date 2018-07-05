package com.haulmont.addon.currency.entity;

import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyValue {

    Date getDate();

    Currency getCurrency();

    BigDecimal getValue();
}
