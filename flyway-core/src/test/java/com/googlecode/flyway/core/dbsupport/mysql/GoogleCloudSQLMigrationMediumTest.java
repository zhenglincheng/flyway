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
package com.googlecode.flyway.core.dbsupport.mysql;

import com.google.appengine.api.rdbms.dev.LocalRdbmsService;
import com.google.appengine.tools.development.testing.LocalRdbmsServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.flyway.core.util.ClassUtils;
import com.googlecode.flyway.core.util.jdbc.DriverDataSource;
import org.junit.experimental.categories.Category;
import com.googlecode.flyway.core.DbCategory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Test to demonstrate the migration functionality using Google Cloud SQL.
 */
@SuppressWarnings({"JavaDoc"})
@Category(DbCategory.MySQL.class)
public class GoogleCloudSQLMigrationMediumTest extends MySQLMigrationTestCase {
    private LocalServiceTestHelper helper;

    @Override
    protected DataSource createDataSource(Properties customProperties) throws Exception {
        String user = customProperties.getProperty("mysql.user", "flyway");
        String password = customProperties.getProperty("mysql.password", "flyway");
        String url = customProperties.getProperty("mysql.cloudsql_url", "jdbc:mysql://localhost:3306/flyway_cloudsql_db");

        LocalRdbmsServiceTestConfig config = new LocalRdbmsServiceTestConfig();
        config.setServerType(LocalRdbmsService.ServerType.LOCAL);
        ClassUtils.instantiate("com.mysql.jdbc.Driver");
        config.setDriverClass("com.mysql.jdbc.Driver");
        config.setJdbcConnectionStringFormat(url);
        config.setUser(user);
        config.setPassword(password);
        helper = new LocalServiceTestHelper(config);
        helper.setUp();

        return new DriverDataSource(null, "jdbc:google:rdbms://localhost/flyway_cloudsql_db", "", "");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        helper.tearDown();
    }
}
