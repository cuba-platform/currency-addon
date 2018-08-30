package com.haulmont.addon.currency.web.gui.components;

import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.CubaCurrencyAddonField;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebAbstractField;

public abstract class AbstractWebCurrencyAddonField extends WebAbstractField<CubaCurrencyAddonField> implements CurrencyAddonField {

    protected final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);

    protected TextField textField;


    public AbstractWebCurrencyAddonField() {
        textField = componentsFactory.createComponent(TextField.class);
    }


    @Override
    public void commit() {
        textField.commit();
    }

    @Override
    public void discard() {
        textField.discard();
    }

    @Override
    public boolean isRequired() {
        return textField.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        textField.setRequired(required);
    }

    @Override
    public void setBuffered(boolean buffered) {
        textField.setBuffered(buffered);
    }

    @Override
    public boolean isBuffered() {
        return textField.isBuffered();
    }


    @Override
    public boolean isModified() {
        return textField.isModified();
    }


    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        textField.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        textField.removeValueChangeListener(listener);
    }


    @Override
    public Datasource getDatasource() {
        return textField.getDatasource();
    }


    @Override
    public MetaProperty getMetaProperty() {
        return textField.getMetaProperty();
    }


    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return textField.getMetaPropertyPath();
    }


    @Override
    public String getCurrency() {
        return component.getCurrency();
    }


    @Override
    public void setShowCurrencyLabel(boolean showCurrencyLabel) {
        component.setShowCurrencyLabel(showCurrencyLabel);
    }


    @Override
    public boolean getShowCurrencyLabel() {
        return component.getShowCurrencyLabel();
    }


    @Override
    public void setDatatype(Datatype datatype) {
        Preconditions.checkNotNullArgument(datatype);

        component.setDatatype(datatype);
    }


    @Override
    public Datatype getDatatype() {
        return component.getDatatype();
    }

    @Override
    public void setCurrencyLabelPosition(CurrencyLabelPosition currencyLabelPosition) {
        Preconditions.checkNotNullArgument(currencyLabelPosition);

        com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition wAlign =
                com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition.valueOf(currencyLabelPosition.name());

        component.setCurrencyLabelPosition(wAlign);
    }

    @Override
    public CurrencyLabelPosition getCurrencyLabelPosition() {
        return CurrencyLabelPosition.valueOf(component.getCurrencyLabelPosition().name());
    }
}
