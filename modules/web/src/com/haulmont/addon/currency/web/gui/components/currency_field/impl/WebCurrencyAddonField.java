package com.haulmont.addon.currency.web.gui.components.currency_field.impl;

import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.format.CurrencyBigDecimalFormat;
import com.haulmont.addon.currency.web.gui.components.currency_field.Side;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.CurrencyValueChangedEventSupplier;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators.AbstractCurrencyButtonPopupContentProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.creators.WriteApplicablePopupProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.CurrencyDataProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.DataSourceCurrencyProvider;
import com.haulmont.addon.currency.web.gui.components.currency_field.impl.currency_switch.providers.InMemoryCurrencyProvider;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.CubaCurrencyAddonField;
import com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield.StringToCurrencyBigDecimalConverter;
import com.haulmont.cuba.gui.components.PopupButton;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.CubaPopupButton;
import com.haulmont.cuba.web.toolkit.ui.CubaTextField;

import java.math.BigDecimal;

public class WebCurrencyAddonField extends AbstractWebCurrencyAddonField implements CurrencyValueChangedEventSupplier {

    protected final PopupButton changeCurrencyPopupButton = componentsFactory.createComponent(PopupButton.class);

    protected CurrencyDataProvider currencyDataProvider;
    protected AbstractCurrencyButtonPopupContentProvider popupContentCreator = new WriteApplicablePopupProvider();


    public WebCurrencyAddonField() {
        component = new CubaCurrencyAddonField(getUnwrapAmountField(), changeCurrencyPopupButton.unwrap(CubaPopupButton.class));

        currencyDataProvider = new InMemoryCurrencyProvider(this);
        popupContentCreator.setDataProvider(currencyDataProvider);

        configurePopupButton();
        configureAmountField(amountField);
    }


    private void configurePopupButton() {
        changeCurrencyPopupButton.setAutoClose(false);
    }


    private void configureAmountField(TextField amountField) {
        configureDataTypeAndConverter(amountField);
        amountField.setValue(BigDecimal.ZERO);
        amountField.addValueChangeListener(valueChangeEvent -> viewChanged());
    }

    public void viewChanged() {
        currencyDataProvider.setAmount((BigDecimal) getUnwrapAmountField().getConvertedValue());
        updatePopupContent();
    }


    protected CubaTextField getUnwrapAmountField() {
        return amountField.unwrap(CubaTextField.class);
    }


    @Override
    public void modelChanged() {
        getUnwrapAmountField().setConvertedValue(currencyDataProvider.getAmount());
        updatePopupContent();
    }


    protected void updatePopupContent() {
        if (popupContentCreator != null) {
            CurrencyDescriptor currency = currencyDataProvider.getCurrency();
            if (currency != null) {
                changeCurrencyPopupButton.setCaption(currency.getSymbol());
            }
            changeCurrencyPopupButton.setPopupContent(popupContentCreator.get());

            //Because currency changed and precision can be changed too
            configureDataTypeAndConverter(amountField);
            getUnwrapAmountField().setConvertedValue(currencyDataProvider.getAmount());
        }
    }


    @Override
    public void setDatasource(Datasource dataSource, String propertyName) {
        initializeAsDs(dataSource, propertyName);
    }


    protected void initializeAsDs(Datasource dataSource, String propertyName) {
        currencyDataProvider = new DataSourceCurrencyProvider(
                dataSource, propertyName, this
        );

        popupContentCreator.setDataProvider(currencyDataProvider);
        configureDataTypeAndConverter(amountField);
    }


    protected void configureDataTypeAndConverter(TextField amountField) {
        int precision = getPrecision();

        StringToCurrencyBigDecimalConverter converter = new StringToCurrencyBigDecimalConverter(precision);
        amountField.unwrap(CubaTextField.class).setConverter(converter);
    }


    protected int getPrecision() {
        int precision = CurrencyBigDecimalFormat.DEFAULT_PRECISION;
        if (currencyDataProvider != null) {
            CurrencyDescriptor currency = currencyDataProvider.getCurrency();
            if (currency != null) {
                precision = currency.getPrecision();
            }
        }
        return precision;
    }


    @Override
    public boolean isWithTime() {
        return popupContentCreator.isWithTime();
    }


    @Override
    public void setCurrencyButtonPosition(Side side) {
        component.setCurrencyLabelPosition(side.getCurrencyLabelPosition());
    }


    @Override
    public Side getCurrencyButtonPosition() {
        return Side.byCurrencyLabelPosition(component.getCurrencyLabelPosition());
    }


    @Override
    public void setWithTime(boolean withTime) {
        popupContentCreator.setWithTime(withTime);
    }


    @Override
    public CurrencyRateAware getCurrencyRateAware() {
        return currencyDataProvider.getItem();
    }


    @Override
    public void setCurrencyRateAware(CurrencyRateAware currencyRateAware) {
        currencyDataProvider.setItem(currencyRateAware);
    }

}