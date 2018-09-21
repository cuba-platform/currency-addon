package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyValueDataProvider;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.VBoxLayout;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteApplicablePopupProvider extends AbstractCurrencyButtonPopupContentProvider {

    private final CurrencyValueChangedEventSupplier valueChangedEventSupplier;


    public WriteApplicablePopupProvider(
            CurrencyValueDataProvider dataProvider, CurrencyValueChangedEventSupplier valueChangedEventSupplier, boolean withTime
    ) {
        super(dataProvider, withTime);

        this.valueChangedEventSupplier = valueChangedEventSupplier;
    }


    @Override
    protected DateField createDateField() {
        DateField dateField = componentsFactory.createComponent(DateField.class);
        dateField.addValueChangeListener(e -> dataProvider.setDate((Date) e.getValue()));
        dateField.setResolution(isWithTime() ? DateField.Resolution.MIN : DateField.Resolution.DAY);
        return dateField;
    }


    @Override
    protected void addCurrencyControls(VBoxLayout layout, Date amountDate) {
        List<CurrencyDescriptor> currencies = currencyService.getAvailableCurrencies();
        Map<String, Object> options = new HashMap<>();
        for (CurrencyDescriptor targetCurrency : currencies) {
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
                CurrencyDescriptor newCurrency = (CurrencyDescriptor) e.getValue();

                BigDecimal newAmount = calculateNewAmount(amountDate, dataProvider.getAmount(), newCurrency);

                dataProvider.setCurrency(newCurrency);
                dataProvider.setAmount(newAmount);

                valueChangedEventSupplier.reloadAmount();
            }
        });
    }

}
