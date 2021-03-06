package com.haulmont.addon.currency.core;


import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.addon.currency.entity.CurrencyRateAware;
import com.haulmont.addon.currency.service.ConvertResult;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Set of methods for conversion operations and access to currency addon entities
 */
public interface Currencies {

    String NAME = "curraddon_Currencies";

    /**
     * @return default currency in system
     */
    CurrencyDescriptor getDefaultCurrency();

    /**
     * Return all created currencies (include not active)
     * @return Not null list of currencies
     */
    List<CurrencyDescriptor> getAllCurrencies();


    /**
     * Return created and active currencies
     * @return Not null list of currencies
     */
    List<CurrencyDescriptor> getActiveCurrencies();


    /**
     * Return amount in target currency based on amount in source currency at passed date or earlier
     * @param amount Amount in source currency
     * @param date Date for lookup actual rate conversion from source to target currency (looking for conversion rate for passed date or earlier)
     * @param sourceCurrency Currency for converting from
     * @param targetCurrency Currency for converting to
     * @return Amount in target currency if conversion rate found or null in other case
     *
     * Examples:
     *
     * Existed rates:
     * 10:00 22.08.2018, CUR1 -> CUR2 = x2.0
     *
     * Usage:
     * convertAmount(3.0, (11:00 22.08.2018), CUR1, CUR2) = 6.0
     *
     * convertAmount(3.0, (11:00 22.08.2018), CUR1, CUR3) = null
     * convertAmount(4.0, (9:00 22.08.2018), CUR1, CUR2) = null
     */
    @Nullable
    ConvertResult convertAmount(BigDecimal amount, Date date, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency);


    /**
     * Return amount in target currency based on amount in source currency using current actual rate or earlier
     * @param amount Amount in source currency
     * @param sourceCurrency Source currency for conversion
     * @param targetCurrency Target currency for conversion
     * @return Amount in target currency if conversion rate found or null in other case
     *
     *
     * Examples:
     *
     * Existed rates:
     * 10:00 22.08.2018, CUR1 -> CUR2 = x2.0
     *
     * Now - 11:00 22.08.2018
     *
     * Usage:
     * convertAmountToCurrentRate(4.0, CUR1, CUR2) = 8.0
     *
     * convertAmountToCurrentRate(4.0, CUR1, CUR3) = null
     */
    @Nullable
    ConvertResult convertAmountToCurrentRate(BigDecimal amount, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency);


    /**
     * Return amount in target currency based on info for source currency value
     * @param sourceCurrencyValue Info about converting amount from source currency
     * @param targetCurrency Target currency for conversion
     * @return Amount in target currency if conversion rate found or null in other case
     *
     *
     * Examples:
     *
     * Existed rates:
     * 10:00 22.08.2018, CUR1 -> CUR2 = x2.0
     *
     * Usage:
     * convertAmount({CUR1, 4.0, (11:00 22.08.2018)}, CUR2) = 8.0
     *
     * convertAmount({CUR2, 4.0, (11:00 22.08.2018)}, CUR1) = null
     */
    @Nullable
    ConvertResult convertAmount(CurrencyRateAware sourceCurrencyValue, CurrencyDescriptor targetCurrency);


    /**
     * Looking for created currency in storage by currency code. If currency not found exception will be thrown.
     * @param code Code of currency
     * @return Currency
     */
    CurrencyDescriptor getCurrencyByCode(String code);

    /**
     * Example:
     * convertAmount(USD, GBP) used 0.720940 rate
     * convertAmount(GBP, USD) used 1.387100 rate
     * But 1 / 0.720940 == 1.387077981524121 != 1.387100
     * In order to use the same rate (1.387077981524121) use convertAmountToRateReverse()
     */
    @Nullable
    ConvertResult convertAmountToRateReverse(BigDecimal amount, Date date, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency);


    /**
     * Return rate for conversion from source currency to target currency at passed date (or earlier)
     * @param date Date for looking conversion rate (or earlier)
     * @param sourceCurrency Currency for conversion from
     * @param targetCurrency Currency for conversion to
     * @return Amount in target currency if conversion rate found or null in other case
     */
    @Nullable
    CurrencyRate getLocalRate(Date date, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency);
}
