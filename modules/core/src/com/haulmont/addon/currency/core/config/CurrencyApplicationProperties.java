package com.haulmont.addon.currency.core.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultString;

import static com.haulmont.addon.currency.CurrencyGlobalConstants.CurrencyProperties;

@Source(type = SourceType.APP)
public interface CurrencyApplicationProperties extends Config {

    /**
     * Returns code of default currency which can be converted to CurrencyDescriptor
     */
    @DefaultString(CurrencyProperties.DEFAULT_CURRENCY_CODE_VALUE)
    @Property(CurrencyProperties.DEFAULT_CURRENCY_CODE)
    String getDefaultCurrencyCode();
}
