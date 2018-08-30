package com.haulmont.addon.currency.web.gui.components;

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
            callback.amountDateChanged();
        }
    }
}
