package com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield;

import com.haulmont.cuba.web.toolkit.ui.CubaCurrencyField;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;

public class CubaCurrencyAddonField extends CubaCurrencyField {

    private CubaPopupButton currencySelector;

    public CubaCurrencyAddonField(CubaTextField textField, CubaPopupButton changeCurrencyButton) {
        super(textField);

        this.currencySelector = changeCurrencyButton;
        this.currencySelector.addStyleName(CURRENCY_STYLENAME);

        container.addStyleName(CURRENCY_VISIBLE);
        container.addStyleName(currencyLabelPosition.name().toLowerCase());
        container.removeComponent(currencyLabel);
        container.addComponent(changeCurrencyButton);
    }

}