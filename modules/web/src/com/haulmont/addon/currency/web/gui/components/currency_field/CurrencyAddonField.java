package com.haulmont.addon.currency.web.gui.components.currency_field;


import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.components.Field;

public interface CurrencyAddonField extends Field, Component.Buffered, CurrencyValueChangedEventSupplier {
    String NAME = "currencyAddonField";


    /**
     * Sets where the currency label will be located: to the left or to the right from the text input component.
     *
     * @param currencyButtonPosition not-null {@link CurrencyField.CurrencyLabelPosition} value
     */
    void setCurrencyButtonPosition(CurrencyField.CurrencyLabelPosition currencyButtonPosition);


    /**
     * @return where the currency label is located
     */
    CurrencyField.CurrencyLabelPosition getCurrencyButtonPosition();


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