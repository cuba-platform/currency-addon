package com.haulmont.addon.currency.web.gui.components;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.CubaCurrencyAddonField;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.annotation.CurrencyValue;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebAbstractField;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;

import java.util.List;
import java.util.Map;

public class WebCurrencyAddonField extends WebAbstractField<CubaCurrencyAddonField> implements CurrencyAddonField {

    protected TextField textField;
    protected PopupButton popupButton;
    protected CurrencyService currencyService;

    public WebCurrencyAddonField() {
        textField = AppBeans.get(ComponentsFactory.class).createComponent(TextField.class);
        popupButton = AppBeans.get(ComponentsFactory.class).createComponent(PopupButton.class);
        currencyService = AppBeans.get(CurrencyService.NAME);

        List<Currency> currencies = currencyService.getAvailableCurrencies();
        for (Currency currency : currencies) {
            popupButton.addAction(new AbstractAction(currency.getCode()) {
                @Override
                public void actionPerform(Component component) {
                    popupButton.setCaption(currency.getSymbol());
                }

                @Override
                public String getCaption() {
                    return currency.getSymbol();
                }
            });
        }

        this.component = new CubaCurrencyAddonField(textField.unwrap(CubaTextField.class), popupButton.unwrap(CubaPopupButton.class));

        com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition currencyLabelPosition =
                com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition.valueOf(CurrencyLabelPosition.RIGHT.name());
        this.component.setCurrencyLabelPosition(currencyLabelPosition);
    }

    @Override
    public void setCurrency(String currency) {
        component.setCurrency(currency);
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

    @Override
    public void setDatasource(Datasource datasource, String property) {
        textField.setDatasource(datasource, property);

        if (datasource != null && !DynamicAttributesUtils.isDynamicAttribute(property)) {
            MetaProperty metaProperty = datasource.getMetaClass().getPropertyNN(property);

            Object obj = metaProperty.getAnnotations().get(CurrencyValue.class.getName());
            if (obj == null)
                return;

            //noinspection unchecked
            Map<String, Object> currencyValue = (Map<String, Object>) obj;
            String currencyName = (String) currencyValue.get("currency");
            component.setCurrency(currencyName);
        }
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
    public void setDatatype(Datatype datatype) {
        Preconditions.checkNotNullArgument(datatype);

        component.setDatatype(datatype);
    }

    @Override
    public Datatype getDatatype() {
        return component.getDatatype();
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
    public <V> V getValue() {
        return textField.getValue();
    }

    @Override
    public void setValue(Object value) {
        textField.setValue(value);
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
    public void commit() {
        textField.commit();
    }

    @Override
    public void discard() {
        textField.discard();
    }

    @Override
    public boolean isModified() {
        return textField.isModified();
    }

    @Override
    public void validate() throws ValidationException {
        if (hasValidationError()) {
            setValidationError(null);
        }

        if (!isVisible() || !isEditableWithParent() || !isEnabled()) {
            return;
        }

        textField.validate();
    }

    @Override
    public boolean isRequired() {
        return textField.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        textField.setRequired(required);
    }
}