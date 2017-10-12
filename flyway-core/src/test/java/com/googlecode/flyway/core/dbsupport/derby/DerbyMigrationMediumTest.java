/**
 * Copyright 2010-2013 Axel Fontaine and the many contributors.
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
package com.googlecode.flyway.core.dbsupport.derby;

import com.googlecode.flyway.core.migration.MigrationTestCase;
import com.googlecode.flyway.core.util.jdbc.DriverDataSource;
import org.junit.experimental.categories.Category;
import com.googlecode.flyway.core.DbCategory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test to demonstrate the migration functionality using Derby.
 */
@Category(DbCategory.Derby.class)
public class DerbyMigrationMediumTest extends MigrationTestCase {
    static {
        System.setProperty("derby.stream.error.field", "java.lang.System.err");
    }

    @Override
    public void tearDown() throws Exception {
        try {
            new DriverDataSource(null, "jdbc:derby:memory:flyway_db;drop=true", "", "").getConnection();
        } catch (SQLException e) {
            //OK, expected error 08006. See http://db.apache.org/derby/docs/dev/devguide/cdevdvlpinmemdb.html
        }

        super.tearDown();
    }

    @Override
    protected DataSource createDataSource(Properties customProperties) {
        return new DriverDataSource(null, "jdbc:derby:memory:flyway_db;create=true", "", "");
    }

    @Override
    protected String getQuoteLocation() {
        return "migration/quote";
    }
}