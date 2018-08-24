package com.haulmont.addon.currency.core.integration_tests;

import com.haulmont.addon.currency.core.integration_tests.parameters.TestDbParameters;
import com.haulmont.cuba.testsupport.TestContainer;

import java.util.ArrayList;
import java.util.Collections;

public class CurrencyTestContainer extends TestContainer {

    private final TestDbParameters dbParameters;

    public CurrencyTestContainer(TestDbParameters dbParameters, String currentModuleName) {
        super();

        appComponents = new ArrayList<>(Collections.singletonList(
                "com.haulmont.cuba"
        ));

        appPropertiesFiles = java.util.Arrays.asList(
                "cuba-app.properties",
                "com/haulmont/addon/" + currentModuleName + "/app.properties",
                "test-app.properties",
                "test.properties"
        );

        this.dbParameters = dbParameters;

        dbDriver = this.dbParameters.getDriverClass();
        dbUrl = this.dbParameters.getDbUrl();
        dbUser = this.dbParameters.getUserName();
        dbPassword = this.dbParameters.getPassword();
    }


    @Override
    protected void before() throws Throwable {
        dbParameters.clean();

        super.before();

        dbParameters.initialize();
    }
}