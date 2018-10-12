package com.haulmont.addon.currency.web.currencydescriptor;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.format.CurrencyBigDecimalFormat;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

public class CurrencyEdit extends AbstractEditor<CurrencyDescriptor> {
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyEdit.class);

    @Named("fieldGroup.code")
    private TextField codeField;

    @Override
    protected void postInit() {
        codeField.addValueChangeListener(event -> {
            CurrencyDescriptor currency = getItem();
            String currencyCode = currency.getCode();
            if (StringUtils.isNotBlank(currencyCode)) {
                try {
                    java.util.Currency instance = java.util.Currency.getInstance(currencyCode);
                    currency.setSymbol(instance.getSymbol());
                    currency.setName(instance.getDisplayName());
                } catch (IllegalArgumentException e) {
                    LOG.debug("Unknown currency code '{}'", currencyCode, e);
                }
            }
        });
    }


    @Override
    protected void initNewItem(CurrencyDescriptor item) {
        item.setActive(true);
        item.setIsDefault(false);
        item.setPrecision(CurrencyBigDecimalFormat.DEFAULT_PRECISION);
    }
}