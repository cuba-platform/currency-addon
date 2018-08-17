package com.haulmont.addon.currency.core.impl;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultString;

@Source(type = SourceType.DATABASE)
public interface FixerIOConfig extends Config {
    String REST_API_KEY_ID = "addon.currency.fixerIO.apiKey";

    @Property(REST_API_KEY_ID)
    @DefaultString("")
    String getApiKey();
}
