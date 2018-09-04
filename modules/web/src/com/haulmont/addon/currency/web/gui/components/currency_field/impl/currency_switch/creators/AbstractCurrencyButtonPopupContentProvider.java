package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyValueDataProvider;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.math.BigDecimal;
import java.util.Date;

public abstract class AbstractCurrencyButtonPopupContentProvider<DataProviderType extends CurrencyValueDataProvider> {

    protected final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
    protected final CurrencyService currencyService = AppBeans.get(CurrencyService.class);

    protected final DataProviderType dataProvider;


    private final VBoxLayout layout;
    private final DateField currencyDateField;


    public AbstractCurrencyButtonPopupContentProvider(DataProviderType dataProvider) {
        this.dataProvider = dataProvider;

        layout = createLayout();
        currencyDateField = createDateField();
    }



    /**
     * Return popup content component
     */
    public Component get() {
        layout.removeAll();

        layout.add(currencyDateField);

        Date amountDate = dataProvider.getDate();
        currencyDateField.setValue(amountDate);

        addCurrencyControls(layout, amountDate);

        return layout;
    }


    private VBoxLayout createLayout() {
        VBoxLayout layout = componentsFactory.createComponent(VBoxLayout.class);
        layout.setSpacing(true);
        return layout;
    }


    protected BigDecimal calculateNewAmount(Date amountDate, BigDecimal oldAmount, Currency targetCurrency) {
        BigDecimal newAmount;
        if (oldAmount != null) {
            newAmount = currencyService.convertAmountToRate(oldAmount, amountDate, dataProvider.getCurrency(), targetCurrency);
        } else {
            newAmount = BigDecimal.ZERO;
        }
        return newAmount;
    }


    protected abstract void addCurrencyControls(VBoxLayout layout, Date amountDate);


    protected abstract DateField createDateField();


}
