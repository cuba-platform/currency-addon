package com.haulmont.addon.currency.entity;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyRateAware {

    String VALUE_PATH = "value";


    Date getDate();

    void setDate(@Nonnull Date date);


    CurrencyDescriptor getCurrency();

    void setCurrency(@Nonnull CurrencyDescriptor currency);


    BigDecimal getValue();

    void setValue(@Nonnull BigDecimal value);

}
