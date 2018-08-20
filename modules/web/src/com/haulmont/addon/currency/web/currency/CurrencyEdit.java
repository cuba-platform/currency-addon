package com.haulmont.addon.currency.web.currency;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Named;

public class CurrencyEdit extends AbstractEditor<Currency> {

    @Named("fieldGroup.code")
    private TextField codeField;

    @Override
    protected void postInit() {
        Currency currency = getItem();
        codeField.addValueChangeListener(e -> {
            if (StringUtils.isNotBlank(currency.getCode())) {
                java.util.Currency instance = java.util.Currency.getInstance(currency.getCode());
                if (instance != null) {
                    currency.setSymbol(instance.getSymbol());
                    currency.setName(instance.getDisplayName());
                }
            }
        });
    }


    @Override
    protected void initNewItem(Currency item) {
        item.setActive(true);
    }
}