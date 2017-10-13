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

import com.googlecode.flyway.core.DbCategory;
import com.googlecode.flyway.core.util.jdbc.DriverDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import org.junit.experimental.categories.Category;

/**
 * Test to demonstrate the migration functionality using Informix.
 */
@Category(DbCategory.SinoDB.class)
public class SinoDBConcurrentMigrationMediumTest extends SinoDBConcurrentMigrationTestCase {
    @Override
    protected DataSource createDataSource(Properties customProperties) throws Exception {
        String user = customProperties.getProperty("informix.user", "informixadmin");
        String password = customProperties.getProperty("informix.password", "flyway");
        String url = customProperties.getProperty("informix.url", "jdbc:informix-sqli://192.168.227.131:3307/flyway:INFORMIXSERVER=demo_on");

        return new DriverDataSource(null, url, user, password);
    }
}