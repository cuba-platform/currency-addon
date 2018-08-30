package com.haulmont.addon.currency.web.gui.components;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyValue;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.CubaCurrencyAddonField;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.PopupButton;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class WebCurrencyAddonField extends AbstractWebCurrencyAddonField {

    protected PopupButton changeCurrencyPopupButton = componentsFactory.createComponent(PopupButton.class);

    protected CurrencyService currencyService = AppBeans.get(CurrencyService.NAME);
    protected Currency currency;
    protected Object value;

    public WebCurrencyAddonField() {
        textField.addValueChangeListener(e -> {
            if (value instanceof CurrencyValue) {
                ((CurrencyValue) value).setValue((BigDecimal) e.getValue());
            } else {
                value = e.getValue();
                updateButtonActions(currency);
            }
        });

        this.component = new CubaCurrencyAddonField(textField.unwrap(CubaTextField.class), changeCurrencyPopupButton.unwrap(CubaPopupButton.class));

        com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition currencyLabelPosition =
                com.haulmont.cuba.web.toolkit.ui.CurrencyLabelPosition.valueOf(CurrencyLabelPosition.RIGHT.name());
        this.component.setCurrencyLabelPosition(currencyLabelPosition);
    }


    private void updateButtonActions(Currency currentCurrency) {
        changeCurrencyPopupButton.removeAllActions();

        BigDecimal oldAmount = textField.getValue();

        List<Currency> currencies = currencyService.getAvailableCurrencies();
        for (Currency currency : currencies) {
            if (currency != null && !currency.equals(currentCurrency)) {
                BigDecimal newAmount;
                if (oldAmount != null) {
                    newAmount = currencyService.convertAmountToCurrentRate(oldAmount, currentCurrency, currency);
                } else {
                    newAmount = BigDecimal.ZERO;
                }

                if (newAmount != null) {
                    TransferCurrencyDoNothingAction changeCurrencyAction = new TransferCurrencyDoNothingAction(currency, newAmount);
                    changeCurrencyPopupButton.addAction(changeCurrencyAction);
                }
            }
        }
    }


    @Override
    public void setCurrency(String currencyName) {
        currency = currencyService.getCurrencyByCode(currencyName);
        if (currency != null) {
            changeCurrencyPopupButton.setCaption(currency.getSymbol());
            component.setCurrency(currency.getName());
            updateButtonActions(currency);
        }
        component.setCurrency(currencyName);
    }


    @Override
    public void setDatasource(Datasource datasource, String propertyName) {
        textField.setDatasource(datasource, propertyName);

        if (!DynamicAttributesUtils.isDynamicAttribute(propertyName)) {
            MetaProperty metaProperty = datasource.getMetaClass().getPropertyNN(propertyName);

            Object obj = metaProperty.getAnnotations().get(com.haulmont.cuba.core.entity.annotation.CurrencyValue.class.getName());
            if (obj != null) {
                //noinspection unchecked
                Map<String, Object> currencyValue = (Map<String, Object>) obj;
                String currencyName = (String) currencyValue.get("currency");
                setCurrency(currencyName);
            }
        }
    }


    @Override
    public void setValue(Object value) {
        this.value = value;
        textField.setValue(value);
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
    public <V> V getValue() {
        return (V) value;
    }

}