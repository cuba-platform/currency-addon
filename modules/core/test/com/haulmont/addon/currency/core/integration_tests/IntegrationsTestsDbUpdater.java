package com.haulmont.addon.currency.core.integration_tests;

import com.haulmont.cuba.core.sys.dbupdate.DbUpdaterEngine;

import javax.sql.DataSource;

public class IntegrationsTestsDbUpdater extends DbUpdaterEngine {

    public IntegrationsTestsDbUpdater(DataSource dataSource, String dbScriptsDirectory, String dbmsType) {
        this.dataSource = dataSource;
        this.dbScriptsDirectory = dbScriptsDirectory ;
        this.dbmsType = dbmsType;
    }

}
