package com.haulmont.addon.currency.service;

public interface CurrencyRatesService {
    String NAME = "curraddon_CurrencyRatesService";


    /**
     * Is scheduling enabled for auto update rates from external system
     */
    boolean autoUpdateIsActive();


    /**
     * Enable/Disable auto update rates from external system
     * @param isActive new value
     */
    void setAutoUpdateActive(boolean isActive);
}
