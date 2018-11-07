package com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield;

import com.haulmont.cuba.web.toolkit.ui.CubaCurrencyField;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;
import com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition;

public class CubaCurrencyAddonField extends CubaCurrencyField {

    private CubaPopupButton currencySelector;

    public CubaCurrencyAddonField(CubaTextField textField, CubaPopupButton changeCurrencyButton) {
        super(textField);

        currencySelector = changeCurrencyButton;
        currencySelector.addStyleName(CURRENCY_STYLENAME);

        container.addStyleName(CURRENCY_VISIBLE);
        container.addStyleName(currencyLabelPosition.name().toLowerCase());
        container.removeComponent(currencyLabel);
        container.addComponent(currencySelector);
    }


    @Override
    public void setCurrencyLabelPosition(CurrencyLabelPosition newPosition) {
        CurrencyLabelPosition oldPosition = currencyLabelPosition;
        this.container.removeStyleName(oldPosition.name().toLowerCase());

        container.addStyleName(newPosition.name().toLowerCase());
        container.removeComponent(currencySelector);
        if (CurrencyLabelPosition.LEFT == newPosition) {
            container.addComponent(currencySelector, 0);
        } else {
            container.addComponent(currencySelector, 1);
        }

        currencyLabelPosition = newPosition;
    }
}