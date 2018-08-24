package com.haulmont.addon.currency.core.integration_tests;

public class CurrencyIntegrationTestUtil extends IntegrationTestsUtil {
    public static final CurrencyIntegrationTestUtil INSTANCE = new CurrencyIntegrationTestUtil();

    protected String getModuleName() {
        return "currency";
    }
}
