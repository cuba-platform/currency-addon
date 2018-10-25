package com.haulmont.addon.currency.format;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Locale;

public interface CurrencyBigDecimalFormat {
    String NAME = "curraddon_CurrencyBigDecimalFormat";

    String DEFAULT_PATTERN = "#,##0.#";
    char DEFAULT_DECIMAL_SEPARATOR = '.';
    char DEFAULT_GROUPING_SEPARATOR = ',';

    int DEFAULT_PRECISION = 2;


    /**
     * Locale will be fetched automatically
     * @param amount currency amount
     * @param fractionPrecision number of digits after fraction delimiter, different currencies has different precision
     * @return formatted amount
     */
    String format(BigDecimal amount, int fractionPrecision);


    /**
     * Locale will be fetched automatically
     * @param amount currency amount
     * @param fractionPrecision number of digits after fraction delimiter, different currencies has different precision
     * @param locale user's locale for determining fraction separator, format, etc
     * @return formatted amount
     */
    String format(BigDecimal amount, int fractionPrecision, @Nullable Locale locale);


    /**
     * Locale will be fetched automatically
     * @param amount formatted currency amount
     * @param fractionPrecision number of digits after fraction delimiter, different currencies has different precision
     * @return amount
     */
    BigDecimal parse(String amount, int fractionPrecision);


    /**
     * Locale will be fetched automatically
     * @param amount formatted currency amount
     * @param fractionPrecision number of digits after fraction delimiter, different currencies has different precision
     * @param locale user's locale for determining fraction separator, format, etc
     * @return amount
     */
    BigDecimal parse(String amount, int fractionPrecision, @Nullable Locale locale);
}
