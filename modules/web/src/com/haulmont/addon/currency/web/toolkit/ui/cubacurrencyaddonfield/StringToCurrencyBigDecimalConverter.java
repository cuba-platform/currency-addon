package com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield;

import com.haulmont.addon.currency.format.CurrencyBigDecimalFormat;
import com.haulmont.cuba.core.global.AppBeans;
import com.vaadin.data.util.converter.Converter;

import java.math.BigDecimal;
import java.util.Locale;

public class StringToCurrencyBigDecimalConverter implements Converter<String, BigDecimal> {

    protected final CurrencyBigDecimalFormat currencyFormat = AppBeans.get(CurrencyBigDecimalFormat.class);

    protected final int fractionPrecision;


    public StringToCurrencyBigDecimalConverter(int fractionPrecision) {
        this.fractionPrecision = fractionPrecision;
    }


    @Override
    public BigDecimal convertToModel(String value, Class<? extends BigDecimal> aClass, Locale locale) throws ConversionException {
        return currencyFormat.parse(value, fractionPrecision, locale);
    }


    @Override
    public String convertToPresentation(BigDecimal value, Class<? extends String> aClass, Locale locale) throws ConversionException {
        return currencyFormat.format(value, fractionPrecision, locale);
    }


    @Override
    public Class<BigDecimal> getModelType() {
        return BigDecimal.class;
    }


    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
