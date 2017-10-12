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
package com.googlecode.flyway.core.dbsupport.h2;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;
import com.googlecode.flyway.core.util.jdbc.JdbcUtils;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Migration triggering the exception.
 */
public class V1__TriggerException implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("CREATE TABLE clinics (id INT NOT NULL)");
            statement.execute("CREATE TRIGGER clinics_history_trigger AFTER INSERT ON clinics FOR EACH ROW CALL\n" +
                    "\"com.googlecode.flyway.core.dbsupport.h2.TestTrigger\";");
        } finally {
            JdbcUtils.closeStatement(statement);
        }
    }
}
