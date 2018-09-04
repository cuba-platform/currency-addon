package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch;

public interface CurrencyValueChangedEventSupplier {

    /**
     * Parameters changed need to rerender button popup content
     */
    void updatePopupContent();


    /**
     * Need to reload amount to field from data provider
     */
    void reloadAmount();
}
