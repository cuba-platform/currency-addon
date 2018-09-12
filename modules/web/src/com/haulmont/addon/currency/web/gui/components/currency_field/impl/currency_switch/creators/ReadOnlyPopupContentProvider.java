package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyValueDataProvider;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.VBoxLayout;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReadOnlyPopupContentProvider extends AbstractCurrencyButtonPopupContentProvider {


    public ReadOnlyPopupContentProvider(CurrencyValueDataProvider dataProvider) {
        super(dataProvider);
    }


    @Override
    protected DateField createDateField() {
        DateField currencyDateField = componentsFactory.createComponent(DateField.class);
        currencyDateField.setEditable(false);
        return currencyDateField;
    }


    @Override
    protected void addCurrencyControls(VBoxLayout layout, Date amountDate) {
        List<CurrencyDescriptor> currencies = currencyService.getAvailableCurrencies();
        for (CurrencyDescriptor targetCurrency : currencies) {
            if (targetCurrency != null && !targetCurrency.equals(dataProvider.getCurrency())) {
                BigDecimal newAmount = calculateNewAmount(amountDate, dataProvider.getAmount(), targetCurrency);

                if (newAmount != null) {
                    Label currencyLabel = componentsFactory.createComponent(Label.class);
                    currencyLabel.setValue(newAmount.stripTrailingZeros().toPlainString() + " " + targetCurrency.getSymbol());
                    layout.add(currencyLabel);
                }
            }
        }
    }
}
