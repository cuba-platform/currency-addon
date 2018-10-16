package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators;

import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyValueDataProvider;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.util.Date;

public abstract class AbstractCurrencyButtonPopupContentProvider {

    protected final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
    protected final CurrencyService currencyService = AppBeans.get(CurrencyService.class);

    protected final CurrencyValueDataProvider dataProvider;


    private final VBoxLayout layout;
    private final DateField currencyDateField;
    private final boolean withTime;


    public AbstractCurrencyButtonPopupContentProvider(CurrencyValueDataProvider dataProvider, boolean withTime) {
        this.dataProvider = dataProvider;
        this.withTime = withTime;

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


    protected abstract void addCurrencyControls(VBoxLayout layout, Date amountDate);


    protected abstract DateField createDateField();


    public boolean isWithTime() {
        return withTime;
    }
}
