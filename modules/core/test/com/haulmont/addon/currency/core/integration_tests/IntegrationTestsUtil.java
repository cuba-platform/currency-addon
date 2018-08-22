package com.haulmont.addon.currency.core.integration_tests;

import com.haulmont.addon.currency.core.integration_tests.parameters.HsqlTestDbParameters;
import com.haulmont.cuba.testsupport.TestContainer;

public abstract class IntegrationTestsUtil {

    private static TestContainer testContainer;



    public synchronized TestContainer initializeHsqlDatabaseAndApplication() {
        if (testContainer == null) {
            testContainer = new CurrencyTestContainer(new HsqlTestDbParameters(), getModuleName());
        }
        return testContainer;
    }


    protected abstract String getModuleName();
}
