package com.haulmont.addon.currency.web.gui.components.currency_field.impl;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.format.CurrencyBigDecimalFormat;
import com.haulmont.addon.currency.service.CurrencyService;
import com.haulmont.addon.currency.web.CurrencyWebConstants;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators.AbstractCurrencyButtonPopupContentProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators.WriteApplicablePopupProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyValueDataProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.SeparateEntityCurrencyValueDataProvider;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.CubaCurrencyAddonField;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.StringToCurrencyBigDecimalConverter;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.PopupButton;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;

import java.math.BigDecimal;

public class WebCurrencyAddonField extends AbstractWebCurrencyAddonField implements CurrencyValueChangedEventSupplier {

    protected final CurrencyService currencyService = AppBeans.get(CurrencyService.NAME);

    protected final PopupButton changeCurrencyPopupButton;

    protected CurrencyValueDataProvider currencyValueDataProvider;
    protected AbstractCurrencyButtonPopupContentProvider popupContentCreator;

    protected boolean withTime = CurrencyWebConstants.CURRENCY_DATE_WITH_TIME_DEFAULT;


    public WebCurrencyAddonField() {
        changeCurrencyPopupButton = componentsFactory.createComponent(PopupButton.class);

        component = new CubaCurrencyAddonField(amountField.unwrap(CubaTextField.class), changeCurrencyPopupButton.unwrap(CubaPopupButton.class));

        //Need to configure twice.
        //First - immediately after creating field because setConverter called in constructor and first field rendering run immediately
        configureNewDataTypeAndConverter(amountField);
    }


    public void updatePopupContent() {
        if (popupContentCreator != null) {
            CurrencyDescriptor currency = currencyValueDataProvider.getCurrency();
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
    public void setDatasource(Datasource datasource, String propertyName) {
        popupContentCreator = createPopupContentCreator(datasource, propertyName);
    }


    private AbstractCurrencyButtonPopupContentProvider createPopupContentCreator(Datasource datasource, String propertyName) {
        AbstractCurrencyButtonPopupContentProvider popupContentCreator = null;
        if (!DynamicAttributesUtils.isDynamicAttribute(propertyName)) {

            currencyValueDataProvider = new SeparateEntityCurrencyValueDataProvider(
                    datasource, propertyName, this
            );
            popupContentCreator = createWriteApplicableContentCreator(datasource, propertyName, currencyValueDataProvider);
            amountField.setDatasource(datasource, propertyName + "." + CurrencyRateAware.VALUE_PATH);
            amountField.addValueChangeListener(e -> updatePopupContent());

            //Need to configure twice.
            //Second - after setting data source because because setConverter called in setDatasource method
            configureNewDataTypeAndConverter(amountField);
        }
        return popupContentCreator;
    }

    private void configureNewDataTypeAndConverter(TextField amountField) {
        int fractionPrecision = CurrencyBigDecimalFormat.DEFAULT_PRECISION;
        amountField.unwrap(CubaTextField.class).setConverter(new StringToCurrencyBigDecimalConverter(fractionPrecision));
    }


    private AbstractCurrencyButtonPopupContentProvider createWriteApplicableContentCreator(
            Datasource datasource, String currencyFieldPropertyName, CurrencyValueDataProvider currencyValueDataProvider
    ) {
        AbstractCurrencyButtonPopupContentProvider popupContentCreator = new WriteApplicablePopupProvider(currencyValueDataProvider, this, withTime);
        changeCurrencyPopupButton.setAutoClose(false);

        amountField.addValueChangeListener(e -> currencyValueDataProvider.setAmount((BigDecimal) e.getValue()));

        //On load item to DS
        datasource.addItemChangeListener(event -> {
            Entity item = event.getItem();
            CurrencyRateAware addonCurrencyField = item.getValue(currencyFieldPropertyName);

            CurrencyDescriptor currencyDescriptor = null;
            if (addonCurrencyField != null) {
                currencyDescriptor = addonCurrencyField.getCurrency();
            }

            if (currencyDescriptor == null) {
                currencyDescriptor = currencyService.getDefaultCurrency();
                currencyValueDataProvider.setCurrency(currencyDescriptor);
            }
        });

        return popupContentCreator;
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


    @Override
    public boolean isWithTime() {
        return withTime;
    }


    @Override
    public void setWithTime(boolean withTime) {
        this.withTime = withTime;
    }

}