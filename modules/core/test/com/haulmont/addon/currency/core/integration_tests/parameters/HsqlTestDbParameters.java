package com.haulmont.addon.currency.core.integration_tests.parameters;

import com.haulmont.addon.currency.core.integration_tests.IntegrationsTestsDbUpdater;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.DbInitializationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

public class HsqlTestDbParameters implements TestDbParameters {
    private static final Logger LOG = LoggerFactory.getLogger(HsqlTestDbParameters.class);

    @Override
    public String geDbType() {
        return "hsql";
    }

    @Override
    public String getDriverClass() {
        return "org.hsqldb.jdbc.JDBCDriver";
    }

    public String getDatabaseFilesLocation() {
        return getIntegrationTestDbFolder() + "/testdb";
    }


    private String getIntegrationTestDbFolder() {
        return "integration_tests_db";
    }


    @Override
    public String getDbUrl() {
        return "jdbc:hsqldb:file:" + getDatabaseFilesLocation();
    }

    @Override
    public String getUserName() {
        return "sa";
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public void initialize() {
        IntegrationsTestsDbUpdater engine = new IntegrationsTestsDbUpdater(AppBeans.get(DataSource.class), "build/db", geDbType());
        try {
            engine.updateDatabase();
        } catch (DbInitializationException e) {
            throw new RuntimeException("Update DB failed", e);
        }
    }

    @Override
    public void clean() throws IOException {
        String directory = getIntegrationTestDbFolder();

        File directoryWithDatabaseFiles = new File(directory);
        LOG.info("Try to delete test DB {}, exist: {}", directoryWithDatabaseFiles.getAbsolutePath(), directoryWithDatabaseFiles.exists());
        FileUtils.deleteDirectory(directoryWithDatabaseFiles);
    }

}
