package com.haulmont.addon.currency.core.impl;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.defaults.DefaultString;

public interface FixerIOConfig extends Config {

    @Property("addon.currency.fixerIO.apiKey")
    @DefaultString("")
    String getApiKey();
}
