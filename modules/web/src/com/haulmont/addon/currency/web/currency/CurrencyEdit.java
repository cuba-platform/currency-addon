package com.haulmont.addon.currency.web.currency;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Named;

public class CurrencyEdit extends AbstractEditor<CurrencyDescriptor> {

    @Named("fieldGroup.code")
    private TextField codeField;

    @Override
    protected void postInit() {
        CurrencyDescriptor currency = getItem();
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
    protected void initNewItem(CurrencyDescriptor item) {
        item.setActive(true);
        item.setIsDefault(false);
    }
}