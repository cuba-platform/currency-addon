package com.haulmont.addon.currency.web.gui.components.currency_field.impl;

import com.haulmont.addon.currency.web.gui.components.currency_field.CurrencyAddonField;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.CubaCurrencyAddonField;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebAbstractField;

public abstract class AbstractWebCurrencyAddonField extends WebAbstractField<CubaCurrencyAddonField> implements CurrencyAddonField {

    protected final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);

    protected TextField amountField = componentsFactory.createComponent(TextField.class);


    public AbstractWebCurrencyAddonField() {
    }


    @Override
    public void commit() {
        amountField.commit();
    }


    @Override
    public void discard() {
        amountField.discard();
    }


    @Override
    public boolean isRequired() {
        return amountField.isRequired();
    }


    @Override
    public void setRequired(boolean required) {
        amountField.setRequired(required);
    }


    @Override
    public void setBuffered(boolean buffered) {
        amountField.setBuffered(buffered);
    }


    @Override
    public boolean isBuffered() {
        return amountField.isBuffered();
    }


    @Override
    public boolean isModified() {
        return amountField.isModified();
    }


    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        amountField.addValueChangeListener(listener);
    }


    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        amountField.removeValueChangeListener(listener);
    }


    @Override
    public Datasource getDatasource() {
        return amountField.getDatasource();
    }


    @Override
    public MetaProperty getMetaProperty() {
        return amountField.getMetaProperty();
    }


    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return amountField.getMetaPropertyPath();
    }


    @Override
    public void setCurrencyLabelPosition(CurrencyField.CurrencyLabelPosition currencyLabelPosition) {
        Preconditions.checkNotNullArgument(currencyLabelPosition);

        com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition wAlign =
                com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition.valueOf(currencyLabelPosition.name());

        component.setCurrencyLabelPosition(wAlign);
    }


    @Override
    public CurrencyField.CurrencyLabelPosition getCurrencyLabelPosition() {
        return CurrencyField.CurrencyLabelPosition.valueOf(component.getCurrencyLabelPosition().name());
    }
}
