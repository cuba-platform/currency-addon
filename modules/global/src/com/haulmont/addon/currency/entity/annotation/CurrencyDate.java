package com.haulmont.addon.currency.entity.annotation;

import com.haulmont.cuba.core.entity.annotation.MetaAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation can specify date for getting transfer rate. <br/>
 * Used only with {@link com.haulmont.cuba.core.entity.annotation.CurrencyValue}
 * </p>
 *
 * <p>
 * Supports annotation {@link java.util.Date} field
 * </p>
 *
 *
 * Example:
 *
 * @CurrencyValue(currency = "USD")
 * @CurrencyDate("orderTransactionDate")
 * BigDecimal orderAmount;
 *
 * Date orderTransactionDate;
 *
 *
 * So for transfer amount from field 'orderAmount' to other currencies will be used date specified in field 'orderTransactionDate' or current date if it will be NULL
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@MetaAnnotation
public @interface CurrencyDate {
    /**
     * Name of field with date value at the same entity for getting transfer rate
     */
    String value();
}
