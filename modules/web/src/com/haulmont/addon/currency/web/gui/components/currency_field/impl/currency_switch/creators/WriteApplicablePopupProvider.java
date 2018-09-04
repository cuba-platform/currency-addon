package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.SeparateEntityCurrencyValueDataProvider;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.VBoxLayout;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteApplicablePopupProvider extends AbstractCurrencyButtonPopupContentProvider<SeparateEntityCurrencyValueDataProvider> {

    private final CurrencyValueChangedEventSupplier valueChangedEventSupplier;


    public WriteApplicablePopupProvider(
            SeparateEntityCurrencyValueDataProvider dataProvider, CurrencyValueChangedEventSupplier valueChangedEventSupplier
    ) {
        super(dataProvider);

        this.valueChangedEventSupplier = valueChangedEventSupplier;
    }


    @Override
    protected DateField createDateField() {
        DateField dateField = componentsFactory.createComponent(DateField.class);
        dateField.addValueChangeListener(new Component.ValueChangeListener() {
            @Override
            public void valueChanged(Component.ValueChangeEvent e) {
                dataProvider.setDate((Date) e.getValue());
            }
        });
        return dateField;
    }


    @Override
    protected void addCurrencyControls(VBoxLayout layout, Date amountDate) {
        List<Currency> currencies = currencyService.getAvailableCurrencies();
        Map<String, Object> options = new HashMap<>();
        for (Currency targetCurrency : currencies) {
            if (targetCurrency != null && !targetCurrency.equals(dataProvider.getCurrency())) {
                BigDecimal newAmount = calculateNewAmount(amountDate, dataProvider.getAmount(), targetCurrency);

                if (newAmount != null) {
                    options.put(newAmount.stripTrailingZeros().toPlainString() + " " + targetCurrency.getSymbol(), targetCurrency);
                }
            }
        }

        OptionsGroup optionsGroup = componentsFactory.createComponent(OptionsGroup.class);
        layout.add(optionsGroup);
        optionsGroup.setOptionsMap(options);

        optionsGroup.addValueChangeListener(new Component.ValueChangeListener() {
            @Override
            public void valueChanged(Component.ValueChangeEvent e) {
                Currency newCurrency = (Currency) e.getValue();

                BigDecimal newAmount = calculateNewAmount(amountDate, dataProvider.getAmount(), newCurrency);

                dataProvider.setCurrency(newCurrency);
                dataProvider.setAmount(newAmount);

                valueChangedEventSupplier.reloadAmount();
            }
        });
    }

}