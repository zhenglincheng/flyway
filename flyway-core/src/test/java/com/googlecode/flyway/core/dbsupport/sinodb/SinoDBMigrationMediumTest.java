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

import com.googlecode.flyway.core.DbCategory;
import com.googlecode.flyway.core.migration.SchemaVersion;
import com.googlecode.flyway.core.util.jdbc.DriverDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Test to demonstrate the migration functionality using SinoDB Dynamic Server.
 */
@Category(DbCategory.SinoDB.class)
public class SinoDBMigrationMediumTest extends SinoDBMigrationTestCase {
    @Override
    protected DataSource createDataSource(Properties customProperties) throws Exception {
        String user = customProperties.getProperty("informix.user", "informixadmin");
        String password = customProperties.getProperty("informix.password", "flyway");
        String url = customProperties.getProperty("informix.url", "jdbc:informix-sqli://192.168.227.131:3307/flyway:INFORMIXSERVER=demo_on");

        return new DriverDataSource(null, url, user, password);
    }

    @Override
    protected String getQuoteLocation() {
        return "migration/quote";
    }

    @Test
    public void sequence() throws Exception {
        flyway.setLocations("migration/dbsupport/sinodb/sql/sequence");
        flyway.migrate();

        SchemaVersion schemaVersion = flyway.status().getVersion();
        assertEquals("1", schemaVersion.toString());
        assertEquals("Sequence", flyway.status().getDescription());

        assertEquals(666, jdbcTemplate.queryForInt("SELECT start_val FROM syssequences"));

        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void mqt() throws Exception {
        flyway.setLocations("migration/dbsupport/sinodb/sql/mqt");
        flyway.migrate();

        SchemaVersion schemaVersion = flyway.status().getVersion();
        assertEquals("1", schemaVersion.toString());
        assertEquals("Mqt", flyway.status().getDescription());

        assertEquals(2, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM empl_mqt"));

        flyway.clean();
        flyway.migrate();
    }


    @Test
    public void trigger() throws Exception {
        flyway.setLocations("migration/dbsupport/sinodb/sql/trigger");
        flyway.migrate();

        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void procedure() throws Exception {
        flyway.setLocations("migration/dbsupport/sinodb/sql/procedure");
        flyway.migrate();

        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void type() throws Exception {
        flyway.setLocations("migration/dbsupport/sinodb/sql/type");
        flyway.migrate();

        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void function() throws Exception {
        flyway.setLocations("migration/dbsupport/sinodb/sql/function");
        flyway.migrate();

        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void versioned() throws Exception {
        flyway.setLocations("migration/dbsupport/sinodb/sql/versioned");
        flyway.migrate();

        flyway.clean();
        flyway.migrate();
    }
}
