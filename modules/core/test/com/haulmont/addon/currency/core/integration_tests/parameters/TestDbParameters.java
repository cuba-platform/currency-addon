package com.haulmont.addon.currency.core.integration_tests.parameters;

import java.io.IOException;

public interface TestDbParameters {

    String geDbType();

    String getDriverClass();

    String getDbUrl();

    String getUserName();

    String getPassword();

    void initialize();

    void clean() throws IOException;
}
