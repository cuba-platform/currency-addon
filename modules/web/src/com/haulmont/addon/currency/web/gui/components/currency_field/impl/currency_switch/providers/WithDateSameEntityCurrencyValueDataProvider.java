package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Date;

public class WithDateSameEntityCurrencyValueDataProvider extends SimpleSameEntityCurrencyValueDataProvider {

    private final Datasource datasource;
    private final String amountDateFieldName;


    public WithDateSameEntityCurrencyValueDataProvider(TextField amountUiField, Datasource datasource, String amountDateFieldName) {
        super(amountUiField);

        this.datasource = datasource;
        this.amountDateFieldName = amountDateFieldName;
    }


    @Override
    public Date getDate() {
        Date amountDate = null;
        if (amountDateFieldName != null) {
            Entity item = datasource.getItem();
            if (item != null) {
                amountDate = item.getValue(amountDateFieldName);
            }
        }
        return amountDate;
    }
}
