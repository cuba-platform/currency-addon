package com.haulmont.addon.currency.web.gui.components.currency_field;


import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;

public interface CurrencyAddonField extends Field, Component.Buffered, CurrencyValueChangedEventSupplier {
    String NAME = "currencyAddonField";


    /**
     * Set show date currency value with time field
     */
    void setWithTime(boolean withTime);


    /**
     * Is show currency value date field with time field
     */
    boolean isWithTime();


    /**
     * When we work with field programmatically
     */
    void setCurrencyRateAware(CurrencyRateAware currencyRateAware);


    CurrencyRateAware getCurrencyRateAware();

}