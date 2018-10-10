package com.haulmont.addon.currency.format;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Locale;

public interface CurrencyBigDecimalFormat {
    String NAME = "curraddon_CurrencyBigDecimalFormat";

    String DEFAULT_PATTERN = "#,##0.#";
    char DEFAULT_DECIMAL_SEPARATOR = '.';
    char DEFAULT_GROUPING_SEPARATOR = ',';

    int DEFAULT_PRECISION = 5;


    /**
     * Locale will be fetched automatically
     * @param value
     * @param fractionPrecision number of digits after fraction delimiter
     * @return
     */
    String format(BigDecimal value, int fractionPrecision);


    String format(BigDecimal value, int fractionPrecision, @Nullable Locale locale);


    /**
     * Locale will be fetched automatically
     * @param value
     * @param fractionPrecision
     * @return
     */
    BigDecimal parse(String value, int fractionPrecision);


    BigDecimal parse(String value, int fractionPrecision, @Nullable Locale locale);
}
