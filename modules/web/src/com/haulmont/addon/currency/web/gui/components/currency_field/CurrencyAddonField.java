package com.haulmont.addon.currency.web.gui.components.currency_field;


import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Field which show amount, currency, date to user and have ability to exchange between currencies
 */
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
     * Set position of "Change Currency" button
     */
    void setCurrencyButtonPosition(@Nonnull Side side);


    /**
     * Get actual position of "Change Currency" button
     */
    Side getCurrencyButtonPosition();


    /**
     * @param currencyRateAware currency value
     */
    void setCurrencyRateAware(@Nullable CurrencyRateAware currencyRateAware);


    /**
     * @return currency value or null
     */
    @Nullable
    CurrencyRateAware getCurrencyRateAware();

}