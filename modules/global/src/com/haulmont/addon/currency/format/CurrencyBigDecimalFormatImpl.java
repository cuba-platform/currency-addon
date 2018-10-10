package com.haulmont.addon.currency.format;

import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.cuba.core.global.UserSessionSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.text.*;
import java.util.Locale;

@Component(CurrencyBigDecimalFormat.NAME)
public class CurrencyBigDecimalFormatImpl implements CurrencyBigDecimalFormat {

    @Autowired
    protected FormatStringsRegistry formatStringsRegistry;
    @Autowired
    protected UserSessionSource sessionSource;

    @Override
    public String format(BigDecimal value, int fractionPrecision) {
        Locale locale = sessionSource.getLocale();
        return format(value, fractionPrecision, locale);
    }


    @Override
    public String format(BigDecimal value, int fractionPrecision, @Nullable Locale locale) {
        String result = "";
        if (value != null) {
            NumberFormat format = getFormat(locale, fractionPrecision);
            result = format.format(value);
        }
        return result;
    }


    @Override
    public BigDecimal parse(String value, int fractionPrecision) {
        Locale locale = sessionSource.getLocale();
        return parse(value, fractionPrecision, locale);
    }


    @Override
    public BigDecimal parse(String value, int fractionPrecision, @Nullable Locale locale) {
        BigDecimal result = null;
        if (StringUtils.isNotBlank(value)) {
            NumberFormat format = getFormat(locale, fractionPrecision);

            result = parseBigDecimal(value, format);
        }
        return result;
    }

    //TODO Maybe need to create field cash of formats by locale key
    protected DecimalFormat getFormat(@Nullable Locale locale, int fractionPrecision) {
        Locale actualLocale = getLocale(locale);

        DecimalFormatSymbols symbols = getFormatSymbols(actualLocale);

        String pattern = getFormatPattern(locale);

        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMaximumFractionDigits(fractionPrecision);

        return decimalFormat;
    }


    protected BigDecimal parseBigDecimal(String value, NumberFormat format) {
        BigDecimal result;
        ParsePosition position = new ParsePosition(0);
        result = (BigDecimal) format.parse(value.trim(), position);

        if (position.getIndex() != value.length()) {
            ParseException parseException = new ParseException(String.format("Invalid number: \"%s\"", value), position.getErrorIndex());
            throw new IllegalArgumentException(parseException);
        }
        return result;
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
            actualLocale = sessionSource.getLocale();
        }
        return actualLocale;
    }

}
