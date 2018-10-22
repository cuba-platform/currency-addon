package com.haulmont.addon.currency;

public interface CurrencyGlobalConstants {
    int CURRENCY_PRECISION = 19;

    interface RateStrategyConstants {
        String REQUIRED = "REQUIRED";
        String WARNING = "WARNING";
        String LAST_AVAILABLE = "LAST_AVAILABLE";
    }

    interface CurrencyProperties {
        String DEFAULT_CURRENCY_CODE = "default_currency_code";
        String DEFAULT_CURRENCY_CODE_VALUE = "USD";
    }
}
