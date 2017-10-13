/**
 * Copyright 2015-2017 Sinoregal.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.flyway.core.dbsupport.sinodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.dbsupport.DbSupportFactory;
import com.googlecode.flyway.core.dbsupport.JdbcTemplate;
import com.googlecode.flyway.core.util.jdbc.JdbcUtils;
import com.googlecode.flyway.core.util.logging.Log;
import com.googlecode.flyway.core.util.logging.LogFactory;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;

/**
 * Test to demonstrate the migration functionality using H2.
 */
@SuppressWarnings({"JavaDoc"})
public abstract class SinoDBConcurrentMigrationTestCase {
    private static final Log LOG = LogFactory.getLog(SinoDBConcurrentMigrationTestCase.class);

    /**
     * The number of threads to use in this test.
     */
    private static final int NUM_THREADS = 10;

    /**
     * The quoted schema placeholder for the tests.
     */
    private String schemaQuoted;

    /**
     * Flag to indicate the concurrent test has failed.
     */
    private boolean failed;

    /**
     * The datasource to use for concurrent migration tests.
     */
    private DataSource concurrentMigrationDataSource;

    /**
     * The instance under test.
     */
    private Flyway flyway;

    @Before
    public void setUp() throws Exception {
        File customPropertiesFile = new File(System.getProperty("user.home") + "/flyway-mediumtests.properties");
        Properties customProperties = new Properties();
        if (customPropertiesFile.canRead()) {
            customProperties.load(new FileInputStream(customPropertiesFile));
        }
        concurrentMigrationDataSource = createDataSource(customProperties);

        Connection connection = concurrentMigrationDataSource.getConnection();
        schemaQuoted = DbSupportFactory.createDbSupport(connection).quote("informixadmin");
        connection.close();

        flyway = createFlyway();
        flyway.clean();
        flyway.init();
    }

    /**
     * Creates the datasource for this testcase based on these optional custom properties from the user home.
     *
     * @param customProperties The optional custom properties.
     * @return The new datasource.
     */
    protected abstract DataSource createDataSource(Properties customProperties) throws Exception;

    @Test
    public void migrateConcurrently() throws Exception {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    createFlyway().migrate();
                } catch (Exception e) {
                    LOG.error("Migrate failed", e);
                    failed = true;
                }
            }
        };

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(runnable);
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].start();
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }

        assertFalse(failed);
        //SinoDB init 不需要创建Schema。 所以table: schema_version 中 没有Type='SCHEMA'的记录
        assertEquals(5, flyway.info().applied().length);
        assertEquals("2.0", flyway.info().current().getVersion().toString());
        assertEquals(0, flyway.migrate());

        Connection connection = null;
        try {
            connection = concurrentMigrationDataSource.getConnection();
            assertEquals(2, new JdbcTemplate(connection, 0).queryForInt(
                    "SELECT COUNT(*) FROM " +schemaQuoted + ".test_user"));
        } finally {
            JdbcUtils.closeConnection(connection);
        }
    }

    private Flyway createFlyway() throws SQLException {
        Flyway newFlyway = new Flyway();
        newFlyway.setDataSource(concurrentMigrationDataSource);
        newFlyway.setLocations("migration/concurrent_sinodb");
        newFlyway.setSchemas("informixadmin");

        Map<String, String> placeholders = new HashMap<String, String>();
        placeholders.put("schema", schemaQuoted);

        newFlyway.setPlaceholders(placeholders);
        newFlyway.setInitVersion("0.1");
        return newFlyway;
    }
}
