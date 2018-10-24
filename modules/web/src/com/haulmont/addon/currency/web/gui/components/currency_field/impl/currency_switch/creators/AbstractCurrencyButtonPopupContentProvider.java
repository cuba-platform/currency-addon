package com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators;

import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.CurrencyWebConstants;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyDataProvider;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.util.Date;

public abstract class AbstractCurrencyButtonPopupContentProvider {

    protected final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
    protected final CurrencyService currencyService = AppBeans.get(CurrencyService.class);

    protected CurrencyDataProvider dataProvider;


    private final VBoxLayout layout;
    protected final DateField currencyDateField;
    private boolean withTime = CurrencyWebConstants.CURRENCY_DATE_WITH_TIME_DEFAULT;


    public AbstractCurrencyButtonPopupContentProvider() {
        layout = createLayout();
        currencyDateField = createDateField();
    }


    public void setDataProvider(CurrencyDataProvider dataProvider) {
        this.dataProvider = dataProvider;
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


    public void setWithTime(boolean withTime) {
        this.withTime = withTime;
    }
}
