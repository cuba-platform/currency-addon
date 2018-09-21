package com.haulmont.addon.currency.web.gui.components.currency_field;


import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.components.Field;

public interface CurrencyAddonField extends Field, Component.Buffered {
    String NAME = "currencyAddonField";


    /**
     * Sets where the currency label will be located: to the left or to the right from the text input component.
     *
     * @param currencyLabelPosition not-null {@link CurrencyField.CurrencyLabelPosition} value
     */
    void setCurrencyLabelPosition(CurrencyField.CurrencyLabelPosition currencyLabelPosition);


    /**
     * @return where the currency label is located
     */
    CurrencyField.CurrencyLabelPosition getCurrencyLabelPosition();


    /**
     * Set show date currency value with time
     */
    void setWithTime(boolean withTime);


    /**
     * Is show currency value date field with time
     */
    boolean isWithTime();
}