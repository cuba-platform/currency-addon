package com.haulmont.addon.currency.core.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

@Source(type = SourceType.APP)
public interface CurrencyApplicationProperties extends Config {

    /**
     * Returns code of default currency which can be converted to CurrencyDescriptor
     */
    @Property("default_currency_code")
    String getDefaultCurrencyCode();
}
