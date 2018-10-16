package com.haulmont.addon.currency.config;

import com.haulmont.cuba.core.config.*;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultString;

@Source(type = SourceType.DATABASE)
public interface CurrencyConfig extends Config {


    /**
     * Which strategy will be used if rate not found for current date
     */
    @EnumStore(value = EnumStoreMode.ID)
    @Property("addon.currency.ui.rate_redundancy")
    @DefaultString("SAME_DATE_REQUIRED")
    RateRedundancy getRateRedundancy();


    /**
     * Show date of rate which used to convert currency value
     */
    @DefaultBoolean(false)
    @Property("addon.currency.ui.show_used_conversion_rate")
    boolean getShowUsedConversionRateDate();

}
