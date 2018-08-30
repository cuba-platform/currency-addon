package com.haulmont.addon.currency.web.gui.components;

import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyValue;
import com.haulmont.addon.currency.entity.annotation.CurrencyDate;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.CubaCurrencyAddonField;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.PopupButton;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WebCurrencyAddonField extends AbstractWebCurrencyAddonField {

    protected final CurrencyService currencyService = AppBeans.get(CurrencyService.NAME);

    protected final PopupButton changeCurrencyPopupButton;

    protected Currency currentCurrency;
    protected Object value;
    protected String amountDateFieldName;


    public WebCurrencyAddonField() {
        changeCurrencyPopupButton = componentsFactory.createComponent(PopupButton.class);

        textField.addValueChangeListener(e -> {
            if (value instanceof CurrencyValue) {
                ((CurrencyValue) value).setValue((BigDecimal) e.getValue());
            } else {
                value = e.getValue();
                amountChanged();
            }
        });

        component = new CubaCurrencyAddonField(textField.unwrap(CubaTextField.class), changeCurrencyPopupButton.unwrap(CubaPopupButton.class));
    }


    public void amountChanged() {
        updateButtonActions();
    }


    public void amountDateChanged() {
        updateButtonActions();
    }


    private void updateButtonActions() {
        changeCurrencyPopupButton.removeAllActions();

        BigDecimal oldAmount = textField.getValue();

        List<Currency> currencies = currencyService.getAvailableCurrencies();
        for (Currency targetCurrency : currencies) {
            if (targetCurrency != null && !targetCurrency.equals(currentCurrency)) {
                BigDecimal newAmount = calculateNewAmount(oldAmount, targetCurrency);

                if (newAmount != null) {
                    TransferCurrencyDoNothingAction changeCurrencyAction = new TransferCurrencyDoNothingAction(targetCurrency, newAmount);
                    changeCurrencyPopupButton.addAction(changeCurrencyAction);
                }
            }
        }
    }


    private BigDecimal calculateNewAmount(BigDecimal oldAmount, Currency targetCurrency) {
        BigDecimal newAmount;
        if (oldAmount != null) {
            Date amountDate = getDateForTransferRate();

            if (amountDate != null) {
                newAmount = currencyService.convertAmountToRate(oldAmount, amountDate, currentCurrency, targetCurrency);
            } else {
                newAmount = currencyService.convertAmountToCurrentRate(oldAmount, currentCurrency, targetCurrency);
            }
        } else {
            newAmount = BigDecimal.ZERO;
        }
        return newAmount;
    }


    private Date getDateForTransferRate() {
        Date amountDate = null;
        if (amountDateFieldName != null) {
            Entity item = getDatasource().getItem();
            amountDate = item.getValue(amountDateFieldName);
        }
        return amountDate;
    }


    @Override
    public void setCurrency(String currencyName) {
        currentCurrency = currencyService.getCurrencyByCode(currencyName);
        String currencyLine = currencyName;
        if (currentCurrency != null) {
            changeCurrencyPopupButton.setCaption(currentCurrency.getSymbol());
            currencyLine = currentCurrency.getName();
            updateButtonActions();
        }
        component.setCurrency(currencyLine);
    }


    @Override
    public void setDatasource(Datasource datasource, String propertyName) {
        textField.setDatasource(datasource, propertyName);

        if (!DynamicAttributesUtils.isDynamicAttribute(propertyName)) {
            MetaProperty metaProperty = datasource.getMetaClass().getPropertyNN(propertyName);

            com.haulmont.cuba.core.entity.annotation.CurrencyValue currencyValueAnnotation =
                    metaProperty.getAnnotatedElement().getDeclaredAnnotation(com.haulmont.cuba.core.entity.annotation.CurrencyValue.class);
            if (currencyValueAnnotation != null) {
                String currencyName = currencyValueAnnotation.currency();
                setCurrency(currencyName);
            }

            CurrencyDate currencyDateAnnotation = metaProperty.getAnnotatedElement().getDeclaredAnnotation(CurrencyDate.class);
            if (currencyDateAnnotation != null) {
                amountDateFieldName = currencyDateAnnotation.value();
            }
        }

        if (amountDateFieldName != null) {
            datasource.addItemPropertyChangeListener(new CurrencyAmountDateChangedListener(amountDateFieldName, this));
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