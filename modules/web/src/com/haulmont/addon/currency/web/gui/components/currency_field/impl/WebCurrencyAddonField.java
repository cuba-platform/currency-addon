package com.haulmont.addon.currency.web.gui.components.currency_field.impl;

import com.haulmont.addon.currency.entity.AddonCurrencyValue;
import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators.AbstractCurrencyButtonPopupContentProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators.ReadOnlyPopupContentProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators.WriteApplicablePopupProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyValueDataProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.SeparateEntityCurrencyValueDataProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.SimpleSameEntityCurrencyValueDataProvider;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.CubaCurrencyAddonField;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.CurrencyValue;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.PopupButton;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;

import java.math.BigDecimal;

public class WebCurrencyAddonField extends AbstractWebCurrencyAddonField implements CurrencyValueChangedEventSupplier {

    protected final CurrencyService currencyService = AppBeans.get(CurrencyService.NAME);

    protected final PopupButton changeCurrencyPopupButton;

    protected CurrencyValueDataProvider currencyValueDataProvider;
    private AbstractCurrencyButtonPopupContentProvider popupContentCreator;


    public WebCurrencyAddonField() {
        changeCurrencyPopupButton = componentsFactory.createComponent(PopupButton.class);

        component = new CubaCurrencyAddonField(amountField.unwrap(CubaTextField.class), changeCurrencyPopupButton.unwrap(CubaPopupButton.class));
    }


    public void updatePopupContent() {
        if (popupContentCreator != null) {
            Currency currency = currencyValueDataProvider.getCurrency();
            if (currency != null) {
                changeCurrencyPopupButton.setCaption(currency.getSymbol());
            }
            changeCurrencyPopupButton.setPopupContent(popupContentCreator.get());
        }
    }


    @Override
    public void reloadAmount() {
        amountField.setValue(currencyValueDataProvider.getAmount());
    }


    @Override
    public void setCurrency(String currencyCode) {
        Currency currency = currencyService.getCurrencyByCode(currencyCode);
        if (currency != null) {
            updatePopupContent();
        }
        currencyValueDataProvider.setCurrency(currency);
    }


    @Override
    public void setDatasource(Datasource datasource, String propertyName) {
        popupContentCreator = createPopupContentCreator(datasource, propertyName);
    }


    private AbstractCurrencyButtonPopupContentProvider createPopupContentCreator(Datasource datasource, String propertyName) {
        AbstractCurrencyButtonPopupContentProvider popupContentCreator = null;
        if (!DynamicAttributesUtils.isDynamicAttribute(propertyName)) {
            MetaProperty metaProperty = datasource.getMetaClass().getPropertyNN(propertyName);
            Class<?> fieldClass = metaProperty.getJavaType();

            if (AddonCurrencyValue.class.isAssignableFrom(fieldClass)) {
                SeparateEntityCurrencyValueDataProvider currencyValueDataProvider = new SeparateEntityCurrencyValueDataProvider(
                        datasource, propertyName, this
                );
                this.currencyValueDataProvider = currencyValueDataProvider;
                popupContentCreator = createWriteApplicableContentCreator(datasource, propertyName, currencyValueDataProvider);
                amountField.setDatasource(datasource, propertyName + "." + AddonCurrencyValue.VALUE_PATH);
            } else {
                CurrencyValue currencyValueAnnotation = metaProperty.getAnnotatedElement().getDeclaredAnnotation(CurrencyValue.class);
                if (currencyValueAnnotation != null) {
                    popupContentCreator = createReadOnlyContentCreator(datasource, metaProperty, currencyValueAnnotation);
                    amountField.setDatasource(datasource, propertyName);
                } else {
                    throw new RuntimeException(
                            "Not applicable addon currency field component for field " + datasource.getMetaClass().getJavaClass() + "." + propertyName
                    );
                }
            }
            amountField.addValueChangeListener(e -> updatePopupContent());
        }
        return popupContentCreator;
    }


    private AbstractCurrencyButtonPopupContentProvider createWriteApplicableContentCreator(
            Datasource datasource, String propertyName, SeparateEntityCurrencyValueDataProvider currencyValueDataProvider
    ) {
        AbstractCurrencyButtonPopupContentProvider popupContentCreator;
        popupContentCreator = new WriteApplicablePopupProvider(currencyValueDataProvider, this);
        changeCurrencyPopupButton.setAutoClose(false);

        amountField.addValueChangeListener(e -> currencyValueDataProvider.setAmount((BigDecimal) e.getValue()));

        //On load item to DS
        datasource.addItemChangeListener(event -> {
            Entity item = event.getItem();
            AddonCurrencyValue addonCurrencyField = item.getValue(propertyName);

            Currency currency = null;
            if (addonCurrencyField != null) {
                currency = addonCurrencyField.getCurrency();
            }

            if (currency == null) {
                currency = currencyService.getDefaultCurrency();
                currencyValueDataProvider.setCurrency(currency);
            }
        });

        return popupContentCreator;
    }


    private AbstractCurrencyButtonPopupContentProvider createReadOnlyContentCreator(
            Datasource datasource, MetaProperty metaProperty, CurrencyValue currencyValueAnnotation
    ) {
        String currencyName = currencyValueAnnotation.currency();

        currencyValueDataProvider = new SimpleSameEntityCurrencyValueDataProvider(amountField);

        setCurrency(currencyName);

        datasource.addItemChangeListener(e -> updatePopupContent());
        return new ReadOnlyPopupContentProvider(currencyValueDataProvider);
    }


    @Override
    public void setValue(Object value) {
        amountField.setValue(value);
    }


    @Override
    public void validate() throws ValidationException {
        if (hasValidationError()) {
            setValidationError(null);
        }

        if (!isVisible() || !isEditableWithParent() || !isEnabled()) {
            return;
        }

        amountField.validate();
    }


    @Override
    public <V> V getValue() {
        return (V) amountField.getValue();
    }

}