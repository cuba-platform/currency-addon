package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch;

import com.haulmont.addon.currency.web.gui.components.currency_field.impl.WebCurrencyAddonField;
import com.haulmont.cuba.gui.data.Datasource;

public class CurrencyAmountDateChangedListener implements Datasource.ItemPropertyChangeListener {

    private final String dateFieldName;
    private final WebCurrencyAddonField callback;


    public CurrencyAmountDateChangedListener(String dateFieldName, WebCurrencyAddonField callback) {
        this.dateFieldName = dateFieldName;
        this.callback = callback;
    }


    @Override
    public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
        if (e.getProperty().equals(dateFieldName)) {
            callback.updatePopupContent();
        }
    }
}
