package com.haulmont.addon.currency.config;

import com.haulmont.cuba.core.config.*;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultLong;
import com.haulmont.cuba.core.config.defaults.DefaultString;

import static com.haulmont.addon.currency.CurrencyGlobalConstants.RateStrategyConstants;

@Source(type = SourceType.DATABASE)
public interface CurrencyConfig extends Config {


    /**
     * Which strategy will be used if rate not found for current date
     */
    @EnumStore(value = EnumStoreMode.ID)
    @Property("addon.currency.ui.rate_strategy")
    @DefaultString(RateStrategyConstants.WARNING)
    RateStrategy getRateStrategy();


    /**
     * Show date of rate which used to convert currency value
     */
    @DefaultBoolean(false)
    @Property("addon.currency.ui.show_used_conversion_rate")
    boolean getShowUsedConversionRateDate();


    long ONE_DAY = 24 * 60 * 60;

    @Property("addon.currency.ui.rate_actual_period_seconds")
    @DefaultLong(ONE_DAY)
    long getRateActualPeriodSeconds();

}
