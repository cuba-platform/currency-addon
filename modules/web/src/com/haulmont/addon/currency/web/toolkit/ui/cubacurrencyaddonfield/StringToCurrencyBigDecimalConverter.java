package com.haulmont.addon.currency.web.toolkit.ui.cubacurrencyaddonfield;

import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.cuba.core.global.AppBeans;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinSession;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.text.*;
import java.util.Locale;

public class StringToCurrencyBigDecimalConverter implements Converter<String, BigDecimal> {

    public static final String DEFAULT_PATTERN = "#,##0.#";
    public static final char DEFAULT_DECIMAL_SEPARATOR = '.';
    public static final char DEFAULT_GROUPING_SEPARATOR = ',';
    public static final int DEFAULT_PRECISION = 5;

    protected final FormatStringsRegistry formatStringsRegistry = AppBeans.get(FormatStringsRegistry.class);

    protected final int fractionPrecision;


    public StringToCurrencyBigDecimalConverter(int fractionPrecision) {
        this.fractionPrecision = fractionPrecision;
    }


    @Override
    public BigDecimal convertToModel(String value, Class<? extends BigDecimal> aClass, Locale locale) throws ConversionException {
        BigDecimal result = null;
        if (StringUtils.isNotBlank(value)) {
            NumberFormat format = getFormat(locale);

            result = parseBigDecimal(value, format);
        }
        return result;
    }


    private BigDecimal parseBigDecimal(String value, NumberFormat format) {
        BigDecimal result;
        ParsePosition position = new ParsePosition(0);
        result = (BigDecimal) format.parse(value.trim(), position);

        if (position.getIndex() != value.length()) {
            ParseException parseException = new ParseException(String.format("Invalid number: \"%s\"", value), position.getErrorIndex());
            throw new ConversionException(parseException);
        }
        return result;
    }


    @Override
    public String convertToPresentation(BigDecimal value, Class<? extends String> aClass, Locale locale) throws ConversionException {
        String result = "";
        if (value != null) {
            NumberFormat format = getFormat(locale);
            result = format.format(value);
        }
        return result;
    }


    //TODO Maybe need to create field cash of formats by locale key
    protected DecimalFormat getFormat(@Nullable Locale locale) {
        Locale actualLocale = getLocale(locale);

        DecimalFormatSymbols symbols = getFormatSymbols(actualLocale);

        String pattern = getFormatPattern(locale);

        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMaximumFractionDigits(fractionPrecision);

        return decimalFormat;
    }


    private String getFormatPattern(Locale locale) {
        String pattern;
        if (locale != null && formatStringsRegistry.getFormatStrings(locale) != null) {
            pattern = formatStringsRegistry.getFormatStrings(locale).getDecimalFormat();
        } else {
            pattern = DEFAULT_PATTERN;
        }
        return pattern;
    }


    protected DecimalFormatSymbols getFormatSymbols(Locale locale) {
        DecimalFormatSymbols decimalFormatSymbols;
        if (locale != null && formatStringsRegistry.getFormatStrings(locale) != null) {
            FormatStrings formatStrings = formatStringsRegistry.getFormatStrings(locale);
            decimalFormatSymbols = formatStrings.getFormatSymbols();
        } else {
            decimalFormatSymbols = getDefaultFormatSymbols();
        }
        return decimalFormatSymbols;
    }


    protected DecimalFormatSymbols getDefaultFormatSymbols() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(DEFAULT_DECIMAL_SEPARATOR);
        decimalFormatSymbols.setGroupingSeparator(DEFAULT_GROUPING_SEPARATOR);
        return decimalFormatSymbols;
    }


    private Locale getLocale(@Nullable Locale locale) {
        Locale actualLocale = locale;
        if (actualLocale == null) {
            actualLocale = VaadinSession.getCurrent().getLocale();
        }
        return actualLocale;
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
